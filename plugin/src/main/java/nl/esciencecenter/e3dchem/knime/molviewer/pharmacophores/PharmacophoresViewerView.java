package nl.esciencecenter.e3dchem.knime.molviewer.pharmacophores;

import java.awt.event.ActionListener;

import org.knime.core.node.property.hilite.HiLiteHandler;
import org.knime.core.node.property.hilite.HiLiteListener;

import nl.esciencecenter.e3dchem.knime.molviewer.ViewerView;
import nl.esciencecenter.e3dchem.knime.molviewer.server.MolViewerServer;
import nl.esciencecenter.e3dchem.knime.molviewer.server.PharmacophoresViewerServer;

public class PharmacophoresViewerView extends ViewerView<PharmacophoresViewerModel>
		implements ActionListener, HiLiteListener {
	private HiLiteHandler hiLiteHandler = null;

	protected PharmacophoresViewerView(PharmacophoresViewerModel nodeModel) {
		super(nodeModel);
	}

	@Override
	public String getUrl() {
		return "#/pharmacophores";
	}

	@Override
	protected MolViewerServer setupServer() {
		return new PharmacophoresViewerServer(getUrl());
	}

	@Override
	protected void modelChanged() {
		// update internal hilite handler
		HiLiteHandler myhiliteHandler = getNodeModel().getInHiLiteHandler(PharmacophoresViewerModel.PORT);
		if (hiLiteHandler == null) {
			hiLiteHandler = myhiliteHandler;
			hiLiteHandler.addHiLiteListener(this);
			((PharmacophoresViewerServer) server).setHiLiteHandler(myhiliteHandler);
		} else {
			if (!myhiliteHandler.equals(hiLiteHandler)) {
				hiLiteHandler.removeHiLiteListener(this);
				hiLiteHandler = myhiliteHandler;
				hiLiteHandler.addHiLiteListener(this);
				((PharmacophoresViewerServer) server).setHiLiteHandler(myhiliteHandler);
			}
		}
		// retrieve the new model from your nodemodel and
		// update the view.
		// be aware of a possibly not executed nodeModel! The data you retrieve
		// from your nodemodel could be null, emtpy, or invalid in any kind.
		((PharmacophoresViewerServer) server).updatePharmacophores(getNodeModel().getPharmacophores());
		super.modelChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClose() {
		super.onClose();

		if (hiLiteHandler != null) {
			hiLiteHandler.removeHiLiteListener(this);
			hiLiteHandler = null;
		}
	}

}
