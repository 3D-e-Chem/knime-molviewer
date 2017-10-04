package nl.esciencecenter.e3dchem.knime.molviewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.knime.bio.types.PdbValue;
import org.knime.chem.types.Mol2Value;
import org.knime.chem.types.SdfValue;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import nl.esciencecenter.e3dchem.knime.molviewer.server.api.Molecule;

public abstract class ViewerModel extends NodeModel {
    protected static final NodeLogger logger = NodeLogger.getLogger(ViewerModel.class);
    public static final String CFG_BROWSERAUTOOPEN = "browserAutoOpen";
    protected final SettingsModelBoolean browserAutoOpen = new SettingsModelBoolean(CFG_BROWSERAUTOOPEN, true);

    protected ViewerModel(int nrInDataPorts, int nrOutDataPorts) {
        super(nrInDataPorts, nrOutDataPorts);
    }

    protected interface isCompatibleLambda {
        boolean test(DataColumnSpec s);
    }

    public void configureColumnWithRowID(DataTableSpec spec, SettingsModelColumnName setting, isCompatibleLambda isCompatible,
            String columnLabel, String specName) throws InvalidSettingsException {
        int colIndex = -1;
        boolean settingIsDefault = setting.getStringValue() == "" || setting.getStringValue() == null
                || setting.getStringValue().isEmpty();
        boolean useRowId = setting.useRowID();
        if (settingIsDefault && !useRowId) {
            for (int i = 0; i < spec.getNumColumns(); i++) {
                DataColumnSpec columnSpec = spec.getColumnSpec(i);
                if (isCompatible.test(columnSpec)) {
                    // Select first column that matches
                    colIndex = i;
                    break;
                }
            }
            if (colIndex == -1) {
                setWarningMessage("RowID auto selected as " + columnLabel + " column on port " + specName);
                setting.setSelection(null, true);
            } else {
                String selectedColumn = spec.getColumnSpec(colIndex).getName();
                setWarningMessage(
                        "Column '" + selectedColumn + "' auto selected as " + columnLabel + " column on port " + specName);
                setting.setStringValue(selectedColumn);
            }
        } else {
            if (!setting.useRowID()) {
                colIndex = spec.findColumnIndex(setting.getStringValue());
                if (colIndex < 0) {
                    throw new InvalidSettingsException("Column '" + setting.getStringValue() + "' missing on port " + specName);
                }
                if (!isCompatible.test(spec.getColumnSpec(colIndex))) {
                    throw new InvalidSettingsException("Column '" + setting.getStringValue()
                            + "' is incompatible, should be column with " + columnLabel + " on port " + specName);
                }
            } else {
                // RowID selected
            }
        }
    }

    public void configureColumn(DataTableSpec spec, SettingsModelString setting, isCompatibleLambda isCompatible,
            String columnLabel, String specName) throws InvalidSettingsException {
        int colIndex = -1;
        boolean settingIsDefault = setting.getStringValue() == "" || setting.getStringValue() == null
                || setting.getStringValue().isEmpty();
        if (settingIsDefault) {
            for (int i = 0; i < spec.getNumColumns(); i++) {
                DataColumnSpec columnSpec = spec.getColumnSpec(i);
                if (isCompatible.test(columnSpec)) {
                    // Select first column that matches
                    colIndex = i;
                    break;
                }
            }
            if (colIndex == -1) {
                throw new InvalidSettingsException("Column with " + columnLabel + " missing on port " + specName);
            }
            String selectedColumn = spec.getColumnSpec(colIndex).getName();
            setWarningMessage(
                    "Column '" + selectedColumn + "' auto selected as column with " + columnLabel + " on port " + specName);
            setting.setStringValue(selectedColumn);
        } else {
            colIndex = spec.findColumnIndex(setting.getStringValue());
            if (colIndex < 0) {
                throw new InvalidSettingsException("Column '" + setting.getStringValue() + "' missing on port " + specName);
            }
            if (!isCompatible.test(spec.getColumnSpec(colIndex))) {
                throw new InvalidSettingsException("Column '" + setting.getStringValue()
                        + "' is incompatible, should be column with " + columnLabel + " on port " + specName);
            }
        }
    }

    public void configureColumnOptional(DataTableSpec spec, SettingsModelString setting, isCompatibleLambda isCompatible,
            String columnLabel, String specName) throws InvalidSettingsException {
        int colIndex = -1;
        boolean settingIsDefault = setting.getStringValue() == "" || setting.getStringValue() == null
                || setting.getStringValue().isEmpty();
        if (!settingIsDefault) {
            colIndex = spec.findColumnIndex(setting.getStringValue());
            if (colIndex < 0) {
                throw new InvalidSettingsException("Column '" + setting.getStringValue() + "' missing on port " + specName);
            }
            if (!isCompatible.test(spec.getColumnSpec(colIndex))) {
                throw new InvalidSettingsException("Column '" + setting.getStringValue()
                        + "' is incompatible, should be column with " + columnLabel + " on port " + specName);
            }
        }
    }

    public List<Molecule> loadInternalsMolecules(final File file) throws IOException {
        if (!file.canRead()) {
            return new ArrayList<Molecule>();
        }
        FileInputStream fstream = new FileInputStream(file);
        return loadInternalsMolecules(fstream);
    }

    public List<Molecule> loadInternalsMolecules(final InputStream fstream) throws IOException {
        ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(fstream));
        try {
            @SuppressWarnings("unchecked")
            List<Molecule> ino = (ArrayList<Molecule>) in.readObject();
            return ino;
        } catch (ClassNotFoundException e) {
            logger.warn(e.getMessage(), e);
        } finally {
            in.close();
        }
        return new ArrayList<Molecule>();
    }

    public void saveInternalsMolecules(final File file, List<Molecule> molecules) throws IOException {
        FileOutputStream in = null;
        ObjectOutputStream out = null;
        try {
            in = new FileOutputStream(file);
            out = new ObjectOutputStream(new GZIPOutputStream(in));
            out.writeObject((ArrayList<Molecule>) molecules);
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        browserAutoOpen.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        browserAutoOpen.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        browserAutoOpen.validateSettings(settings);
    }

    /**
     * @return If browser should be automatically opened
     */
    public boolean isBrowserAutoOpen() {
        return browserAutoOpen.getBooleanValue();
    }

    protected String guessCellFormat(DataCell cell) throws InvalidFormatException {
        if (cell.getType().isCompatible(Mol2Value.class)) {
            return "mol2";
        } else if (cell.getType().isCompatible(SdfValue.class)) {
            return "sdf";
        } else if (cell.getType().isCompatible(PdbValue.class)) {
            return "pdb";
        } else {
            throw new InvalidFormatException();
        }
    }
}
