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
	protected JTextField currentUriField;
	protected MolViewerServer server;

	protected ViewerView(T nodeModel) {
		super(nodeModel);
		setupPanel();
		server = setupServer();
	}

	public abstract String getUrl();

	protected abstract MolViewerServer setupServer();

	protected void setupPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		setComponent(panel);

		JLabel help = new JLabel("If web browser has not opened press button below");
		panel.add(help);

		JButton openBrowser = new JButton("Open web browser");
		openBrowser.addActionListener(this);
		openBrowser.setActionCommand("open");
		openBrowser.setMnemonic(KeyEvent.VK_ENTER);
		panel.add(openBrowser);

		currentUriField = new JTextField(40);

		currentUriField.setEditable(false);
		panel.add(currentUriField);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onOpen() {
		try {
			server.start();
			currentUriField.setText(server.getCurrentUri().toString());
			if (this.getNodeModel().isBrowserAutoOpen()) {
				openBrowser();
			}
		} catch (Exception e) {
			logger.warn(e);
			logger.warn(e.getCause());
		}
	}

	protected void openBrowser() {
		try {
			if (System.getProperty("os.name").contains("Linux")) {
				// Don't trust Desktop.getDesktop().browse under Linux due to gtk2/3 problems, causing KNIME to core dump
				openBrowserNativeLinux();
			} else if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(server.getCurrentUri());
			} else {
				logger.warn("Unable to open '" + server.getCurrentUri() + "' automatically in web browser");
				logger.warn("Please copy/paste the URL manually into a web browser");
			}
		} catch (IOException e) {
			logger.warn("Unable to open '" + server.getCurrentUri() + "' automatically in web browser");
			logger.warn("Please copy/paste the URL manually into a web browser");
		}
	}

	protected void openBrowserNativeLinux() throws IOException {
		Runtime runtime = Runtime.getRuntime();
		String[] command = { "xdg-open", server.getCurrentUri().toString() };
		runtime.exec(command);
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
