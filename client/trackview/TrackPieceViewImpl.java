
/*
*  TrackPieceView.java
*
*  Created on 20 July 2001, 17:46
*/

/**
*@author     Luke Lindsay
*@version
*/
package jfreerails.client.trackview;
import jfreerails.common.exception.FreerailsException;
import java.awt.Image;

/**
*  Description of the Class
*
*@author     Luke Lindsay
*@created    09 October 2001
*/


public class TrackPieceViewImpl implements TrackPieceView {

    Image[] trackPieceIcons = new Image[ 512 ];
    
    /**
    *  Description of the Method
    *
    *@param  trackTemplate           Description of Parameter
    *@param  g                       Description of Parameter
    *@param  x                       Description of Parameter
    *@param  y                       Description of Parameter
    *@param  tileSize                Description of Parameter
    *@exception  FreerailsException  Description of Exception
    */
    
    public void drawTrackPieceIcon( int trackTemplate, java.awt.Graphics g, int x, int y, java.awt.Dimension tileSize ) throws FreerailsException {
        if( ( trackTemplate > 511 ) || ( trackTemplate < 0 ) ) {
            throw new FreerailsException( "trackTemplate = " + trackTemplate + ", it should be in the range 0-511" );
        }
        if( trackPieceIcons[ trackTemplate ] != null ) {
            int  drawX = x * tileSize.width - tileSize.width / 2;
            int  drawY = y * tileSize.height - tileSize.height / 2;
            g.drawImage( trackPieceIcons[ trackTemplate ], drawX, drawY, null );
        }
    }
    
    /**
    *  Creates new TrackPieceView
    *
    *@param  trackTemplatesPrototypes  int's representing the legal track pieces.
    *@param  trackImageSplitter        Source of track icons
    *@exception  FreerailsException    Description of Exception
    */
    
    public TrackPieceViewImpl( int[] trackTemplatesPrototypes, jfreerails.lib.ImageSplitter trackImageSplitter ) throws FreerailsException {
        trackImageSplitter.setTransparencyToTRANSLUCENT();
        
        //Since track tiles have transparent regions.
        for( int  i = 0;i < trackTemplatesPrototypes.length;i++ ) {
            
            /*
            *  Check for invalid parameters.
            */
            if( ( trackTemplatesPrototypes[ i ] > 511 ) || ( trackTemplatesPrototypes[ i ] < 0 ) ) {
                throw new FreerailsException( "trackTemplate = " + trackTemplatesPrototypes[ i ] + ", it should be in the range 0-511" );
            }
            
            /*
            *  Grab the images for those track pieces that are legal.
            */
            for( int  j = 0;j < trackTemplatesPrototypes.length;j++ ) {
                int[]  rotationsOfTrackTemplate = jfreerails.misc.EightRotationsOfTrackPieceProducer.getRotations( trackTemplatesPrototypes[ j ] );
                for( int  k = 0;k < rotationsOfTrackTemplate.length;k++ ) {
                    if( trackPieceIcons[ rotationsOfTrackTemplate[ k ] ] == null ) {
                        trackPieceIcons[ rotationsOfTrackTemplate[ k ] ] = trackImageSplitter.getTileFromSubGrid( k, j );
                    }
                }
            }
        }
    }
    
    /**
    *  Gets the trackPieceIcon attribute of the TrackPieceView object
    *
    *@param  trackTemplate           Description of Parameter
    *@return                         The trackPieceIcon value
    *@exception  FreerailsException  Description of Exception
    */
    
    public Image getTrackPieceIcon( int trackTemplate ) throws FreerailsException {
        if( ( trackTemplate > 511 ) || ( trackTemplate < 0 ) ) {
            throw new FreerailsException( "trackTemplate = " + trackTemplate + ", it should be in the range 0-511" );
        }
        return trackPieceIcons[ trackTemplate ];
    }
}
