
/*
* Cursor.java
*
* Created on 01 August 2001, 06:02
*/
package jfreerails.client;
/** This class represents the cursor for the map view.  It receives key
* and mouse inputs and fires cursor events.
*
* @author Luke Lindsay
* @version 1
*/
import java.awt.*;
import java.awt.event.*;
import jfreerails.client.event.*;
import jfreerails.common.*;
import jfreerails.misc.*;
import jfreerails.client.view.map.*;


public class FreerailsCursor implements java.awt.event.KeyListener {

    
    /** This inner class controls rendering of the cursor.
    */
    public jfreerails.client.FreerailsCursor.CursorRenderer cursorRenderer = new jfreerails.client.FreerailsCursor.CursorRenderer();

    /**
     * @associates CursorEventListener 
     */
    protected java.util.Vector listeners = new java.util.Vector();

    private Point oldCursorMapPosition = new Point( 0, 0 );

    private static int blinkValue=1;
    /*The cursor tile and one tile in each direction
    */
    private java.awt.BasicStroke stroke = new java.awt.BasicStroke( 3 );

    
    //The width of the rectangle used to draw the cursor
    private int paintCursor;

    
    //private jfreerails.common.trackmodel.TrackBuilder trackBuilder;
    private Point cursorMapPosition = new Point( 0, 0 );

    private NewMapView mapView;
    
    /** This inner class controls rendering of the cursor.
    */
    

    public class CursorRenderer {
        
        /** Paints the cursor.  The method calculates position to paint it based on the
        * tile size and the cursor's map position.
        * @param g The graphics object to paint the cursor on.
        * @param tileSize The dimensions of a tile.
        */
        
        public void paintCursor( java.awt.Graphics g, java.awt.Dimension tileSize ) {
            java.awt.Graphics2D  g2 = (java.awt.Graphics2D)g;
            g2.setStroke( stroke );
            if(1==blinkValue){
            g2.setColor( java.awt.Color.white ); //The colour of the cursor
            }else{
                g2.setColor( java.awt.Color.black ); 
            }
            g2.drawRect( cursorMapPosition.x * tileSize.width, cursorMapPosition.y * tileSize.height, tileSize.width, tileSize.height );
        }
        
            
    }
    public static void blinkCursor(){
            blinkValue=-blinkValue;
        }
    
    /** Use this method rather than KeyTyped to process keyboard input.
    * @param keyEvent The key pressed.
    */
    
    public void keyPressed( KeyEvent keyEvent ) {
        switch( keyEvent.getKeyCode() ) {
          case KeyEvent.VK_NUMPAD1:
              moveCursor( OneTileMoveVector.SOUTH_WEST );
              break;
          
          case KeyEvent.VK_NUMPAD2:
              moveCursor( OneTileMoveVector.SOUTH );
              break;
          
          case KeyEvent.VK_NUMPAD3:
              moveCursor( OneTileMoveVector.SOUTH_EAST );
              break;
          
          case KeyEvent.VK_NUMPAD4:
              moveCursor( OneTileMoveVector.WEST );
              break;
          
          case KeyEvent.VK_NUMPAD6:
              moveCursor( OneTileMoveVector.EAST );
              break;
          
          case KeyEvent.VK_NUMPAD7:
              moveCursor( OneTileMoveVector.NORTH_WEST );
              break;
          
          case KeyEvent.VK_NUMPAD8:
              moveCursor( OneTileMoveVector.NORTH );
              break;
          
          case KeyEvent.VK_NUMPAD9:
              moveCursor( OneTileMoveVector.NORTH_EAST );
              break;
          
          default:
              fireOffCursorKeyPressed( keyEvent, cursorMapPosition );
        }
    
    // component = (MapViewJComponent)keyEvent.getComponent();
    }
    
    /** Moves the cursor provided the destination is a legal position.
    * @param tryThisPoint The cursor's destination.
    */
    
    public void TryMoveCursor( Point tryThisPoint ) {
    	Dimension tileSize=mapView.getTileSize();
        Dimension  mapSizeInPixels = mapView.getMapSizeInPixels();
       int maxX=(mapSizeInPixels.width/tileSize.width)-2;
       int maxY=(mapSizeInPixels.height/tileSize.height)-2;
        Rectangle  legalRectangle; //The set of legal cursor positions.
        legalRectangle = new Rectangle( 1, 1, maxX, maxY );
        if( true == legalRectangle.contains( tryThisPoint ) ) {
            
            /*Move the cursor. */
            oldCursorMapPosition.setLocation( cursorMapPosition );
            cursorMapPosition.setLocation( tryThisPoint );
            int  deltaX = cursorMapPosition.x - oldCursorMapPosition.x;
            int  deltaY = cursorMapPosition.y - oldCursorMapPosition.y;
            
            /*Build track! */
            if( OneTileMoveVector.checkValidity( deltaX, deltaY ) ) {

                    fireOffCursorOneTileMove( OneTileMoveVector.getInstance( deltaX, deltaY ), oldCursorMapPosition );

            }
            else {
                jfreerails.lib.TextMessageHandler.sendMessage( "Jumped to: " + cursorMapPosition.x + ", " + cursorMapPosition.y );
                fireOffCursorJumped( oldCursorMapPosition, cursorMapPosition );
            }
        }
        else {
            jfreerails.lib.TextMessageHandler.sendMessage( "Illegal cursor position!" );
        }
    }
    
    /** Empty method, needed to implement the KeyListener interface.
    * @param keyEvent The key typed.
    */
    
    public void keyTyped( KeyEvent keyEvent ) {
        
    }
    
    /** Use keyPressed instead of this method.
    * @param keyEvent the key pressed
    */
    
    public void keyReleased( KeyEvent keyEvent ) {
        
    }
    
    /** Creates a new FreerailsCursor.
    * @param mapView The view that the curors moves across.
    */
    
    public FreerailsCursor( NewMapView mapView ) {
        this.mapView = mapView;
    }
    
    /** Adds a listener.  Listeners could include: the trackbuild system, the view the cursor moves across, etc.
    * @param l The listener.
    */
    
    public void addCursorEventListener( CursorEventListener l ) {
        listeners.addElement( l );
    }
    
    /** Removes a listener.
    * @param l The listener.
    */
    
    public void removeCursorEventListener( CursorEventListener l ) {
        listeners.removeElement( l );
    }
    
    private void moveCursor( OneTileMoveVector v ) {
        TryMoveCursor( new Point( cursorMapPosition.x + v.getX(), cursorMapPosition.y + v.getY() ) );
    }
    
    private void fireOffCursorJumped( Point oldPosition, Point newPosition ) {
        CursorEvent  ce = new CursorEvent( this );
        ce.oldPosition = oldCursorMapPosition;
        ce.newPosition = newPosition;
        for( int  i = 0;i < listeners.size();i++ ) {
            ( (CursorEventListener)listeners.elementAt( i ) ).cursorJumped( ce );
        }
    }
    
    private void fireOffCursorOneTileMove( OneTileMoveVector v, Point oldPosition ) {
        CursorEvent  ce = new CursorEvent( this );
        ce.vector = v;
        ce.oldPosition = oldPosition;
        ce.newPosition = cursorMapPosition;
        for( int  i = 0;i < listeners.size();i++ ) {
            ( (CursorEventListener)listeners.elementAt( i ) ).cursorOneTileMove( ce );
        }
    }
    
    private void fireOffCursorKeyPressed( KeyEvent keyEvent, Point position ) {
        CursorEvent  ce = new CursorEvent( this );
        ce.keyEvent = keyEvent;
        ce.oldPosition = position;
        ce.newPosition = position;
        for( int  i = 0;i < listeners.size();i++ ) {
            ( (CursorEventListener)listeners.elementAt( i ) ).cursorKeyPressed( ce );
        }
    }
}
