/*
 * ClientOptionsJPanel.java
 *
 * Created on 20 December 2003, 15:57
 */

package jfreerails.launcher;

import java.awt.DisplayMode;
import java.util.logging.Logger;
import java.net.*;
import jfreerails.client.common.MyDisplayMode;
import jfreerails.client.common.ScreenHandler;
import jfreerails.client.view.DisplayModesComboBoxModels;
import javax.swing.event.*;
/**
 * The  Launcher panel that lets you choose fullscreen or windowed mode and the
 * screen resolution etc.
 *
 * @author  rtuck99@users.sourceforge.net
 *@author Luke Lindsay
 */
class ClientOptionsJPanel extends javax.swing.JPanel {
    private static final Logger logger = Logger.getLogger(ClientOptionsJPanel.class.getName());
    private final Launcher owner;
    
    String getPlayerName() {
        return playerName.getText();
    }
    
    DisplayMode getDisplayMode() {
        MyDisplayMode displayMode = ((MyDisplayMode) jList1.getSelectedValue());
        logger.fine("The selected display mode is "+displayMode.toString());
        return displayMode.displayMode;
    }
    InetSocketAddress getRemoteServerAddress() {
        String portStr = remotePort.getText();
        if (portStr == null) {
            return null;
        }
        int port;
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            return null;
        }
        InetSocketAddress address;
        try {
            address = new InetSocketAddress
            (remoteIP.getText(), port);
        } catch (IllegalArgumentException e) {
            return null;
        }
        /* cut and pasted
         InetSocketAddress isa = getRemoteServerAddress();
                if (isa == null) {
                    infoText = "Please enter a valid remote server address";
                } else if (isa.isUnresolved()) {
                    infoText = "Couldn't resolve remote server address";
                } else {
                    isValid = true;
                }
         */
        return address;
    }
    
    int getScreenMode(){
        if(this.fullScreenButton.isSelected()){
            return ScreenHandler.FULL_SCREEN;
        }else if(this.windowedButton.isSelected()){
            return ScreenHandler.WINDOWED_MODE;
        }else if(this.fixedSizeButton.isSelected()){
            return ScreenHandler.FIXED_SIZE_WINDOWED_MODE;
        }else{
            throw new IllegalStateException();
        }
    }
    
    boolean validateRemoteAddress(){
        
        boolean isValid = true;
        //Validate the port.
        try{
            int port = Integer.parseInt(this.remotePort.getText());
            if(port >= 0 && port <= 65535){
                this.portErrorMessage.setText("");
            }else{
                throw new Exception();
            }
        }catch(Exception e){
            isValid = false;
            this.portErrorMessage.setText("A valid port value is between 0 and 65535.");
        }
        //Validate the IPAddress.
        return isValid;
        
    }
    
    public void setControlsEnabled(boolean enabled) {
        windowedButton.setEnabled(enabled);
        fullScreenButton.setEnabled(enabled);
        fixedSizeButton.setEnabled(enabled);
        if (fullScreenButton.isSelected()) {
            jList1.setEnabled(enabled);
        }
    }
    
    private void validateSettings() {
        boolean isValid = false;
        String infoText = "";
        if (playerName.getText() == null ||
        playerName.getText().equals("")) {
            infoText = "Please set a name for your player";
        } else {
            isValid = true;
        }
        owner.setInfoText(infoText);
        owner.setNextEnabled(isValid);
    }
    
    private final DisplayModesComboBoxModels listModel;
    
    void setRemoteServerPanelVisible(boolean b){
        this.jPanel4.setVisible(b);
    }
    
    public ClientOptionsJPanel(Launcher owner) {
        this.owner = owner;
        initComponents();
        listModel = new DisplayModesComboBoxModels();
        listModel.removeDisplayModesBelow(640, 480, 16);
        jList1.setModel(listModel);
        jList1.setSelectedIndex(0);
        
        validateSettings();
        
        //Listen for changes in the server port text box.
        remotePort.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e) {
                validatePort();
            }
            public void removeUpdate(DocumentEvent e) {
                validatePort();
            }
            public void changedUpdate(DocumentEvent e) {
                validatePort();
            }
            
        });
        
        
    }
    
    boolean  validatePort(){
        try{
            int port = Integer.parseInt(remotePort.getText());
            if(port >= 0 && port <= 65535){
                portErrorMessage.setText("");
                return true;
            }
        }catch(Exception e){
        }
        portErrorMessage.setText("A valid port value is between 0 and 65535.");
        return false;
    }
    
    private void validateIP(){
        String host = this.remoteIP.getText();
        try{
            InetAddress address = InetAddress.getByName(host);
            ipErrorMessage.setText("");
        }catch(Exception e){
            ipErrorMessage.setText("IP address for "+host+" could be found");
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        playerName = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        remoteIP = new javax.swing.JTextField();
        ipErrorMessage = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        remotePort = new javax.swing.JTextField();
        portErrorMessage = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        windowedButton = new javax.swing.JRadioButton();
        fixedSizeButton = new javax.swing.JRadioButton();
        fullScreenButton = new javax.swing.JRadioButton();

        setLayout(new java.awt.GridBagLayout());

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel3.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Player Details"));
        jLabel1.setText("Player name:");
        jPanel3.add(jLabel1);

        playerName.setColumns(12);
        playerName.setText("Player1");
        playerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playerNameActionPerformed(evt);
            }
        });
        playerName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                playerNameFocusLost(evt);
            }
        });

        jPanel3.add(playerName);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jPanel3, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel4.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Remote server address"));
        jPanel4.setEnabled(false);
        jLabel2.setText("IP Address:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel4.add(jLabel2, gridBagConstraints);

        remoteIP.setColumns(15);
        remoteIP.setText("127.0.0.1");
        remoteIP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remoteIPActionPerformed(evt);
            }
        });
        remoteIP.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                remoteIPCaretUpdate(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel4.add(remoteIP, gridBagConstraints);

        ipErrorMessage.setForeground(java.awt.Color.red);
        ipErrorMessage.setText("  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(ipErrorMessage, gridBagConstraints);

        jLabel3.setText("port");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel4.add(jLabel3, gridBagConstraints);

        remotePort.setColumns(5);
        remotePort.setText("55000");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel4.add(remotePort, gridBagConstraints);

        portErrorMessage.setForeground(java.awt.Color.red);
        portErrorMessage.setText("   ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(portErrorMessage, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(jPanel4, gridBagConstraints);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Select Display Mode"));
        jScrollPane1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setEnabled(false);
        jScrollPane1.setViewportView(jList1);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        windowedButton.setSelected(true);
        windowedButton.setText("Windowed");
        buttonGroup1.add(windowedButton);
        jPanel2.add(windowedButton);

        fixedSizeButton.setText("Windowed (fixed size 640*480)");
        buttonGroup1.add(fixedSizeButton);
        jPanel2.add(fixedSizeButton);

        fullScreenButton.setText("Full screen");
        buttonGroup1.add(fullScreenButton);
        fullScreenButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fullScreenButtonStateChanged(evt);
            }
        });

        jPanel2.add(fullScreenButton);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);

    }//GEN-END:initComponents
    
    private void remoteIPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remoteIPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_remoteIPActionPerformed
    
    private void remoteIPCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_remoteIPCaretUpdate
        // TODO add your handling code here:
    }//GEN-LAST:event_remoteIPCaretUpdate
    
    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        validateSettings();
    }//GEN-LAST:event_formComponentShown
    
    private void playerNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playerNameActionPerformed
        validateSettings();
    }//GEN-LAST:event_playerNameActionPerformed
    
    private void playerNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_playerNameFocusLost
        validateSettings();
    }//GEN-LAST:event_playerNameFocusLost
    
    private void fullScreenButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fullScreenButtonStateChanged
        jList1.setEnabled(fullScreenButton.isSelected());
    }//GEN-LAST:event_fullScreenButtonStateChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton fixedSizeButton;
    private javax.swing.JRadioButton fullScreenButton;
    private javax.swing.JLabel ipErrorMessage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField playerName;
    private javax.swing.JLabel portErrorMessage;
    private javax.swing.JTextField remoteIP;
    private javax.swing.JTextField remotePort;
    private javax.swing.JRadioButton windowedButton;
    // End of variables declaration//GEN-END:variables
    
}