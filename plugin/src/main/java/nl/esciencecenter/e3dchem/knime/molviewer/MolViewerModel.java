package nl.esciencecenter.e3dchem.knime.molviewer;

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
 * This is the model implementation of MolViewer.
 *
 */
public class MolViewerModel extends NodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger.getLogger(MolViewerModel.class);

	public static final int LIGAND_PORT = 0;
	public static final String CFGKEY_LIGAND = "ligandColumn";
	public static final String CFGKEY_LIGAND_LABEL = "ligandLabelColumn";
	private static final String LIGANDS_FILE_NAME = "molViewerInternals.ligands.ser.gz";

	private final SettingsModelString m_ligand_column = new SettingsModelString(MolViewerModel.CFGKEY_LIGAND, "");
	private final SettingsModelColumnName m_ligand_label_column = new SettingsModelColumnName(
			MolViewerModel.CFGKEY_LIGAND_LABEL, "");

	private List<Molecule> ligands;

	public static final int PROTEIN_PORT = 1;
	public static final String CFGKEY_PROTEIN = "proteinColumn";
	public static final String CFGKEY_PROTEIN_LABEL = "proteinLabelColumn";
	private static final String PROTEINS_FILE_NAME = "molViewerInternals.proteins.ser.gz";

	private final SettingsModelString m_protein_column = new SettingsModelString(MolViewerModel.CFGKEY_PROTEIN, "");
	private final SettingsModelColumnName m_protein_label_column = new SettingsModelColumnName(
			MolViewerModel.CFGKEY_PROTEIN_LABEL, "");

	private List<Molecule> proteins;

	public static final String CFG_BROWSERAUTOOPEN = "browserAutoOpen";
	private final SettingsModelBoolean m_browserAutoOpen = new SettingsModelBoolean(CFG_BROWSERAUTOOPEN, true);

	// TODO add pharmacophores column on ligand and/or protein port

	/**
	 * Constructor for the node model.
	 */
	public MolViewerModel() {
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
		String format = "sdf";
		ligands = getMolecules(inData, molPort, molColumnName, molLabelColumn, format);
	}

	private void setProteins(BufferedDataTable[] inData) {
		int molPort = PROTEIN_PORT;
		String molColumnName = m_protein_column.getStringValue();
		SettingsModelColumnName molLabelColumn = m_protein_label_column;
		String format = "pdb";
		proteins = getMolecules(inData, molPort, molColumnName, molLabelColumn, format);
	}

	private List<Molecule> getMolecules(BufferedDataTable[] datatables, int port, String molColumnName,
			SettingsModelColumnName molLabelColumn, String format) {
		BufferedDataTable molDataTable = datatables[port];
		DataTableSpec molSpec = molDataTable.getDataTableSpec();
		int molColIndex = molSpec.findColumnIndex(molColumnName);
		boolean useRowKeyAsLabel = molLabelColumn.useRowID();
		int labelColIndex = molSpec.findColumnIndex(molLabelColumn.getStringValue());

		ArrayList<Molecule> molecules = new ArrayList<Molecule>();
		for (DataRow currRow : molDataTable) {
			Molecule mol = new Molecule();
			mol.format = format;
			DataCell currCell = currRow.getCell(molColIndex);
			mol.data = ((StringValue) currCell).getStringValue();
			mol.id = currRow.getKey().getString();
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
		logger.warn("Reset node");
		// Models build during execute are cleared here.
		// Also data handled in load/saveInternals will be erased here.
		ligands = null;
		proteins = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {

		// check if user settings are available, fit to the incoming
		// table structure, and the incoming types are feasible for the node
		// to execute. If the node can execute in its current state return
		// the spec of its output data table(s) (if you can, otherwise an array
		// with null elements), or throw an exception with a useful user message

		logger.warn("configure");

		boolean hasSdfColumn = false;
		for (int i = 0; i < inSpecs[LIGAND_PORT].getNumColumns(); i++) {
			DataColumnSpec columnSpec = inSpecs[LIGAND_PORT].getColumnSpec(i);
			if (columnSpec.getType().isCompatible(SdfValue.class)) {
				// found one numeric column
				hasSdfColumn = true;
			}
		}
		if (!hasSdfColumn) {
			throw new InvalidSettingsException("Input table on input port 0 must contain at least one SDF column");
		}

		return new DataTableSpec[] {};
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
