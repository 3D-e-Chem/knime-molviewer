package nl.esciencecenter.e3dchem.knime.molviewer.server;

import java.util.List;

import org.glassfish.jersey.server.ResourceConfig;
import org.knime.core.node.property.hilite.HiLiteHandler;

import nl.esciencecenter.e3dchem.knime.molviewer.server.api.Molecule;
import nl.esciencecenter.e3dchem.knime.molviewer.server.resources.ProteinsHiLiteResource;
import nl.esciencecenter.e3dchem.knime.molviewer.server.resources.ProteinsResource;

public class ProteinsViewerServer extends MolViewerServer {
	private ProteinsHiLiteResource proteins_hilite_res;
	private ProteinsResource proteins_res;

	public ProteinsViewerServer(String page, int port) {
		super(page, port);
	}

	public ProteinsViewerServer(String page) {
		super(page);
	}

	public void updateProteins(List<Molecule> proteins) {
		proteins_res.setMolecules(proteins);
	}

	public void setProteinsHiLiteHandler(HiLiteHandler hiliteHandler) {
		proteins_hilite_res.setHiLiteHandler(hiliteHandler);
	}
	
	@Override
	protected void setupResources(ResourceConfig rc) {
		proteins_hilite_res = new ProteinsHiLiteResource();
		rc.register(proteins_hilite_res);
		proteins_res = new ProteinsResource();
		rc.register(proteins_res);
	}
}
