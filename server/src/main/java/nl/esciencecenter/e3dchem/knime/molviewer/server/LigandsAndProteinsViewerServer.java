package nl.esciencecenter.e3dchem.knime.molviewer.server;

import java.util.List;

import org.glassfish.jersey.server.ResourceConfig;
import org.knime.core.node.property.hilite.HiLiteHandler;

import nl.esciencecenter.e3dchem.knime.molviewer.server.api.Molecule;
import nl.esciencecenter.e3dchem.knime.molviewer.server.resources.HiLiteResource;
import nl.esciencecenter.e3dchem.knime.molviewer.server.resources.LigandsHiLiteResource;
import nl.esciencecenter.e3dchem.knime.molviewer.server.resources.LigandsResource;
import nl.esciencecenter.e3dchem.knime.molviewer.server.resources.ProteinsResource;

public class LigandsAndProteinsViewerServer extends MolViewerServer {
	private LigandsResource ligands_res;
	private HiLiteResource ligand_hilite_res;
	private ProteinsResource proteins_res;

	public LigandsAndProteinsViewerServer(String page, int port) {
		super(page, port);
	}

	public LigandsAndProteinsViewerServer(String page) {
		super(page);
	}

	public void updateLigands(List<Molecule> ligands) {
		ligands_res.setMolecules(ligands);
	}

	public void updateProteins(List<Molecule> proteins) {
		proteins_res.setMolecules(proteins);
	}

	public void setLigandsHiLiteHandler(HiLiteHandler hiliteHandler) {
		ligand_hilite_res.setHiLiteHandler(hiliteHandler);
	}

	@Override
	protected void setupResources(ResourceConfig rc) {
		ligands_res = new LigandsResource();
		rc.register(ligands_res);
		ligand_hilite_res = new LigandsHiLiteResource();
		rc.register(ligand_hilite_res);
		proteins_res = new ProteinsResource();
		rc.register(proteins_res);
	}
}
