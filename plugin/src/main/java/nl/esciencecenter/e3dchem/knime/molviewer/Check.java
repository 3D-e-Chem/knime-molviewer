package nl.esciencecenter.e3dchem.knime.molviewer;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.dynamic.js.v30.DynamicStatefulJSProcessor;

public abstract class Check extends DynamicStatefulJSProcessor {

	protected interface isCompatibleLambda {
		boolean test(DataColumnSpec s);
	}

	public Check() {
		super();
	}

	protected void hasCompatibleColumn(DataTableSpec spec, SettingsModelString setting,
			isCompatibleLambda isCompatible, String columnLabel, String specName) {
		int colIndex = -1;
		boolean settingIsDefault = setting.getStringValue() == "" || setting.getStringValue() == null
				|| setting.getStringValue().isEmpty();
		if (settingIsDefault) {
			for (int i = 0; i < spec.getNumColumns(); i++) {
				DataColumnSpec columnSpec = spec.getColumnSpec(i);
				if (isCompatible.test(columnSpec)) {
					// Select first column that matches
					colIndex = i;
					break;
				}
			}
			if (colIndex == -1) {
				throw new IllegalArgumentException("Column with " + columnLabel + " missing on port " + specName);
			}
		} else {
			colIndex = spec.findColumnIndex(setting.getStringValue());
			if (colIndex < 0) {
				throw new IllegalArgumentException(
						"Column '" + setting.getStringValue() + "' missing on port " + specName);
			}
			if (!isCompatible.test(spec.getColumnSpec(colIndex))) {
				throw new IllegalArgumentException("Column '" + setting.getStringValue()
						+ "' is incompatible, should be column with " + columnLabel + " on port " + specName);
			}
		}
	}

	protected void hasCompatibleColumnWithRowID(DataTableSpec spec, SettingsModelColumnName setting,
			isCompatibleLambda isCompatible, String columnLabel, String specName) {

		int colIndex = -1;
		boolean settingIsDefault = setting.getStringValue() == "" || setting.getStringValue() == null
				|| setting.getStringValue().isEmpty();
		boolean useRowId = setting.useRowID();
		if (settingIsDefault && !useRowId) {
			for (int i = 0; i < spec.getNumColumns(); i++) {
				DataColumnSpec columnSpec = spec.getColumnSpec(i);
				if (isCompatible.test(columnSpec)) {
					// Select first column that matches
					colIndex = i;
					break;
				}
			}
			if (colIndex == -1) {
				throw new IllegalArgumentException("Column with " + columnLabel + " missing on port " + specName);
			}
		} else {
			if (setting.useRowID()) {
				// Fine row id selected, always OK
			} else {
				colIndex = spec.findColumnIndex(setting.getStringValue());
				if (colIndex < 0) {
					throw new IllegalArgumentException(
							"Column '" + setting.getStringValue() + "' missing on port " + specName);
				}
				if (!isCompatible.test(spec.getColumnSpec(colIndex))) {
					throw new IllegalArgumentException("Column '" + setting.getStringValue()
							+ "' is incompatible, should be column with " + columnLabel + " on port " + specName);
				}
			}
		}
	}
	
	protected void hasCompatibleColumnOptional(DataTableSpec spec, SettingsModelString setting, isCompatibleLambda isCompatible, String columnLabel, String specName) {
		int colIndex = -1;
        boolean settingIsDefault = setting.getStringValue() == "" || setting.getStringValue() == null
                || setting.getStringValue().isEmpty();
        if (!settingIsDefault) {
            colIndex = spec.findColumnIndex(setting.getStringValue());
            if (colIndex < 0) {
                throw new IllegalArgumentException("Column '" + setting.getStringValue() + "' missing on port " + specName);
            }
            if (!isCompatible.test(spec.getColumnSpec(colIndex))) {
                throw new IllegalArgumentException("Column '" + setting.getStringValue()
                        + "' is incompatible, should be column with " + columnLabel + " on port " + specName);
            }
        }
	}
}