package nl.esciencecenter.e3dchem.knime.molviewer.ligands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import nl.esciencecenter.e3dchem.knime.molviewer.InvalidFormatException;
import nl.esciencecenter.e3dchem.knime.molviewer.ViewerModel;
import nl.esciencecenter.e3dchem.knime.molviewer.server.api.Molecule;

/**
 * This is the model implementation of ligands and proteins MolViewer.
 *
 */
public class LigandsViewerModel extends ViewerModel {
	public static final int LIGAND_PORT = 0;
	public static final String CFGKEY_LIGAND = "ligandColumn";
	public static final String CFGKEY_LIGAND_LABEL = "ligandLabelColumn";
	private static final String LIGANDS_FILE_NAME = "molViewerInternals.ligands.ser.gz";

	private final SettingsModelString ligandColumn = new SettingsModelString(
			LigandsViewerModel.CFGKEY_LIGAND, "");
	private final SettingsModelColumnName ligandLabelColumn = new SettingsModelColumnName(
			LigandsViewerModel.CFGKEY_LIGAND_LABEL, "");

	private List<Molecule> ligands;


	/**
	 * Constructor for the node model.
	 */
	public LigandsViewerModel() {
		super(1, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		setLigands(inData);
		return new BufferedDataTable[] {};
	}

	private void setLigands(final BufferedDataTable[] inData) {
		int molPort = LIGAND_PORT;
		String molColumnName = ligandColumn.getStringValue();
		SettingsModelColumnName molLabelColumn = ligandLabelColumn;
		ligands = getMolecules(inData, molPort, molColumnName, molLabelColumn);
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
			try {
				mol.format = guessCellFormat(currCell);
			} catch (InvalidFormatException e) {
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		// Models build during execute are cleared here.
		// Also data handled in load/saveInternals will be erased here.
		ligands = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		isCompatibleLambda compatibleLigand = (DataColumnSpec s) -> s.getType().isCompatible(SdfValue.class)
				|| s.getType().isCompatible(Mol2Value.class);
		isCompatibleLambda compatibleProtein = (DataColumnSpec s) -> s.getType().isCompatible(PdbValue.class)
				|| s.getType().isCompatible(Mol2Value.class);
		// as PDB, SDF and Mol2 are also string compatible exclude them for
		// label
		isCompatibleLambda compatibleLabel = (DataColumnSpec s) -> s.getType().isCompatible(StringValue.class)
				&& !(compatibleLigand.test(s) || compatibleProtein.test(s));

		configureColumn(inSpecs[LIGAND_PORT], ligandColumn, compatibleLigand, "molecules", "ligands");
		configureColumnWithRowID(inSpecs[LIGAND_PORT], ligandLabelColumn, compatibleLabel, "labels", "ligands");

		return new DataTableSpec[] {};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		ligandColumn.saveSettingsTo(settings);
		ligandLabelColumn.saveSettingsTo(settings);
		super.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		ligandColumn.loadSettingsFrom(settings);
		ligandLabelColumn.loadSettingsFrom(settings);
		super.loadValidatedSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		ligandColumn.validateSettings(settings);
		ligandLabelColumn.validateSettings(settings);
		super.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		ligands = loadInternalsMolecules(new File(internDir, LIGANDS_FILE_NAME));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		saveInternalsMolecules(new File(internDir, LIGANDS_FILE_NAME), ligands);
	}
}
