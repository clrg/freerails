
package jfreerails.world;

import java.util.Iterator;
import java.util.Vector;

import jfreerails.world.financial.*;
import jfreerails.world.financial.RRCompany;


/**
 * Represents ...
 * 
 * @see OtherClasses
 * @author lindsal
 */

public class TrackList {


    public Vector trackNodeVector = new Vector();
    public RRCompany rRCompany;
    public GameElementsList gameElementsList;


   ///////////////////////////////////////
   // access methods for associations


    public Vector getTrackNode() {
        return trackNodeVector;
    }
//    public void addTrackNode(TrackNode trackNode) {
//        if (! this.trackNodeVector.contains(trackNode)) {     
//            this.trackNodeVector.addElement(trackNode);  
//            trackNode.setTrackList(this);
//        }
//    }
//    public void removeTrackNode(TrackNode trackNode) {
//        if (this.trackNodeVector.removeElement(trackNode)) {      
//            trackNode.setTrackList(null);
//        }
//    }

//    public RRCompany getRRCompany() {
//        return rRCompany;
//    }
//    public void setRRCompany(RRCompany rRCompany) {
//        if (this.rRCompany != rRCompany) {
//            this.rRCompany = rRCompany;
//            if (rRCompany != null)
//                rRCompany.setTrackList(this);  
//        }      
//    } 

//    public GameElementsList getGameElementsList() {
//        return gameElementsList;
//    }
//    public void setGameElementsList(GameElementsList gameElementsList) {
//        if (this.gameElementsList != gameElementsList) {
//            this.gameElementsList = gameElementsList;
//            if (gameElementsList != null)
//                gameElementsList.setTrackList(this);  
//        }      
//    } 
//

  ///////////////////////////////////////
  // operations

/**
 * Does ...
 * 
 * @return A Iterator with ...
 */

    public Iterator getTrackNodesIterator() {
        return null;
    }
/**
 * Does ...
 * 
 * @return A Iterator with ...
 */

    public Iterator getTrackSectionsIterator() {
        return null;
    }
/**
 * Does ...
 * 
 * @return A Player with ...
 */

    public Player getOwner() {
        return null;
    }
    
//    public boolean contains(TrackNode trackNode){
//    	return trackNodeVector.contains(trackNode);
//    }



}




