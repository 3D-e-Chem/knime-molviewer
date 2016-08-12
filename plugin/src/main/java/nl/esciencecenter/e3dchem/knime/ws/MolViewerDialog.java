package nl.esciencecenter.e3dchem.knime.ws;

import org.knime.chem.types.SdfValue;
import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
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

		addDialogComponent(
				new DialogComponentNumber(
						new SettingsModelIntegerBounded(MolViewerModel.CFGKEY_COUNT, MolViewerModel.DEFAULT_COUNT,
								Integer.MIN_VALUE, Integer.MAX_VALUE),
						"Counter:", /* step */ 1, /* componentwidth */ 5));

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelString(MolViewerModel.CFGKEY_LIGAND, null),
				"Column with SDF molecules", MolViewerModel.LIGAND_PORT, SdfValue.class));
	}
}
