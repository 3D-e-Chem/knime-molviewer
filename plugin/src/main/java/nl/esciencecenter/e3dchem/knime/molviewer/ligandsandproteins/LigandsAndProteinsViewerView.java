package nl.esciencecenter.e3dchem.knime.molviewer.ligandsandproteins;

import java.awt.event.ActionListener;

import org.knime.core.node.property.hilite.HiLiteHandler;
import org.knime.core.node.property.hilite.HiLiteListener;

import nl.esciencecenter.e3dchem.knime.molviewer.ViewerView;
import nl.esciencecenter.e3dchem.knime.molviewer.server.LigandsAndProteinsViewerServer;
import nl.esciencecenter.e3dchem.knime.molviewer.server.MolViewerServer;

/**
 * <code>NodeView</code> for the "MolViewer" Node.
 *
 */
public class LigandsAndProteinsViewerView extends ViewerView<LigandsAndProteinsViewerModel>
		implements ActionListener, HiLiteListener {
	private HiLiteHandler ligandsHiLiteHandler = null;

	/**
	 * Creates a new view.
	 *
	 * @param nodeModel
	 *            The model (class: {@link LigandsAndProteinsViewerModel})
	 */
	public LigandsAndProteinsViewerView(final LigandsAndProteinsViewerModel nodeModel) {
		super(nodeModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {
		// update internal hilite handler
		HiLiteHandler hiliteHandler = getNodeModel().getInHiLiteHandler(LigandsAndProteinsViewerModel.LIGAND_PORT);
		if (ligandsHiLiteHandler == null) {
			ligandsHiLiteHandler = hiliteHandler;
			ligandsHiLiteHandler.addHiLiteListener(this);
			((LigandsAndProteinsViewerServer) server).setLigandsHiLiteHandler(hiliteHandler);
		} else {
			if (hiliteHandler != ligandsHiLiteHandler) {
				ligandsHiLiteHandler.removeHiLiteListener(this);
				ligandsHiLiteHandler = hiliteHandler;
				ligandsHiLiteHandler.addHiLiteListener(this);
				((LigandsAndProteinsViewerServer) server).setLigandsHiLiteHandler(hiliteHandler);
			}
		}

		// retrieve the new model from your nodemodel and
		// update the view.
		// be aware of a possibly not executed nodeModel! The data you retrieve
		// from your nodemodel could be null, emtpy, or invalid in any kind.
		((LigandsAndProteinsViewerServer) server).updateLigands(getNodeModel().getLigands());
		((LigandsAndProteinsViewerServer) server).updateProteins(getNodeModel().getProteins());
		super.modelChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClose() {
		super.onClose();

		if (ligandsHiLiteHandler != null) {
			ligandsHiLiteHandler.removeHiLiteListener(this);
			ligandsHiLiteHandler = null;
		}
	}

	@Override
	protected MolViewerServer setupServer() {
		return new LigandsAndProteinsViewerServer(getUrl());
	}

	@Override
	public String getUrl() {
		// TODO remove # in url, by letting Jetty return index.html on 404
		return "#/ligands-and-proteins";
	}
}
