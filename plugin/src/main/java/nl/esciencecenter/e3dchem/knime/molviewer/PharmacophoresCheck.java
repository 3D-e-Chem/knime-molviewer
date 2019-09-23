package nl.esciencecenter.e3dchem.knime.molviewer;

import org.knime.bio.types.PdbValue;
import org.knime.chem.types.Mol2Value;
import org.knime.chem.types.SdfValue;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.data.vector.doublevector.DoubleVectorValue;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.port.PortObject;
import org.knime.dynamic.js.v30.DynamicJSConfig;

import nl.esciencecenter.e3dchem.knime.pharmacophore.PharValue;

public class PharmacophoresCheck extends Check {

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
		isCompatibleLambda compatiblePhar = (DataColumnSpec s) -> s.getType().isCompatible(PharValue.class);
		isCompatibleLambda compatibleTransform = (DataColumnSpec s) -> s.getType()
				.isCompatible(DoubleVectorValue.class);

		BufferedDataTable table = (BufferedDataTable) inObjects[0];
		DataTableSpec spec = table.getSpec();

		hasCompatibleColumn(spec, (SettingsModelColumnName) config.getModel("pharmacophores"), compatiblePhar,
				"molecules", "pharmacophores");
		hasCompatibleColumnWithRowID(spec, (SettingsModelColumnName) config.getModel("labels"), compatibleLabel,
				"labels", "pharmacophores");
		hasCompatibleColumnOptional(spec, (SettingsModelColumnName) config.getModel("ligands"), compatibleLigand,
				"ligands", "pharmacophores");
		hasCompatibleColumnOptional(spec, (SettingsModelColumnName) config.getModel("proteins"), compatibleProtein,
				"proteins", "pharmacophores");
		hasCompatibleColumnOptional(spec, (SettingsModelColumnName) config.getModel("transform"), compatibleTransform,
				"transform", "pharmacophores");

		return inObjects;
	}

}
