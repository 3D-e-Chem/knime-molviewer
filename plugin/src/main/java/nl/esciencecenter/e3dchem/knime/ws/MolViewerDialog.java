package nl.esciencecenter.e3dchem.knime.ws;

import org.knime.chem.types.SdfValue;
import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "MolViewer" Node.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 */
public class MolViewerDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring MolViewer node dialog. This is just a suggestion
	 * to demonstrate possible default dialog components.
	 */
	@SuppressWarnings("unchecked")
	protected MolViewerDialog() {
		super();

		createNewGroup("Ligands");

		addDialogComponent(
				new DialogComponentColumnNameSelection(new SettingsModelString(MolViewerModel.CFGKEY_LIGAND, null),
						"Column with SDF formatted molecules", MolViewerModel.LIGAND_PORT, SdfValue.class));

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelColumnName(MolViewerModel.CFGKEY_LIGAND_LABEL, null), "Column with labels",
				MolViewerModel.LIGAND_PORT, StringValue.class));

		createNewGroup("Proteins");

		addDialogComponent(
				new DialogComponentColumnNameSelection(new SettingsModelString(MolViewerModel.CFGKEY_PROTEIN, null),
						"Column with PDB formatted molecules", MolViewerModel.PROTEIN_PORT, StringValue.class));

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelColumnName(MolViewerModel.CFGKEY_PROTEIN_LABEL, null), "Column with labels",
				MolViewerModel.PROTEIN_PORT, StringValue.class));

		closeCurrentGroup();
	}
}
