package nl.esciencecenter.e3dchem.knime.molviewer.ligandsandproteins;

import org.knime.bio.types.PdbValue;
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
public class LigandsAndProteinsViewerDialog extends ViewerDialog {

	@SuppressWarnings("unchecked")
	protected LigandsAndProteinsViewerDialog() {
		super();

		createNewGroup("Ligands");

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelString(LigandsAndProteinsViewerModel.CFGKEY_LIGAND, null),
				"Column with SDF or Mol2 formatted molecules", LigandsAndProteinsViewerModel.LIGAND_PORT, SdfValue.class,
				Mol2Value.class));

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelColumnName(LigandsAndProteinsViewerModel.CFGKEY_LIGAND_LABEL, null),
				"Column with labels", LigandsAndProteinsViewerModel.LIGAND_PORT, StringValue.class));

		createNewGroup("Proteins");

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelString(LigandsAndProteinsViewerModel.CFGKEY_PROTEIN, null),
				"Column with PDB or Mol2 formatted molecules", LigandsAndProteinsViewerModel.PROTEIN_PORT, PdbValue.class,
				Mol2Value.class));

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelColumnName(LigandsAndProteinsViewerModel.CFGKEY_PROTEIN_LABEL, null),
				"Column with labels", LigandsAndProteinsViewerModel.PROTEIN_PORT, StringValue.class));

		closeCurrentGroup();

		advancedTab();
	}
}
