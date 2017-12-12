
/*
* BuildTrainView.java
*
* Created on 14 de julio de 2001, 05:30 PM
*/
package jfreerails.client.train;
import javax.swing.JEditorPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.*;
import java.awt.*;

/**
*
* @author  Samuel Benzaquen
*/


public class BuildTrainView extends java.awt.Frame {

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList TrainList;

    
    // End of variables declaration//GEN-END:variables
    private Object selected = null;

    private javax.swing.JButton Cancel;

    private javax.swing.JButton OK;

    private javax.swing.JPanel PanelButtons;
    

    private class TrainCellRenderer implements ListCellRenderer {
        
        public Component getListCellRendererComponent( JList list, Object value, /* value to display*/ int index, /* cell index*/ boolean isSelected, /* is the cell selected*/ boolean cellHasFocus ) /* the list and the cell have the focus*/ {
            return new JLabel( BuildTrainController.getText( value, isSelected ), new ImageIcon( BuildTrainController.getImg( value, isSelected ) ), SwingConstants.LEFT );
        }
    }
    
    /** Creates new form BuildTrainView */
    
    public BuildTrainView( Object[] list ) {
        initComponents();
        TrainList.setListData( list );
        pack();
    }
    
    synchronized Object getSelectedValue() {
        try {
            wait();
        }
        catch( InterruptedException e ) {
            e.printStackTrace( System.err );
            System.err.println( "This should not be happening" );
        }
        return ( selected );
    }
    
    private void OKActionPerformed( java.awt.event.ActionEvent evt ) { //GEN-FIRST:event_OKActionPerformed
        selected = TrainList.getSelectedValue();
        synchronized( this ) {
            notify();
        }
    } //GEN-LAST:event_OKActionPerformed
    
    /** Exit the Application */
    
    private void exitForm( java.awt.event.WindowEvent evt ) { //GEN-FIRST:event_exitForm
        System.exit( 0 );
    } //GEN-LAST:event_exitForm
    
    private void CancelActionPerformed( java.awt.event.ActionEvent evt ) { //GEN-FIRST:event_CancelActionPerformed
        synchronized( this ) {
            notify();
        }
    } //GEN-LAST:event_CancelActionPerformed
    
    /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
    
    private void initComponents() { //GEN-BEGIN:initComponents
        TrainList = new javax.swing.JList();
        PanelButtons = new javax.swing.JPanel();
        OK = new javax.swing.JButton();
        Cancel = new javax.swing.JButton();
        addWindowListener( new java.awt.event.WindowAdapter()  {
            
            public void windowClosing( java.awt.event.WindowEvent evt ) {
                exitForm( evt );
            }
        } );
        TrainList.setCellRenderer( new TrainCellRenderer() );
        TrainList.setSelectionMode( javax.swing.ListSelectionModel.SINGLE_SELECTION );
        add( TrainList, java.awt.BorderLayout.NORTH );
        OK.setText( "OK" );
        OK.addActionListener( new java.awt.event.ActionListener()  {
            
            public void actionPerformed( java.awt.event.ActionEvent evt ) {
                OKActionPerformed( evt );
            }
        } );
        PanelButtons.add( OK );
        Cancel.setText( "Cancel" );
        Cancel.addActionListener( new java.awt.event.ActionListener()  {
            
            public void actionPerformed( java.awt.event.ActionEvent evt ) {
                CancelActionPerformed( evt );
            }
        } );
        PanelButtons.add( Cancel );
        add( PanelButtons, java.awt.BorderLayout.SOUTH );
        pack();
    } //GEN-END:initComponents
}
