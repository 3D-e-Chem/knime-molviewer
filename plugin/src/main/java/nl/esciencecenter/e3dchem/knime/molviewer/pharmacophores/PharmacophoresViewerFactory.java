package nl.esciencecenter.e3dchem.knime.molviewer.pharmacophores;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class PharmacophoresViewerFactory extends NodeFactory<PharmacophoresViewerModel> {

	@Override
	public PharmacophoresViewerModel createNodeModel() {
		return new PharmacophoresViewerModel();
	}

	@Override
	protected int getNrNodeViews() {
		return 1;
	}

	@Override
	public NodeView<PharmacophoresViewerModel> createNodeView(int viewIndex, PharmacophoresViewerModel nodeModel) {
		return new PharmacophoresViewerView(nodeModel);
	}

	@Override
	protected boolean hasDialog() {
		return true;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return new PharmacophoresViewerDialog();
	}

}
