package nl.esciencecenter.e3dchem.knime.molviewer.ligandsandproteins;

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
public class LigandsAndProteinsViewerModel extends ViewerModel {
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

		configureColumn(inSpecs[LIGAND_PORT], m_ligand_column, compatibleLigand, "molecules", "ligands");
		configureColumnWithRowID(inSpecs[LIGAND_PORT], m_ligand_label_column, compatibleLabel, "labels", "ligands");
		configureColumn(inSpecs[PROTEIN_PORT], m_protein_column, compatibleProtein, "molecules", "proteins");
		configureColumnWithRowID(inSpecs[PROTEIN_PORT], m_protein_label_column, compatibleLabel, "labels", "proteins");

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
		super.saveSettingsTo(settings);
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
		super.loadValidatedSettingsFrom(settings);
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
		super.validateSettings(settings);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		saveInternalsMolecules(new File(internDir, LIGANDS_FILE_NAME), ligands);
		saveInternalsMolecules(new File(internDir, PROTEINS_FILE_NAME), proteins);
	}
}
