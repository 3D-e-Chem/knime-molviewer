package nl.esciencecenter.e3dchem.knime.molviewer;

import org.knime.bio.types.PdbValue;
import org.knime.chem.types.Mol2Value;
import org.knime.chem.types.SdfValue;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.dynamic.js.v30.DynamicJSConfig;

public class LigandsAndProteinsCheck extends Check {

	@Override
	public Object[] processInputObjects(PortObject[] inObjects, ExecutionContext exec, DynamicJSConfig config)
			throws Exception {
		isCompatibleLambda compatibleLigand = (DataColumnSpec s) -> s.getType().isCompatible(SdfValue.class)
				|| s.getType().isCompatible(Mol2Value.class);
		isCompatibleLambda compatibleProtein = (DataColumnSpec s) -> s.getType().isCompatible(PdbValue.class)
				|| s.getType().isCompatible(Mol2Value.class);
		// as PDB, SDF and Mol2 are also string compatible exclude them for
		// label
		isCompatibleLambda compatibleLabel = (DataColumnSpec s) -> s.getType().isCompatible(StringValue.class)
				&& !(compatibleLigand.test(s) || compatibleProtein.test(s));

		BufferedDataTable ligandsTable = (BufferedDataTable) inObjects[0];
		DataTableSpec ligandsSpec = ligandsTable.getSpec();
		hasCompatibleColumn(ligandsSpec, (SettingsModelString) config.getModel("ligands"), compatibleLigand, "molecules",
				"ligands");
		hasCompatibleColumnWithRowID(ligandsSpec, (SettingsModelColumnName) config.getModel("liglabels"), compatibleLabel,
				"labels", "ligands");
		
		BufferedDataTable protTable = (BufferedDataTable) inObjects[1];
		DataTableSpec protSpec = protTable.getSpec();
		hasCompatibleColumn(protSpec, (SettingsModelString) config.getModel("pdbs"), compatibleProtein, "molecules",
				"proteins");
		hasCompatibleColumnWithRowID(protSpec, (SettingsModelColumnName) config.getModel("protlabels"), compatibleLabel,
				"labels", "proteins");
		
		return inObjects;
	}

}
