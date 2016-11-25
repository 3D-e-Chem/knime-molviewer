package nl.esciencecenter.e3dchem.knime.molviewer.ligandsandproteins;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "MolViewer" Node.
 *
 */
public class LigandsAndProteinsViewerFactory extends NodeFactory<LigandsAndProteinsViewerModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LigandsAndProteinsViewerModel createNodeModel() {
		return new LigandsAndProteinsViewerModel();
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
	public NodeView<LigandsAndProteinsViewerModel> createNodeView(final int viewIndex,
			final LigandsAndProteinsViewerModel nodeModel) {
		return new LigandsAndProteinsViewerView(nodeModel);
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
		return new LigandsAndProteinsViewerDialog();
	}

}
