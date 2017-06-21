package nl.esciencecenter.e3dchem.knime.molviewer.pharmacophores;

import org.knime.bio.types.PdbValue;
import org.knime.chem.types.Mol2Value;
import org.knime.chem.types.SdfValue;
import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import nl.esciencecenter.e3dchem.knime.molviewer.ViewerDialog;

public class PharmacophoresViewerDialog extends ViewerDialog {

	@SuppressWarnings("unchecked")
	public PharmacophoresViewerDialog() {
		super();

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelColumnName(PharmacophoresViewerModel.CFGKEY_LABEL, null), "Column with labels",
				PharmacophoresViewerModel.PORT, true, StringValue.class));

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelString(PharmacophoresViewerModel.CFGKEY_PHARMACOPHORE, null),
				"Column with PHAR formatted pharmacophores", PharmacophoresViewerModel.PORT, true, StringValue.class));

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelString(PharmacophoresViewerModel.CFGKEY_LIGAND, null),
				"Column with SDF or Mol2 formatted small molecules (ligands)", PharmacophoresViewerModel.PORT, false, true, SdfValue.class,
				Mol2Value.class));

		addDialogComponent(new DialogComponentColumnNameSelection(
				new SettingsModelString(PharmacophoresViewerModel.CFGKEY_PROTEIN, null),
				"Column with PDB or Mol2 formatted big molecules (proteins)", PharmacophoresViewerModel.PORT, false, true, PdbValue.class,
				Mol2Value.class));

		advancedTab();
	}
}
