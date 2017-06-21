package nl.esciencecenter.e3dchem.knime.molviewer.server;

import java.util.List;

import org.glassfish.jersey.server.ResourceConfig;
import org.knime.core.node.property.hilite.HiLiteHandler;

import nl.esciencecenter.e3dchem.knime.molviewer.server.api.Pharmacophore;
import nl.esciencecenter.e3dchem.knime.molviewer.server.resources.PharmacophoresHiLiteResource;
import nl.esciencecenter.e3dchem.knime.molviewer.server.resources.PharmacophoresResource;

public class PharmacophoresViewerServer extends MolViewerServer {
	private PharmacophoresResource pharmacophoresRes;
	private PharmacophoresHiLiteResource hiliteRes;

	public PharmacophoresViewerServer(String page, int port) {
		super(page, port);
	}

	public PharmacophoresViewerServer(String page) {
		super(page);
	}

	public void updatePharmacophores(List<Pharmacophore> pharmacophores) {
		pharmacophoresRes.setPharmacophores(pharmacophores);
	}
	
	public void setHiLiteHandler(HiLiteHandler hiliteHandler) {
		hiliteRes.setHiLiteHandler(hiliteHandler);
	}
	
	@Override
	protected void setupResources(ResourceConfig rc) {
		pharmacophoresRes = new PharmacophoresResource();
		rc.register(pharmacophoresRes);
		hiliteRes = new PharmacophoresHiLiteResource();
		rc.register(hiliteRes);
	}

}
