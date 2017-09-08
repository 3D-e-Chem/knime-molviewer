package nl.esciencecenter.e3dchem.knime.molviewer.ligands;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "MolViewer" Node.
 *
 */
public class LigandsViewerFactory extends NodeFactory<LigandsViewerModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LigandsViewerModel createNodeModel() {
		return new LigandsViewerModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNrNodeViews() {
		return 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeView<LigandsViewerModel> createNodeView(final int viewIndex,
			final LigandsViewerModel nodeModel) {
		return new LigandsViewerView(nodeModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasDialog() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeDialogPane createNodeDialogPane() {
		return new LigandsViewerDialog();
	}

}
