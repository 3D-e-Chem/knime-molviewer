package nl.esciencecenter.e3dchem.knime.molviewer.proteins;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "MolViewer" Node.
 *
 */
public class ProteinsViewerFactory extends NodeFactory<ProteinsViewerModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProteinsViewerModel createNodeModel() {
		return new ProteinsViewerModel();
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
	public NodeView<ProteinsViewerModel> createNodeView(final int viewIndex,
			final ProteinsViewerModel nodeModel) {
		return new ProteinsViewerView(nodeModel);
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
		return new ProteinsViewerDialog();
	}

}
