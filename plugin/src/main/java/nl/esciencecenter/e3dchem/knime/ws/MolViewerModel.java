package nl.esciencecenter.e3dchem.knime.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.knime.chem.types.SdfValue;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import nl.esciencecenter.e3dchem.knime.ws.server.api.Molecule;

/**
 * This is the model implementation of MolViewer.
 *
 */
public class MolViewerModel extends NodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger.getLogger(MolViewerModel.class);

	/**
	 * the settings key which is used to retrieve and store the settings (from
	 * the dialog or from a settings file) (package visibility to be usable from
	 * the dialog).
	 */
	static final String CFGKEY_COUNT = "Count";

	/** initial default count value. */
	static final int DEFAULT_COUNT = 100;

	public static final String CFGKEY_LIGAND = "ligandColumn";

	public static final int LIGAND_PORT = 0;

	// example value: the models count variable filled from the dialog
	// and used in the models execution method. The default components of the
	// dialog work with "SettingsModels".
	private final SettingsModelIntegerBounded m_count = new SettingsModelIntegerBounded(MolViewerModel.CFGKEY_COUNT,
			MolViewerModel.DEFAULT_COUNT, Integer.MIN_VALUE, Integer.MAX_VALUE);

	private final SettingsModelString m_ligand_column = new SettingsModelString(MolViewerModel.CFGKEY_LIGAND, "");

	private List<Molecule> ligands;

	private static final String LIGANDS_FILE_NAME = "molViewerInternals.ligands.ser.gz";

	// TODO add pharmacophores column on ligand and/or protein port

	// TODO add proteins column on protein port

	/**
	 * Constructor for the node model.
	 */
	public MolViewerModel() {
		// one incoming port and zero outgoing port is assumed
		super(1, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		BufferedDataTable ligandDataTable = inData[LIGAND_PORT];
		int ligandColIndex = ligandDataTable.getDataTableSpec().findColumnIndex(m_ligand_column.getStringValue());

		ligands = new ArrayList<Molecule>();
		for (DataRow currRow : ligandDataTable) {
			DataCell currCell = currRow.getCell(ligandColIndex);
			Molecule mol = new Molecule();
			mol.format = "sdf";
			mol.id = currRow.getKey().getString();
			// TODO add label column to dialog
			mol.label = currRow.getKey().getString();
			mol.data = ((SdfValue) currCell).getSdfValue();
			ligands.add(mol);
		}

		return new BufferedDataTable[] {};
	}

	public List<Molecule> getLigands() {
		return ligands;
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
			throw new InvalidSettingsException("Input table must contain at " + "least one SDF column");
		}

		return new DataTableSpec[] {};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		// save user settings to the config object.

		m_count.saveSettingsTo(settings);
		m_ligand_column.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

		// load (valid) settings from the config object.
		// It can be safely assumed that the settings are valided by the
		// method below.

		m_count.loadSettingsFrom(settings);
		m_ligand_column.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		// check if the settings could be applied to our model
		// e.g. if the count is in a certain range (which is ensured by the
		// SettingsModel).
		// Do not actually set any values of any member variables.

		m_count.validateSettings(settings);
		m_ligand_column.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {

		// load internal data.
		// Everything handed to output ports is loaded automatically (data
		// returned by the execute method, models loaded in loadModelContent,
		// and user settings set through loadSettingsFrom - is all taken care
		// of). Load here only the other internals that need to be restored
		// (e.g. data used by the views).
		logger.warn("loadInternals");

		File file = new File(internDir, LIGANDS_FILE_NAME);
		if (!file.canRead()) {
			return;
		}
		ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
		try {
			Molecule[] array = (Molecule[]) in.readObject();
			ligands = Arrays.asList(array);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		in.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {

		// save internal models.
		// Everything written to output ports is saved automatically (data
		// returned by the execute method, models saved in the saveModelContent,
		// and user settings saved through saveSettingsTo - is all taken care
		// of). Save here only the other internals that need to be preserved
		// (e.g. data used by the views).
		logger.warn("saveInternals");

		File file = new File(internDir, LIGANDS_FILE_NAME);
		ObjectOutputStream out = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
		out.writeObject(ligands.toArray());
		out.flush();
		out.close();
	}

}
