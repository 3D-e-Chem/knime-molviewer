package nl.esciencecenter.e3dchem.knime.molviewer;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;

import nl.esciencecenter.e3dchem.knime.molviewer.ligandsandproteins.LigandsAndProteinsViewerModel;

public class ViewerDialog extends DefaultNodeSettingsPane {

	protected void advancedTab() {
		createNewTab("Advanced");

		addDialogComponent(new DialogComponentBoolean(
				new SettingsModelBoolean(LigandsAndProteinsViewerModel.CFG_BROWSERAUTOOPEN, true),
				"Automatically open web browser when view is opened"));
	}

}
