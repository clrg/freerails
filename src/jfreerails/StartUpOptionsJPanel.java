/*
 * StartUpOptionsJPanel.java
 *
 * Created on 30 August 2003, 22:41
 */

package jfreerails;

import java.awt.DisplayMode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import jfreerails.client.common.ScreenHandler;
import jfreerails.client.view.DisplayModesComboBoxModels;
import jfreerails.client.view.MyDisplayMode;
import jfreerails.server.GameServer;
import jfreerails.util.FreerailsProgressMonitor;

/**
 *
 * @author  Luke Lindsay
 */
public class StartUpOptionsJPanel extends javax.swing.JPanel {
    
    int mode = ScreenHandler.WINDOWED_MODE;
    
    DisplayMode selectedDisplayMode= null;
    
    private GameServer gs;

    int numberOfClients = 1;
    
    private FreerailsProgressMonitor pm;

    private RunFreerails launcher;
    
    private JFrame parentWindow;
    
    void setProgressMonitor(FreerailsProgressMonitor aPm) {
	pm = aPm;
	launcher = new RunFreerails(pm);
    }

    /** Creates new form StartUpOptionsJPanel */
    public StartUpOptionsJPanel(JFrame aParentWindow) {
	parentWindow = aParentWindow;
        initComponents();
	remoteServerCheckBox1.addActionListener(remoteServerCheckBoxListener);	
	remoteIPTextField.setEnabled(false);
	startServerJButton.addActionListener(startServerActionListener);
	startGameJButton.addActionListener(startGameActionListener);
    }
    
    private ActionListener startGameActionListener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	    gs.startGame();
	    parentWindow.dispose();
	}
    };

    private ActionListener startServerActionListener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	    gs = launcher.startServer();
	    clientJTable.setModel(gs.getClientConnectionTableModel());
	    startGameJButton.setEnabled(true);	    
	}
    };

    private ActionListener remoteServerCheckBoxListener = new
    ActionListener() {
	public void actionPerformed(ActionEvent e) {
	    remoteIPTextField.setEnabled(remoteServerCheckBox1.isSelected());
	}
    };

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        displayOptions = new javax.swing.ButtonGroup();
        clientOptionsJPanel = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        startJButton = new javax.swing.JButton();
        displaymode = new javax.swing.JComboBox();
        numberOfClientsJComboBox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        remoteServerCheckBox1 = new javax.swing.JCheckBox();
        remoteIPTextField = new javax.swing.JTextField();
        serverOptionsJPanel = new javax.swing.JPanel();
        startServerJButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        clientJTable = new javax.swing.JTable();
        startGameJButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setAlignmentX(0.0F);
        setAlignmentY(0.0F);
        clientOptionsJPanel.setLayout(new java.awt.GridBagLayout());

        clientOptionsJPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Client Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12)));
        clientOptionsJPanel.setName("Client Options");
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Windowed Mode");
        displayOptions.add(jRadioButton1);
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        clientOptionsJPanel.add(jRadioButton1, gridBagConstraints);

        jRadioButton2.setText("Windowed Mode (fixed size 640x480)");
        displayOptions.add(jRadioButton2);
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        clientOptionsJPanel.add(jRadioButton2, gridBagConstraints);

        jRadioButton3.setText("Fullscreen");
        displayOptions.add(jRadioButton3);
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        clientOptionsJPanel.add(jRadioButton3, gridBagConstraints);

        startJButton.setText("Launch Client(s)");
        startJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startJButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        clientOptionsJPanel.add(startJButton, gridBagConstraints);

        new DisplayModesComboBoxModels();
        displaymode.setModel(new DisplayModesComboBoxModels());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        clientOptionsJPanel.add(displaymode, gridBagConstraints);

        numberOfClientsJComboBox.setModel(new javax.swing.DefaultComboBoxModel(new Integer[] { new Integer(1), new Integer(2), new Integer(3), new Integer(4)   }));
        numberOfClientsJComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberOfClientsJComboBoxActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        clientOptionsJPanel.add(numberOfClientsJComboBox, gridBagConstraints);

        jLabel1.setText("Client(s)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        clientOptionsJPanel.add(jLabel1, gridBagConstraints);

        remoteServerCheckBox1.setText("Remote Server");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        clientOptionsJPanel.add(remoteServerCheckBox1, gridBagConstraints);

        remoteIPTextField.setText("127.0.0.1");
        remoteIPTextField.setMinimumSize(new java.awt.Dimension(150, 21));
        remoteIPTextField.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        clientOptionsJPanel.add(remoteIPTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(clientOptionsJPanel, gridBagConstraints);

        serverOptionsJPanel.setLayout(new java.awt.GridBagLayout());

        serverOptionsJPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Server Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12)));
        serverOptionsJPanel.setName("Server Options");
        startServerJButton.setText("Launch Server");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        serverOptionsJPanel.add(startServerJButton, gridBagConstraints);

        clientJTable.setToolTipText("The currently connected clients");
        clientJTable.setPreferredScrollableViewportSize(new java.awt.Dimension(150, 50));
        jScrollPane1.setViewportView(clientJTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        serverOptionsJPanel.add(jScrollPane1, gridBagConstraints);

        startGameJButton.setText("Start Game");
        startGameJButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        serverOptionsJPanel.add(startGameJButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(serverOptionsJPanel, gridBagConstraints);

    }//GEN-END:initComponents
    
    private void numberOfClientsJComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numberOfClientsJComboBoxActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_numberOfClientsJComboBoxActionPerformed
    
    /**
     * Called when the Start Clients button is pressed.
     */
    private void startJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startJButtonActionPerformed
        //disable all the controls        
        displaymode.setEnabled(false);
        jRadioButton1.setEnabled(false);
        jRadioButton2.setEnabled(false);
        jRadioButton3.setEnabled(false);
        numberOfClientsJComboBox.setEnabled(false);
        startJButton.setEnabled(false);        
        
        //Set selected options.
        selectedDisplayMode = ((MyDisplayMode)displaymode.getSelectedItem()).displayMode;
                
	Integer integer =
	new Integer (1 +this.numberOfClientsJComboBox.getSelectedIndex());
		this.numberOfClients= integer.intValue();
         
        RunFreerails rf = launcher;
        rf.setDisplayMode(this.selectedDisplayMode);
        rf.setNumberOfClients(this.numberOfClients);
        rf.setMode(this.mode);
	if (remoteServerCheckBox1.isSelected()) {
	    try {
		InetAddress address =
		    InetAddress.getByName(remoteIPTextField.getText());
		rf.setRemoteServer(address);
	    } catch (UnknownHostException e) {
		System.err.println("Unknown host " +
		remoteIPTextField.getText());
		return;
	    }
	}
	try {
	    launcher.startClients();
	} catch (IllegalStateException e) {
		e.printStackTrace();
	    /* do nothing */
	}
    }//GEN-LAST:event_startJButtonActionPerformed
    
    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // Add your handling code here:
        selectionChanged();
        mode = ScreenHandler.FULL_SCREEN;
      
    }//GEN-LAST:event_jRadioButton3ActionPerformed
    
    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // Add your handling code here:
        selectionChanged();
        mode = ScreenHandler.FIXED_SIZE_WINDOWED_MODE;
    }//GEN-LAST:event_jRadioButton2ActionPerformed
    
    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // Add your handling code here:
        selectionChanged();
        mode = ScreenHandler.WINDOWED_MODE;
    }//GEN-LAST:event_jRadioButton1ActionPerformed
    
    public void selectionChanged(){
        if(jRadioButton3.isSelected()){
            displaymode.setEnabled(true);
            numberOfClientsJComboBox.setSelectedIndex(0);
            numberOfClientsJComboBox.setEnabled(false);
        }else{
            displaymode.setEnabled(false);
            numberOfClientsJComboBox.setEnabled(true);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable clientJTable;
    private javax.swing.JPanel clientOptionsJPanel;
    private javax.swing.ButtonGroup displayOptions;
    private javax.swing.JComboBox displaymode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox numberOfClientsJComboBox;
    private javax.swing.JTextField remoteIPTextField;
    private javax.swing.JCheckBox remoteServerCheckBox1;
    private javax.swing.JPanel serverOptionsJPanel;
    private javax.swing.JButton startGameJButton;
    private javax.swing.JButton startJButton;
    private javax.swing.JButton startServerJButton;
    // End of variables declaration//GEN-END:variables
    
}
