package nl.esciencecenter.e3dchem.knime.molviewer.proteins;

import org.knime.bio.types.PdbValue;
import org.knime.chem.types.Mol2Value;
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
public class ProteinsViewerDialog extends ViewerDialog {

	@SuppressWarnings("unchecked")
	protected ProteinsViewerDialog() {
		super();

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelString(ProteinsViewerModel.CFGKEY_PROTEIN, null),
				"Column with PDB or Mol2 formatted molecules", ProteinsViewerModel.PROTEIN_PORT, PdbValue.class,
				Mol2Value.class));

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelColumnName(ProteinsViewerModel.CFGKEY_PROTEIN_LABEL, null),
				"Column with labels", ProteinsViewerModel.PROTEIN_PORT, StringValue.class));

		advancedTab();
	}
}
