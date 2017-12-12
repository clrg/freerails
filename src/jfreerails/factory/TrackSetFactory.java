package jfreerails.factory;
import java.awt.Point;

import jfreerails.client.trackview.TrackPieceViewList;
import jfreerails.lib.ImageSplitter;
import jfreerails.world.TrackRuleList;


/**
*  Description of the Interface
*
*@author     Luke Lindsay
*@created    09 October 2001
*/


public interface TrackSetFactory {
    
   
    
     TrackPieceViewList getTrackViewList( ImageSplitter trackImageSplitter ) ;
    

    
     TrackRuleList getTrackRuleList();
    

    
     Point getTrackPieceSize();
}