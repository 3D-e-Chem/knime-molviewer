package nl.esciencecenter.e3dchem.knime.molviewer;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "MolViewer" Node.
 *
 */
public class MolViewerFactory extends NodeFactory<MolViewerModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MolViewerModel createNodeModel() {
		return new MolViewerModel();
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
	public NodeView<MolViewerModel> createNodeView(final int viewIndex, final MolViewerModel nodeModel) {
		return new MolViewerView(nodeModel);
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
		return new MolViewerDialog();
	}

}
