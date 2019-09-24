package nl.esciencecenter.e3dchem.knime.molviewer.ligands;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

import nl.esciencecenter.e3dchem.knime.molviewer.ViewerModel;

@Deprecated
public class LigandsViewerFactory extends NodeFactory<ViewerModel> {

	public LigandsViewerFactory() {
	}

	public LigandsViewerFactory(boolean lazyInitialization) {
		super(lazyInitialization);
	}

	@Override
	public ViewerModel createNodeModel() {
		return new ViewerModel(1, 0);
	}

	@Override
	protected int getNrNodeViews() {
		return 0;
	}

	@Override
	public NodeView createNodeView(int viewIndex, ViewerModel nodeModel) {
		return null;
	}

	@Override
	protected boolean hasDialog() {
		return false;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return null;
	}
}
