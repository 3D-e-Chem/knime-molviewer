package nl.esciencecenter.e3dchem.knime.molviewer.pharmacophores;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.knime.bio.types.PdbValue;
import org.knime.chem.types.Mol2Value;
import org.knime.chem.types.SdfValue;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.data.vector.doublevector.DoubleVectorValue;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import nl.esciencecenter.e3dchem.knime.molviewer.InvalidFormatException;
import nl.esciencecenter.e3dchem.knime.molviewer.ViewerModel;
import nl.esciencecenter.e3dchem.knime.molviewer.server.api.Molecule;
import nl.esciencecenter.e3dchem.knime.molviewer.server.api.PharmacophoreContainer;
import nl.esciencecenter.e3dchem.knime.pharmacophore.PharValue;

public class PharmacophoresViewerModel extends ViewerModel {
    private List<PharmacophoreContainer> pharmacophores = new ArrayList<>();
    public static final int PORT = 0;
    private static final String PHARMACOPHORESTXT = "pharmacophores";
    private static final String TABLE_FILENAME = "internals.ser.gz";
    public static final String CFGKEY_LABEL = "labelColumn";
    public static final String CFGKEY_PHARMACOPHORE = "pharColumn";
    public static final String CFGKEY_LIGAND = "ligandColumn";
    public static final String CFGKEY_PROTEIN = "proteinColumn";
    public static final String CFGKEY_TRANSFORM = "transformColumn";
    private final SettingsModelColumnName labelColumn = new SettingsModelColumnName(PharmacophoresViewerModel.CFGKEY_LABEL, "");
    private final SettingsModelString pharColumn = new SettingsModelString(PharmacophoresViewerModel.CFGKEY_PHARMACOPHORE, "");
    private final SettingsModelString ligandColumn = new SettingsModelString(PharmacophoresViewerModel.CFGKEY_LIGAND, "");
    private final SettingsModelString proteinColumn = new SettingsModelString(PharmacophoresViewerModel.CFGKEY_PROTEIN, "");
    private final SettingsModelString transformColumn = new SettingsModelString(CFGKEY_TRANSFORM, "");

    public PharmacophoresViewerModel() {
        super(1, 0);
    }

    @Override
    protected BufferedDataTable[] execute(BufferedDataTable[] inData, ExecutionContext exec) throws Exception {
        setPharmacophores(inData);
        return new BufferedDataTable[] {};
    }

    private void setPharmacophores(BufferedDataTable[] inData) {
        BufferedDataTable dataTable = inData[PORT];
        DataTableSpec spec = dataTable.getDataTableSpec();
        int pharIndex = spec.findColumnIndex(pharColumn.getStringValue());
        int labelIndex = spec.findColumnIndex(labelColumn.getColumnName());
        int transformIndex = spec.findColumnIndex(transformColumn.getStringValue());
        boolean useRowKeyAsLabel = labelColumn.useRowID();
        int ligandIndex = spec.findColumnIndex(ligandColumn.getStringValue());
        int proteinIndex = spec.findColumnIndex(proteinColumn.getStringValue());

        pharmacophores.clear();
        for (DataRow row : dataTable) {
            PharmacophoreContainer phar = new PharmacophoreContainer();
            phar.id = row.getKey().getString();
            phar.pharmacophore = new Molecule(((PharValue) row.getCell(pharIndex)).getStringValue(), "phar");
            if (useRowKeyAsLabel) {
                phar.label = row.getKey().getString();
            } else {
                phar.label = ((StringValue) row.getCell(labelIndex)).getStringValue();
            }
            if (ligandIndex > -1) {
                try {
                    phar.ligand = new Molecule(((StringValue) row.getCell(ligandIndex)).getStringValue(),
                            guessCellFormat(row.getCell(ligandIndex)));
                } catch (InvalidFormatException e) {
                    logger.warn("Row " + phar.id + " is has invalid format for ligand, skipping");
                }
            }
            if (transformIndex > -1) {
                if (row.getCell(transformIndex).isMissing()) {
                    // use default identity transform, aka transformation has no effect
                } else {
                    for (int i = 0; i < 16; i++) {
                        phar.transform[i] = ((DoubleVectorValue) row.getCell(transformIndex)).getValue(i);
                    }
                }
            }
            if (proteinIndex > -1) {
                try {
                    phar.protein = new Molecule(((StringValue) row.getCell(proteinIndex)).getStringValue(),
                            guessCellFormat(row.getCell(proteinIndex)));
                } catch (InvalidFormatException e) {
                    logger.warn("Row " + phar.id + " is has invalid format for protein, skipping");
                }
            }
            pharmacophores.add(phar);
        }
    }

    public List<PharmacophoreContainer> getPharmacophores() {
        return pharmacophores;
    }

    @Override
    protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
        isCompatibleLambda compatibleLigand = (DataColumnSpec s) -> s.getType().isCompatible(SdfValue.class)
                || s.getType().isCompatible(Mol2Value.class);
        isCompatibleLambda compatibleProtein = (DataColumnSpec s) -> s.getType().isCompatible(PdbValue.class)
                || s.getType().isCompatible(Mol2Value.class);
        // as PDB, SDF and Mol2 are also string compatible exclude them for
        // label
        isCompatibleLambda compatibleLabel = (DataColumnSpec s) -> s.getType().isCompatible(StringValue.class)
                && !(compatibleLigand.test(s) || compatibleProtein.test(s));
        isCompatibleLambda compatiblePhar = (DataColumnSpec s) -> s.getType().isCompatible(PharValue.class);
        isCompatibleLambda compatibleTransform = (DataColumnSpec s) -> s.getType().isCompatible(DoubleVectorValue.class);

        DataTableSpec spec = inSpecs[PORT];
        configureColumnWithRowID(spec, labelColumn, compatibleLabel, "labels", PHARMACOPHORESTXT);
        configureColumn(spec, pharColumn, compatiblePhar, PHARMACOPHORESTXT, PHARMACOPHORESTXT);
        configureColumnOptional(spec, ligandColumn, compatibleLigand, "ligands", PHARMACOPHORESTXT);
        configureColumnOptional(spec, transformColumn, compatibleTransform, "transform", PHARMACOPHORESTXT);
        configureColumnOptional(spec, proteinColumn, compatibleProtein, "proteins", PHARMACOPHORESTXT);

        return new DataTableSpec[] {};
    }

    @Override
    protected void saveSettingsTo(NodeSettingsWO settings) {
        labelColumn.saveSettingsTo(settings);
        pharColumn.saveSettingsTo(settings);
        ligandColumn.saveSettingsTo(settings);
        proteinColumn.saveSettingsTo(settings);
        transformColumn.saveSettingsTo(settings);
        super.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
        labelColumn.loadSettingsFrom(settings);
        pharColumn.loadSettingsFrom(settings);
        ligandColumn.loadSettingsFrom(settings);
        proteinColumn.loadSettingsFrom(settings);
        transformColumn.loadSettingsFrom(settings);
        super.loadValidatedSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
        labelColumn.validateSettings(settings);
        pharColumn.validateSettings(settings);
        ligandColumn.validateSettings(settings);
        proteinColumn.validateSettings(settings);
        transformColumn.validateSettings(settings);
        super.validateSettings(settings);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {
        File file = new File(nodeInternDir, TABLE_FILENAME);
        if (!file.canRead()) {
            pharmacophores.clear();
            return;
        }
        ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
        try {
            pharmacophores = (List<PharmacophoreContainer>) in.readObject();
        } catch (ClassNotFoundException e) {
            pharmacophores.clear();
            logger.warn(e.getMessage(), e);
        } finally {
            in.close();
        }
    }

    @Override
    public void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {
        File file = new File(nodeInternDir, TABLE_FILENAME);
        FileOutputStream in = null;
        ObjectOutputStream out = null;
        try {
            in = new FileOutputStream(file);
            out = new ObjectOutputStream(new GZIPOutputStream(in));
            out.writeObject((ArrayList<PharmacophoreContainer>) pharmacophores);
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }

    @Override
    protected void reset() {
        pharmacophores.clear();
    }
}
