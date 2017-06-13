package nl.esciencecenter.e3dchem.knime.molviewer.pharmacophores;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;

import nl.esciencecenter.e3dchem.knime.molviewer.ViewerModel;
import nl.esciencecenter.e3dchem.knime.molviewer.server.api.Pharmacophore;

public class PharmacophoresViewerModel extends ViewerModel {
	private List<Pharmacophore> pharmacophores;
	
	protected PharmacophoresViewerModel() {
		super(1, 0);
	}

	@Override
	protected BufferedDataTable[] execute(BufferedDataTable[] inData, ExecutionContext exec) throws Exception {
		setPharmacophores(inData);
		return new BufferedDataTable[] {};
	}

	private void setPharmacophores(BufferedDataTable[] inData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
		// TODO Auto-generated method stub
		return new DataTableSpec[] {};
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void reset() {
		pharmacophores = null;
	}

}
