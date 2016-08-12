package nl.esciencecenter.e3dchem.knime.ws;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeView;
import org.knime.core.node.property.hilite.HiLiteHandler;
import org.knime.core.node.property.hilite.HiLiteListener;

import nl.esciencecenter.e3dchem.knime.ws.server.HelloServer;

/**
 * <code>NodeView</code> for the "MolViewer" Node.
 *
 */
public class MolViewerView extends NodeView<MolViewerModel> implements ActionListener, HiLiteListener {
	private static final NodeLogger logger = NodeLogger
            .getLogger(MolViewerView.class);
	private JTextField current_uri_field;
	private HelloServer server;
	private HiLiteHandler m_hiliteHandler = null;
	
	/**
     * Creates a new view.
     *
     * @param nodeModel The model (class: {@link MolViewerModel})
     */
    public MolViewerView(final MolViewerModel nodeModel) {
        super(nodeModel);
        logger.warn("mol view: construct");

        setupPanel();
        server = new HelloServer("sdfviewer.html");
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
    	logger.warn("mol view: modelChanged");

    	// update internal hilite handler
        HiLiteHandler hiliteHandler = getNodeModel().getInHiLiteHandler(MolViewerModel.LIGAND_PORT);
        if (m_hiliteHandler == null) {
            m_hiliteHandler = hiliteHandler;
            m_hiliteHandler.addHiLiteListener(this);
            server.setHiLiteHandler(hiliteHandler);
        } else {
            if (hiliteHandler != m_hiliteHandler) {
                m_hiliteHandler.removeHiLiteListener(this);
                m_hiliteHandler = hiliteHandler;
                m_hiliteHandler.addHiLiteListener(this);
                server.setHiLiteHandler(hiliteHandler);
            }
        }
    	
        // retrieve the new model from your nodemodel and
        // update the view.
        // be aware of a possibly not executed nodeModel! The data you retrieve
        // from your nodemodel could be null, emtpy, or invalid in any kind.
        List<String> ligands = getNodeModel().getLigands();
        server.updateLigands(ligands);
        server.sendMessage("modelChanged");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClose() {
    	logger.warn("mol view: onClose");

    	try {
			server.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if (m_hiliteHandler != null) {
            m_hiliteHandler.removeHiLiteListener(this);
            m_hiliteHandler = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOpen() {
    	logger.warn("mol view: onOpen");

    	try {
    		server.start();
	    	current_uri_field.setText(server.getCurrentUri().toString());
	    	// TODO add checkbox to dialog to auto open browser
	    	openBrowser();
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
