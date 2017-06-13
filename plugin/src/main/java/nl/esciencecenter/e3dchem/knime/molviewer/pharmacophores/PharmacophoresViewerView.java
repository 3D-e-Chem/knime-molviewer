package nl.esciencecenter.e3dchem.knime.molviewer.pharmacophores;

import nl.esciencecenter.e3dchem.knime.molviewer.ViewerView;
import nl.esciencecenter.e3dchem.knime.molviewer.server.MolViewerServer;

public class PharmacophoresViewerView extends ViewerView<PharmacophoresViewerModel> {

	protected PharmacophoresViewerView(PharmacophoresViewerModel nodeModel) {
		super(nodeModel);
	}

	@Override
	public String getUrl() {
		return "#/pharmacophores";
	}

	@Override
	protected MolViewerServer setupServer() {
		// TODO Auto-generated method stub
		return null;
	}

}
