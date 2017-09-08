package nl.esciencecenter.e3dchem.knime.molviewer.proteins;

import java.awt.event.ActionListener;

import org.knime.core.node.property.hilite.HiLiteHandler;
import org.knime.core.node.property.hilite.HiLiteListener;

import nl.esciencecenter.e3dchem.knime.molviewer.ViewerView;
import nl.esciencecenter.e3dchem.knime.molviewer.server.MolViewerServer;
import nl.esciencecenter.e3dchem.knime.molviewer.server.ProteinsViewerServer;

/**
 * <code>NodeView</code> for the "MolViewer" Node.
 *
 */
public class ProteinsViewerView extends ViewerView<ProteinsViewerModel>
		implements ActionListener, HiLiteListener {
	private HiLiteHandler proteinsHiLiteHandler = null;

	/**
	 * Creates a new view.
	 *
	 * @param nodeModel
	 *            The model (class: {@link ProteinsViewerModel})
	 */
	public ProteinsViewerView(final ProteinsViewerModel nodeModel) {
		super(nodeModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {
		// update internal hilite handler
		HiLiteHandler hiliteHandler = getNodeModel().getInHiLiteHandler(ProteinsViewerModel.PROTEIN_PORT);
		if (proteinsHiLiteHandler == null) {
			proteinsHiLiteHandler = hiliteHandler;
			proteinsHiLiteHandler.addHiLiteListener(this);
			((ProteinsViewerServer) server).setProteinsHiLiteHandler(hiliteHandler);
		} else {
			if (!hiliteHandler.equals(proteinsHiLiteHandler)) {
				proteinsHiLiteHandler.removeHiLiteListener(this);
				proteinsHiLiteHandler = hiliteHandler;
				proteinsHiLiteHandler.addHiLiteListener(this);
				((ProteinsViewerServer) server).setProteinsHiLiteHandler(hiliteHandler);
			}
		}

		// retrieve the new model from your nodemodel and
		// update the view.
		// be aware of a possibly not executed nodeModel! The data you retrieve
		// from your nodemodel could be null, emtpy, or invalid in any kind.
		((ProteinsViewerServer) server).updateProteins(getNodeModel().getProteins());
		super.modelChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClose() {
		super.onClose();

		if (proteinsHiLiteHandler != null) {
			proteinsHiLiteHandler.removeHiLiteListener(this);
			proteinsHiLiteHandler = null;
		}
	}

	@Override
	protected MolViewerServer setupServer() {
		return new ProteinsViewerServer(getUrl());
	}

	@Override
	public String getUrl() {
		// TODO remove # in url, by letting Jetty return index.html on 404
		return "#/proteins";
	}
}
