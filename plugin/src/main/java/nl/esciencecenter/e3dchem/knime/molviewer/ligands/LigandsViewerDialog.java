package nl.esciencecenter.e3dchem.knime.molviewer.ligands;

import org.knime.chem.types.Mol2Value;
import org.knime.chem.types.SdfValue;
import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import nl.esciencecenter.e3dchem.knime.molviewer.ViewerDialog;

/**
 * <code>NodeDialog</code> for the "MolViewer" Node.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 */
public class LigandsViewerDialog extends ViewerDialog {

	@SuppressWarnings("unchecked")
	protected LigandsViewerDialog() {
		super();

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelString(LigandsViewerModel.CFGKEY_LIGAND, null),
				"Column with SDF or Mol2 formatted molecules", LigandsViewerModel.LIGAND_PORT, SdfValue.class,
				Mol2Value.class));

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelColumnName(LigandsViewerModel.CFGKEY_LIGAND_LABEL, null),
				"Column with labels", LigandsViewerModel.LIGAND_PORT, StringValue.class));

		advancedTab();
	}
}
