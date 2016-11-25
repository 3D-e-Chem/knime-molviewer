package nl.esciencecenter.e3dchem.knime.molviewer;

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
import org.knime.core.node.property.hilite.HiLiteListener;

import nl.esciencecenter.e3dchem.knime.molviewer.server.MolViewerServer;

public abstract class ViewerView<T extends ViewerModel> extends NodeView<T> implements ActionListener, HiLiteListener {
	protected static final NodeLogger logger = NodeLogger.getLogger(ViewerView.class);
	protected JTextField current_uri_field;
	protected MolViewerServer server;

	protected ViewerView(T nodeModel) {
		super(nodeModel);
		setupPanel();
		server = setupServer();
	}

	abstract public String getUrl();

	abstract protected MolViewerServer setupServer();

	protected void setupPanel() {
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
	public void onOpen() {
		try {
			server.start();
			current_uri_field.setText(server.getCurrentUri().toString());
			if (this.getNodeModel().isBrowserAutoOpen()) {
				openBrowser();
			}
		} catch (Exception e) {
			logger.warn(e);
			logger.warn(e.getCause());
			e.printStackTrace();
		}
	}

	protected void openBrowser() {
		try {
			// TODO test if browse() is supported
			Desktop.getDesktop().browse(server.getCurrentUri());
		} catch (IOException e) {
			logger.warn(e);
			logger.warn(e.getCause());
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		if ("open".equals(e.getActionCommand())) {
			openBrowser();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {
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
			logger.warn(e);
			logger.warn(e.getCause());
			e.printStackTrace();
		}
	}

	@Override
	public void hiLite(org.knime.core.node.property.hilite.KeyEvent event) {
		server.sendMessage("hiLite");
	}

	@Override
	public void unHiLite(org.knime.core.node.property.hilite.KeyEvent event) {
		server.sendMessage("unHiLite");
	}

	@Override
	public void unHiLiteAll(org.knime.core.node.property.hilite.KeyEvent event) {
		server.sendMessage("unHiLiteAll");
	}

}
