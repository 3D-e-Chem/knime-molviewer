package nl.esciencecenter.e3dchem.knime.molviewer.ligandsandproteins;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeView;
import org.knime.core.node.property.hilite.HiLiteHandler;
import org.knime.core.node.property.hilite.HiLiteListener;

import nl.esciencecenter.e3dchem.knime.molviewer.server.MolViewerServer;

/**
 * <code>NodeView</code> for the "MolViewer" Node.
 *
 */
public class LigandsAndProteinsViewerView extends NodeView<LigandsAndProteinsViewerModel> implements ActionListener, HiLiteListener {
	private static final NodeLogger logger = NodeLogger.getLogger(LigandsAndProteinsViewerView.class);
	private JTextField current_uri_field;
	private MolViewerServer server;
	private HiLiteHandler ligandsHiLiteHandler = null;

	/**
	 * Creates a new view.
	 *
	 * @param nodeModel
	 *            The model (class: {@link LigandsAndProteinsViewerModel})
	 */
	public LigandsAndProteinsViewerView(final LigandsAndProteinsViewerModel nodeModel) {
		super(nodeModel);
		setupPanel();
		server = new MolViewerServer("index.html");
	}

	private void setupPanel() {
		JPanel m_panel = new JPanel();
		m_panel.setLayout(new BoxLayout(m_panel, BoxLayout.Y_AXIS));
		setComponent(m_panel);

		JLabel help = new JLabel("If web browser has not opened press button below");
		m_panel.add(help);

		JButton openBrowser = new JButton("Open web browser");
		openBrowser.addActionListener(this);
		openBrowser.setActionCommand("open");
		openBrowser.setMnemonic(KeyEvent.VK_ENTER);
		m_panel.add(openBrowser);

		current_uri_field = new JTextField(40);

		current_uri_field.setEditable(false);
		m_panel.add(current_uri_field);
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
			server.setLigandsHiLiteHandler(hiliteHandler);
		} else {
			if (hiliteHandler != ligandsHiLiteHandler) {
				ligandsHiLiteHandler.removeHiLiteListener(this);
				ligandsHiLiteHandler = hiliteHandler;
				ligandsHiLiteHandler.addHiLiteListener(this);
				server.setLigandsHiLiteHandler(hiliteHandler);
			}
		}

		// retrieve the new model from your nodemodel and
		// update the view.
		// be aware of a possibly not executed nodeModel! The data you retrieve
		// from your nodemodel could be null, emtpy, or invalid in any kind.
		server.updateLigands(getNodeModel().getLigands());
		server.updateProteins(getNodeModel().getProteins());
		server.sendMessage("modelChanged");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClose() {
		try {
			server.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (ligandsHiLiteHandler != null) {
			ligandsHiLiteHandler.removeHiLiteListener(this);
			ligandsHiLiteHandler = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onOpen() {
		try {
			server.start();
			current_uri_field.setText(server.getCurrentUri().toString());
			if (this.getNodeModel().isBrowserAutoOpen()) {
				openBrowser();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn(e);
			logger.warn(e.getCause());
			e.printStackTrace();
		}
	}

	protected void openBrowser() {
		try {
			// TODO test if browse() is supported
			Desktop.getDesktop().browse(server.getCurrentUri());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		if ("open".equals(e.getActionCommand())) {
			openBrowser();
		}
	}

	@Override
	public void hiLite(org.knime.core.node.property.hilite.KeyEvent event) {
		// TODO select hiliteKeys in webinterface
		server.sendMessage("hiLite");
	}

	@Override
	public void unHiLite(org.knime.core.node.property.hilite.KeyEvent event) {
		// TODO unselect hiliteKeys in webinterface
		server.sendMessage("unHiLite");
	}

	@Override
	public void unHiLiteAll(org.knime.core.node.property.hilite.KeyEvent event) {
		// TODO clear selection in webinterface
		server.sendMessage("unHiLiteAll");
	}
}
