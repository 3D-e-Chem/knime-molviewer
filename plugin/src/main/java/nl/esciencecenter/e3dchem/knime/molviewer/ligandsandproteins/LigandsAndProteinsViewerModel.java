package nl.esciencecenter.e3dchem.knime.molviewer.ligandsandproteins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import nl.esciencecenter.e3dchem.knime.molviewer.server.api.Molecule;

/**
 * This is the model implementation of ligands and proteins MolViewer.
 *
 */
public class LigandsAndProteinsViewerModel extends NodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger.getLogger(LigandsAndProteinsViewerModel.class);

	public static final int LIGAND_PORT = 0;
	public static final String CFGKEY_LIGAND = "ligandColumn";
	public static final String CFGKEY_LIGAND_LABEL = "ligandLabelColumn";
	private static final String LIGANDS_FILE_NAME = "molViewerInternals.ligands.ser.gz";

	private final SettingsModelString m_ligand_column = new SettingsModelString(
			LigandsAndProteinsViewerModel.CFGKEY_LIGAND, "");
	private final SettingsModelColumnName m_ligand_label_column = new SettingsModelColumnName(
			LigandsAndProteinsViewerModel.CFGKEY_LIGAND_LABEL, "");

	private List<Molecule> ligands;

	public static final int PROTEIN_PORT = 1;
	public static final String CFGKEY_PROTEIN = "proteinColumn";
	public static final String CFGKEY_PROTEIN_LABEL = "proteinLabelColumn";
	private static final String PROTEINS_FILE_NAME = "molViewerInternals.proteins.ser.gz";

	private final SettingsModelString m_protein_column = new SettingsModelString(
			LigandsAndProteinsViewerModel.CFGKEY_PROTEIN, "");
	private final SettingsModelColumnName m_protein_label_column = new SettingsModelColumnName(
			LigandsAndProteinsViewerModel.CFGKEY_PROTEIN_LABEL, "");

	private List<Molecule> proteins;

	public static final String CFG_BROWSERAUTOOPEN = "browserAutoOpen";
	private final SettingsModelBoolean m_browserAutoOpen = new SettingsModelBoolean(CFG_BROWSERAUTOOPEN, true);

	// TODO add pharmacophores column on ligand and/or protein port

	/**
	 * Constructor for the node model.
	 */
	public LigandsAndProteinsViewerModel() {
		super(2, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		setLigands(inData);
		setProteins(inData);
		return new BufferedDataTable[] {};
	}

	private void setLigands(final BufferedDataTable[] inData) {
		int molPort = LIGAND_PORT;
		String molColumnName = m_ligand_column.getStringValue();
		SettingsModelColumnName molLabelColumn = m_ligand_label_column;
		ligands = getMolecules(inData, molPort, molColumnName, molLabelColumn);
	}

	private void setProteins(BufferedDataTable[] inData) {
		int molPort = PROTEIN_PORT;
		String molColumnName = m_protein_column.getStringValue();
		SettingsModelColumnName molLabelColumn = m_protein_label_column;
		proteins = getMolecules(inData, molPort, molColumnName, molLabelColumn);
	}

	private List<Molecule> getMolecules(BufferedDataTable[] datatables, int port, String molColumnName,
			SettingsModelColumnName molLabelColumn) {
		BufferedDataTable molDataTable = datatables[port];
		DataTableSpec molSpec = molDataTable.getDataTableSpec();
		int molColIndex = molSpec.findColumnIndex(molColumnName);
		boolean useRowKeyAsLabel = molLabelColumn.useRowID();
		int labelColIndex = molSpec.findColumnIndex(molLabelColumn.getStringValue());

		ArrayList<Molecule> molecules = new ArrayList<Molecule>();
		for (DataRow currRow : molDataTable) {
			Molecule mol = new Molecule();
			DataCell currCell = currRow.getCell(molColIndex);
			mol.id = currRow.getKey().getString();
			if (currCell.getType().isCompatible(Mol2Value.class)) {
				mol.format = "mol2";
			} else if (currCell.getType().isCompatible(SdfValue.class)) {
				mol.format = "sdf";
			} else if (currCell.getType().isCompatible(PdbValue.class)) {
				mol.format = "pdb";
			} else {
				logger.warn("Row " + mol.id + " is has invalid format, skipping");
				continue;
			}
			mol.data = ((StringValue) currCell).getStringValue();
			if (useRowKeyAsLabel) {
				mol.label = currRow.getKey().getString();
			} else {
				currCell = currRow.getCell(labelColIndex);
				mol.label = ((StringValue) currCell).getStringValue();
			}
			molecules.add(mol);
		}
		return molecules;
	}

	public List<Molecule> getLigands() {
		return ligands;
	}

	public List<Molecule> getProteins() {
		return proteins;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		// Models build during execute are cleared here.
		// Also data handled in load/saveInternals will be erased here.
		ligands = null;
		proteins = null;
	}

	private interface isCompatibleLambda {
		boolean test(DataColumnSpec s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		isCompatibleLambda compatibleLigand = (DataColumnSpec s) -> s.getType().isCompatible(SdfValue.class)
				|| s.getType().isCompatible(Mol2Value.class);
		isCompatibleLambda compatibleProtein = (DataColumnSpec s) -> s.getType().isCompatible(PdbValue.class);
		// as PDB, SDF and Mol2 are also string compatible exclude them for
		// label
		isCompatibleLambda compatibleLabel = (DataColumnSpec s) -> s.getType().isCompatible(StringValue.class)
				&& !(compatibleLigand.test(s) || compatibleProtein.test(s));

		configureColumn(inSpecs[LIGAND_PORT], m_ligand_column, compatibleLigand, "molecules", "ligands");
		configureColumnWithRowID(inSpecs[LIGAND_PORT], m_ligand_label_column, compatibleLabel, "labels", "ligands");
		configureColumn(inSpecs[PROTEIN_PORT], m_protein_column, compatibleProtein, "molecules", "proteins");
		configureColumnWithRowID(inSpecs[PROTEIN_PORT], m_protein_label_column, compatibleLabel, "labels", "proteins");

		return new DataTableSpec[] {};
	}

	private void configureColumnWithRowID(DataTableSpec spec, SettingsModelColumnName setting,
			isCompatibleLambda isCompatible, String columnLabel, String specName) throws InvalidSettingsException {
		int colIndex = -1;
		boolean settingIsDefault = setting.getStringValue() == "" || setting.getStringValue() == null || setting.getStringValue().isEmpty();
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
				setWarningMessage("Column '" + selectedColumn + "' auto selected as " + columnLabel + " column on port "
						+ specName);
				setting.setStringValue(selectedColumn);
			}
		} else {
			if (!setting.useRowID()) {
				colIndex = spec.findColumnIndex(setting.getStringValue());
				if (colIndex < 0) {
					throw new InvalidSettingsException(
							"Column '" + setting.getStringValue() + "' missing on port " + specName);
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

	private void configureColumn(DataTableSpec spec, SettingsModelString setting, isCompatibleLambda isCompatible,
			String columnLabel, String specName) throws InvalidSettingsException {
		int colIndex = -1;
		boolean settingIsDefault = setting.getStringValue() == "" || setting.getStringValue() == null || setting.getStringValue().isEmpty();
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
			setWarningMessage("Column '" + selectedColumn + "' auto selected as column with " + columnLabel
					+ " on port " + specName);
			setting.setStringValue(selectedColumn);
		} else {
			colIndex = spec.findColumnIndex(setting.getStringValue());
			if (colIndex < 0) {
				throw new InvalidSettingsException(
						"Column '" + setting.getStringValue() + "' missing on port " + specName);
			}
			if (!isCompatible.test(spec.getColumnSpec(colIndex))) {
				throw new InvalidSettingsException("Column '" + setting.getStringValue()
						+ "' is incompatible, should be column with " + columnLabel + " on port " + specName);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		m_ligand_column.saveSettingsTo(settings);
		m_ligand_label_column.saveSettingsTo(settings);
		m_protein_column.saveSettingsTo(settings);
		m_protein_label_column.saveSettingsTo(settings);
		m_browserAutoOpen.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_ligand_column.loadSettingsFrom(settings);
		m_ligand_label_column.loadSettingsFrom(settings);
		m_protein_column.loadSettingsFrom(settings);
		m_protein_label_column.loadSettingsFrom(settings);
		m_browserAutoOpen.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_ligand_column.validateSettings(settings);
		m_ligand_label_column.validateSettings(settings);
		m_protein_column.validateSettings(settings);
		m_protein_label_column.validateSettings(settings);
		m_browserAutoOpen.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		ligands = loadInternalsMolecules(new File(internDir, LIGANDS_FILE_NAME));
		proteins = loadInternalsMolecules(new File(internDir, PROTEINS_FILE_NAME));
	}

	public List<Molecule> loadInternalsMolecules(final File file) throws IOException, FileNotFoundException {
		if (!file.canRead()) {
			return new ArrayList<Molecule>();
		}
		ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
		try {
			@SuppressWarnings("unchecked")
			List<Molecule> ino = (List<Molecule>) in.readObject();
			return ino;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return new ArrayList<Molecule>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		saveInternalsMolecules(new File(internDir, LIGANDS_FILE_NAME), ligands);
		saveInternalsMolecules(new File(internDir, PROTEINS_FILE_NAME), proteins);
	}

	public void saveInternalsMolecules(final File file, List<Molecule> molecules)
			throws FileNotFoundException, IOException {
		ObjectOutputStream out = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
		out.writeObject(molecules);
		out.flush();
		out.close();
	}

	/**
	 * @return If browser should be automatically opened
	 */
	public boolean isBrowserAutoOpen() {
		return m_browserAutoOpen.getBooleanValue();
	}

}
