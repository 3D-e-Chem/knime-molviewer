package nl.esciencecenter.e3dchem.knime.molviewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import nl.esciencecenter.e3dchem.knime.molviewer.ViewerModel.isCompatibleLambda;
import nl.esciencecenter.e3dchem.knime.molviewer.ligandsandproteins.LigandsAndProteinsViewerModel;

public class ViewerModelTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test()
	public void configureColumn_noColum_invalidsettingexception() throws InvalidSettingsException {
		thrown.expect(InvalidSettingsException.class);
		thrown.expectMessage("Column with label missing on port Ligands");

		LigandsAndProteinsViewerModel model = new LigandsAndProteinsViewerModel();
		DataTableSpec spec = new DataTableSpec();
		SettingsModelString setting = new SettingsModelString("MySetting", "");
		isCompatibleLambda isCompatible = (DataColumnSpec s) -> s.getType().isCompatible(StringValue.class);
		String columnLabel = "label";
		String specName = "Ligands";

		model.configureColumn(spec, setting, isCompatible, columnLabel, specName);
	}

	@Test()
	public void configureColumn_singleStringColum_autoselect() throws InvalidSettingsException {
		LigandsAndProteinsViewerModel model = new LigandsAndProteinsViewerModel();
		DataTableSpec spec = new DataTableSpec(
				new DataColumnSpecCreator("mylabelcolumn", StringCell.TYPE).createSpec()
		);
		SettingsModelString setting = new SettingsModelString("MySetting", "");
		isCompatibleLambda isCompatible = (DataColumnSpec s) -> s.getType().isCompatible(StringValue.class);
		String columnLabel = "label";
		String specName = "Ligands";

		model.configureColumn(spec, setting, isCompatible, columnLabel, specName);
		
		assertEquals("mylabelcolumn", setting.getStringValue());
	}
	
	@Test()
	public void configureColumn_preSelectedAndnoColum_missingcolexception() throws InvalidSettingsException {
		thrown.expect(InvalidSettingsException.class);
		thrown.expectMessage("Column 'mylabelcolumn' missing on port Ligands");

		LigandsAndProteinsViewerModel model = new LigandsAndProteinsViewerModel();
		DataTableSpec spec = new DataTableSpec();
		SettingsModelString setting = new SettingsModelString("MySetting", "mylabelcolumn");
		isCompatibleLambda isCompatible = (DataColumnSpec s) -> s.getType().isCompatible(StringValue.class);
		String columnLabel = "label";
		String specName = "Ligands";

		model.configureColumn(spec, setting, isCompatible, columnLabel, specName);
	}

	@Test()
	public void configureColumn_preSelectedAndWrongColum_mismatchcolexception() throws InvalidSettingsException {
		thrown.expect(InvalidSettingsException.class);
		thrown.expectMessage("Column 'mylabelcolumn' is incompatible, should be column with label on port Ligands");

		LigandsAndProteinsViewerModel model = new LigandsAndProteinsViewerModel();
		DataTableSpec spec = new DataTableSpec(
				new DataColumnSpecCreator("mylabelcolumn", BooleanCell.TYPE).createSpec()
		);
		SettingsModelString setting = new SettingsModelString("MySetting", "mylabelcolumn");
		isCompatibleLambda isCompatible = (DataColumnSpec s) -> s.getType().isCompatible(StringValue.class);
		String columnLabel = "label";
		String specName = "Ligands";

		model.configureColumn(spec, setting, isCompatible, columnLabel, specName);
	}

	@Test()
	public void configureColumnWithRowID_noColum_selectsrowid() throws InvalidSettingsException {
		LigandsAndProteinsViewerModel model = new LigandsAndProteinsViewerModel();
		DataTableSpec spec = new DataTableSpec();
		SettingsModelColumnName setting = new SettingsModelColumnName("MySetting", "");
		isCompatibleLambda isCompatible = (DataColumnSpec s) -> s.getType().isCompatible(StringValue.class);
		String columnLabel = "label";
		String specName = "Ligands";

		model.configureColumnWithRowID(spec, setting, isCompatible, columnLabel, specName);
		
		assertTrue(setting.useRowID());
	}
	
	@Test()
	public void configureColumnWithRowID_singleStringColum_autoselect() throws InvalidSettingsException {
		LigandsAndProteinsViewerModel model = new LigandsAndProteinsViewerModel();
		DataTableSpec spec = new DataTableSpec(
				new DataColumnSpecCreator("mylabelcolumn", StringCell.TYPE).createSpec()
		);
		SettingsModelColumnName setting = new SettingsModelColumnName("MySetting", "");
		isCompatibleLambda isCompatible = (DataColumnSpec s) -> s.getType().isCompatible(StringValue.class);
		String columnLabel = "label";
		String specName = "Ligands";

		model.configureColumnWithRowID(spec, setting, isCompatible, columnLabel, specName);
		
		assertEquals("mylabelcolumn", setting.getStringValue());
	}
	
	@Test()
	public void configureColumnWithRowID_preSelectedAndnoColum_missingcolexception() throws InvalidSettingsException {
		thrown.expect(InvalidSettingsException.class);
		thrown.expectMessage("Column 'mylabelcolumn' missing on port Ligands");

		LigandsAndProteinsViewerModel model = new LigandsAndProteinsViewerModel();
		DataTableSpec spec = new DataTableSpec();
		SettingsModelColumnName setting = new SettingsModelColumnName("MySetting", "mylabelcolumn");
		isCompatibleLambda isCompatible = (DataColumnSpec s) -> s.getType().isCompatible(StringValue.class);
		String columnLabel = "label";
		String specName = "Ligands";

		model.configureColumnWithRowID(spec, setting, isCompatible, columnLabel, specName);
	}

	@Test()
	public void configureColumnWithRowID_preSelectedAndWrongColum_mismatchcolexception() throws InvalidSettingsException {
		thrown.expect(InvalidSettingsException.class);
		thrown.expectMessage("Column 'mylabelcolumn' is incompatible, should be column with label on port Ligands");

		LigandsAndProteinsViewerModel model = new LigandsAndProteinsViewerModel();
		DataTableSpec spec = new DataTableSpec(
				new DataColumnSpecCreator("mylabelcolumn", BooleanCell.TYPE).createSpec()
		);
		SettingsModelColumnName setting = new SettingsModelColumnName("MySetting", "mylabelcolumn");
		isCompatibleLambda isCompatible = (DataColumnSpec s) -> s.getType().isCompatible(StringValue.class);
		String columnLabel = "label";
		String specName = "Ligands";

		model.configureColumnWithRowID(spec, setting, isCompatible, columnLabel, specName);
	}

	
	@Test()
	public void configureColumnOptional_preSelectedAndnoColum_missingcolexception() throws InvalidSettingsException {
		thrown.expect(InvalidSettingsException.class);
		thrown.expectMessage("Column 'mylabelcolumn' missing on port Ligands");

		LigandsAndProteinsViewerModel model = new LigandsAndProteinsViewerModel();
		DataTableSpec spec = new DataTableSpec();
		SettingsModelString setting = new SettingsModelString("MySetting", "mylabelcolumn");
		isCompatibleLambda isCompatible = (DataColumnSpec s) -> s.getType().isCompatible(StringValue.class);
		String columnLabel = "label";
		String specName = "Ligands";

		model.configureColumnOptional(spec, setting, isCompatible, columnLabel, specName);
	}

	@Test()
	public void configureColumnOptional_preSelectedAndWrongColum_mismatchcolexception() throws InvalidSettingsException {
		thrown.expect(InvalidSettingsException.class);
		thrown.expectMessage("Column 'mylabelcolumn' is incompatible, should be column with label on port Ligands");

		LigandsAndProteinsViewerModel model = new LigandsAndProteinsViewerModel();
		DataTableSpec spec = new DataTableSpec(
				new DataColumnSpecCreator("mylabelcolumn", BooleanCell.TYPE).createSpec()
		);
		SettingsModelString setting = new SettingsModelString("MySetting", "mylabelcolumn");
		isCompatibleLambda isCompatible = (DataColumnSpec s) -> s.getType().isCompatible(StringValue.class);
		String columnLabel = "label";
		String specName = "Ligands";

		model.configureColumnOptional(spec, setting, isCompatible, columnLabel, specName);
	}
}
