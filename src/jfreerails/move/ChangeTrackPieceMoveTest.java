
/*
 * ChangeTrackPieceMoveTest.java
 * JUnit based test
 *
 * Created on 24 January 2002, 23:57
 */

package jfreerails.move;
import java.awt.Point;

import jfreerails.MapFixtureFactory;
import jfreerails.world.top.KEY;
import jfreerails.world.top.World;
import jfreerails.world.top.WorldImpl;
import jfreerails.world.track.TrackConfiguration;
import jfreerails.world.track.TrackPiece;
import jfreerails.world.track.TrackRule;

/**
 *
 * @author lindsal
 */

public class ChangeTrackPieceMoveTest extends junit.framework.TestCase {
   	
   	World world;
   	
    public ChangeTrackPieceMoveTest(String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static junit.framework.Test suite() {
        junit.framework.TestSuite testSuite = new junit.framework.TestSuite(ChangeTrackPieceMoveTest.class);
        return testSuite;
    }
    
    protected void setUp(){
           
        world = new WorldImpl(20, 20);
		MapFixtureFactory.generateTrackRuleList(world);       
    }
    
    public void testTryDoMove(){
        setUp();
        TrackPiece oldTrackPiece, newTrackPiece;
        TrackConfiguration oldConfig, newConfig;
        NewTrackMove move;
        MoveStatus moveStatus;
        
        //Try building the simplest piece of track.
        newConfig=TrackConfiguration.getFlatInstance("000010000");
        oldTrackPiece=(TrackPiece)world.getTile(0,0);
        
        TrackRule r = (TrackRule)world.get(KEY.TRACK_RULES, 0);
        newTrackPiece=r.getTrackPiece(newConfig);
        move=new ChangeTrackPieceMove(oldTrackPiece, newTrackPiece, new Point(0,0));
        moveStatus=move.tryDoMove(world);
        assertNotNull(moveStatus);
        assertEquals(true, moveStatus.isOk());
        
        //As above but with newTrackPiece and oldTrackPiece in the wrong order, should fail.
        move=new ChangeTrackPieceMove(newTrackPiece, oldTrackPiece, new Point(0,0));
        moveStatus=move.tryDoMove(world);
        assertNotNull(moveStatus);
        assertEquals(false, moveStatus.isOk());
        
        //Try a move that does nothing, i.e. oldTrackPiece==newTrackPiece, should fail.
        move=new ChangeTrackPieceMove(oldTrackPiece, oldTrackPiece, new Point(0,0));
        moveStatus=move.tryDoMove(world);
        assertNotNull(moveStatus);
        assertEquals(false, moveStatus.isOk());
        
        //Try buildingtrack outside the map.
        move=new ChangeTrackPieceMove(newTrackPiece, oldTrackPiece, new Point(100,0));
        moveStatus=move.tryDoMove(world);
        assertNotNull(moveStatus);
        assertEquals(false, moveStatus.isOk());
        
        //Try building an illegal track configuration.
        newConfig=TrackConfiguration.getFlatInstance("000011111");
        
        r=(TrackRule)world.get(KEY.TRACK_RULES, 0);
        newTrackPiece=r.getTrackPiece(newConfig);
        move=new ChangeTrackPieceMove(oldTrackPiece, newTrackPiece, new Point(0,0));
        moveStatus=move.tryDoMove(world);
        assertEquals(false, moveStatus.isOk());
        
    }
    
    public void  testTryUndoMove(){
        setUp();
        TrackPiece oldTrackPiece, newTrackPiece;
        TrackConfiguration oldConfig, newConfig;
        NewTrackMove move;
        MoveStatus moveStatus;
    }
    
    public void testDoMove(){
        setUp();
        TrackPiece oldTrackPiece, newTrackPiece;
        TrackConfiguration oldConfig, newConfig;
        NewTrackMove move;
        MoveStatus moveStatus;
        
        //Try building the simplest piece of track.
        newConfig=TrackConfiguration.getFlatInstance("000010000");
        oldTrackPiece=(TrackPiece)world.getTile(0, 0);
		TrackRule r = (TrackRule)world.get(KEY.TRACK_RULES, 0);
        newTrackPiece=r.getTrackPiece(newConfig);
        
        assertMoveDoMoveIsOk(oldTrackPiece, newTrackPiece);
    }

	protected void assertMoveDoMoveIsOk(TrackPiece oldTrackPiece, TrackPiece newTrackPiece) {
		
		NewTrackMove move;
		MoveStatus moveStatus;
		
		move=new ChangeTrackPieceMove(oldTrackPiece, newTrackPiece, new Point(0,0));
		moveStatus=move.doMove(world);
		assertNotNull(moveStatus);
		assertEquals(true, moveStatus.isOk());
		assertEquals(newTrackPiece, world.getTile(0,0) );
		
	}
    
    public void testUndoMove(){
    	
    	//Exactly like testDoMove() v
    	setUp();
        TrackPiece oldTrackPiece, newTrackPiece;
        TrackConfiguration oldConfig, newConfig;
        NewTrackMove move;
        MoveStatus moveStatus;
        
        //Try building the simplest piece of track.
        newConfig=TrackConfiguration.getFlatInstance("000010000");
        oldTrackPiece=(TrackPiece)world.getTile(0,0);
        
        
      //  assertMoveDoMoveIsOk(oldTrackPiece, newConfig);
        
    }
    
   private void assertTryMoveIsOK(NewTrackMove move, boolean undo, String testDescription){
    	
    }
    
   private void assertExecuteMoveIsOK(NewTrackMove move, boolean undo, String testDescription){
    	
    }
    
}
