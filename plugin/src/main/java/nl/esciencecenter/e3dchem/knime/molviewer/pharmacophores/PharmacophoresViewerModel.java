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
import nl.esciencecenter.e3dchem.knime.molviewer.server.api.AnonymousMolecule;
import nl.esciencecenter.e3dchem.knime.molviewer.server.api.Pharmacophore;

public class PharmacophoresViewerModel extends ViewerModel {
	private List<Pharmacophore> pharmacophores = new ArrayList<>();
	public static final int PORT = 0;
	private static final String TABLE_FILENAME = "internals.ser.gz";
	public static final String CFGKEY_LABEL = "labelColumn";
	public static final String CFGKEY_PHARMACOPHORE = "pharColumn";
	public static final String CFGKEY_LIGAND = "ligandColumn";
	public static final String CFGKEY_PROTEIN = "proteinColumn";
	private final SettingsModelColumnName m_label_column = new SettingsModelColumnName(
			PharmacophoresViewerModel.CFGKEY_LABEL, "");
	private final SettingsModelString m_phar_column = new SettingsModelString(
			PharmacophoresViewerModel.CFGKEY_PHARMACOPHORE, "");
	private final SettingsModelString m_ligand_column = new SettingsModelString(PharmacophoresViewerModel.CFGKEY_LIGAND,
			"");
	private final SettingsModelString m_protein_column = new SettingsModelString(
			PharmacophoresViewerModel.CFGKEY_PROTEIN, "");

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
		int pharIndex = spec.findColumnIndex(m_phar_column.getStringValue());
		int labelIndex = spec.findColumnIndex(m_label_column.getColumnName());
		boolean useRowKeyAsLabel = m_label_column.useRowID();
		int ligandIndex = spec.findColumnIndex(m_ligand_column.getStringValue());
		int proteinIndex = spec.findColumnIndex(m_protein_column.getStringValue());

		pharmacophores.clear();
		for (DataRow row : dataTable) {
			Pharmacophore phar = new Pharmacophore();
			phar.id = row.getKey().getString();
			phar.pharmacophore = new AnonymousMolecule(((StringValue) row.getCell(pharIndex)).getStringValue(), "phar");
			if (useRowKeyAsLabel) {
				phar.label = row.getKey().getString();
			} else {
				phar.label = ((StringValue) row.getCell(labelIndex)).getStringValue();
			}
			if (ligandIndex > -1) {
				try {
					phar.ligand = new AnonymousMolecule(((StringValue) row.getCell(ligandIndex)).getStringValue(),
							guessCellFormat(row.getCell(ligandIndex)));
				} catch (InvalidFormatException e) {
					logger.warn("Row " + phar.id + " is has invalid format for ligand, skipping");
				}
			}
			if (proteinIndex > -1) {
				try {
					phar.ligand = new AnonymousMolecule(((StringValue) row.getCell(proteinIndex)).getStringValue(),
							guessCellFormat(row.getCell(proteinIndex)));
				} catch (InvalidFormatException e) {
					logger.warn("Row " + phar.id + " is has invalid format for protein, skipping");
				}
			}
			pharmacophores.add(phar);
		}
	}

	public List<Pharmacophore> getPharmacophores() {
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

		DataTableSpec spec = inSpecs[PORT];
		configureColumnWithRowID(spec, m_label_column, compatibleLabel, "labels", "pharmacophores");
		configureColumn(spec, m_phar_column, compatibleLabel, "pharmacophores", "pharmacophores");
		configureColumn(spec, m_ligand_column, compatibleLigand, "ligands", "pharmacophores");
		configureColumn(spec, m_protein_column, compatibleProtein, "proteins", "pharmacophores");

		return new DataTableSpec[] {};
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		m_label_column.saveSettingsTo(settings);
		m_phar_column.saveSettingsTo(settings);
		m_ligand_column.saveSettingsTo(settings);
		m_protein_column.saveSettingsTo(settings);
		super.saveSettingsTo(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		m_label_column.loadSettingsFrom(settings);
		m_phar_column.loadSettingsFrom(settings);
		m_ligand_column.loadSettingsFrom(settings);
		m_protein_column.loadSettingsFrom(settings);
		super.loadValidatedSettingsFrom(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		m_label_column.validateSettings(settings);
		m_phar_column.validateSettings(settings);
		m_ligand_column.validateSettings(settings);
		m_protein_column.validateSettings(settings);
		super.validateSettings(settings);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		File file = new File(nodeInternDir, TABLE_FILENAME);
		if (!file.canRead()) {
			pharmacophores.clear();
			return;
		}
		ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
		try {
			pharmacophores = (List<Pharmacophore>) in.readObject();
		} catch (ClassNotFoundException e) {
			pharmacophores.clear();
			logger.warn(e.getMessage(), e);
		} finally {
			in.close();
		}
	}

	@Override
	public void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		File file = new File(nodeInternDir, TABLE_FILENAME);
		ObjectOutputStream out = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
		out.writeObject(pharmacophores);
		out.flush();
		out.close();
	}

	@Override
	protected void reset() {
		pharmacophores.clear();
	}
}
