/*
 * FreeRails
 * Copyright (C) 2000-2018 The FreeRails Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 *
 */
package freerails.move;

import freerails.model.world.WorldItem;
import freerails.util.Vector2D;
import freerails.util.Utils;
import freerails.model.*;
import freerails.model.game.GameCalendar;
import freerails.model.player.FreerailsPrincipal;
import freerails.model.player.Player;
import freerails.model.terrain.FullTerrainTile;
import freerails.model.terrain.TileTransition;
import freerails.model.train.PathOnTiles;
import freerails.model.world.FullWorld;
import freerails.model.world.World;
import junit.framework.TestCase;

import java.io.Serializable;

/**
 * All TestCases for moves should extend this class.
 */
public abstract class AbstractMoveTestCase extends TestCase {

    protected World world;
    private boolean hasSetupBeenCalled = false;

    public AbstractMoveTestCase() {
    }

    /**
     * @param str
     */
    public AbstractMoveTestCase(String str) {
        super(str);
    }

    /**
     * @param move
     */
    void assertDoMoveFails(Move move) {
        assertSetupHasBeenCalled();

        MoveStatus moveStatus = move.doMove(world, Player.AUTHORITATIVE);
        assertNotNull(moveStatus);
        assertTrue("Move went through when it should have failed", !moveStatus.succeeds());
    }

    /**
     * @param move
     */
    protected void assertDoMoveIsOk(Move move) {
        assertSetupHasBeenCalled();

        MoveStatus moveStatus = move.doMove(world, Player.AUTHORITATIVE);
        assertNotNull(moveStatus);
        assertEquals(MoveStatus.MOVE_OK, moveStatus);
    }

    /**
     * @param move
     */
    public void assertDoThenUndoLeavesWorldUnchanged(Move move) {
        try {
            World world = this.world;
            World before = (World) Utils.cloneBySerialisation(world);

            assertEquals(before, world);

            assertTrue(move.doMove(world, Player.AUTHORITATIVE).succeeds());
            World after = (World) Utils.cloneBySerialisation(world);
            assertFalse(after.equals(before));

            boolean b = move.undoMove(world, Player.AUTHORITATIVE).succeeds();
            assertTrue(b);
            assertEquals(before, world);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    /**
     * This method asserts that if we serialise then deserialize the specified
     * move, the specified move is equal to the deserialized move. The assertion
     * depends on the move being serializable and the equals method being
     * implemented correctly. Also checks that the hashcode does not change.
     *
     * @param m m
     */
    protected void assertSurvivesSerialisation(Serializable m) {
        assertEquals("Reflexivity violated: the move does not equal itself", m,
                m);

        try {
            Object o = Utils.cloneBySerialisation(m);
            assertEquals(m, o);
            assertEquals("The hashcodes should be the same!", m.hashCode(), o
                    .hashCode());
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    /**
     * @param move
     */
    void assertOkAndRepeatable(Move move) {
        assertSetupHasBeenCalled();

        // Do move
        assertTryMoveIsOk(move);
        assertDoMoveIsOk(move);
        assertTryMoveIsOk(move);
        assertDoMoveIsOk(move);

        // Since it leaves the world unchanged it should also be
        // possible to undo it repeatably
        assertTryUndoMoveIsOk(move);
        assertUndoMoveIsOk(move);
        assertTryUndoMoveIsOk(move);
        assertUndoMoveIsOk(move);
    }

    /**
     * Generally moves should not be repeatable. For example, if we have just
     * removed a piece of track, that piece of track is gone, so we cannot
     * remove it again.
     *
     * @param move
     */
    public void assertOkButNotRepeatable(Move move) {
        assertSetupHasBeenCalled();

        assertTryMoveIsOk(move);
        assertDoMoveIsOk(move);
        assertTryMoveFails(move);
        assertDoMoveFails(move);
        assertTryUndoMoveIsOk(move);
        assertUndoMoveIsOk(move);
        assertTryUndoMoveFails(move);
        assertTryMoveIsOk(move);
        assertDoMoveIsOk(move);
    }

    private void assertSetupHasBeenCalled() {
        assertTrue("AbstractMoveTestCase.setUp has not been called!",
                hasSetupBeenCalled());
    }

    /**
     * @param move
     */
    public void assertTryMoveFails(Move move) {
        assertSetupHasBeenCalled();

        MoveStatus moveStatus = move.tryDoMove(world, Player.AUTHORITATIVE);
        assertNotNull(moveStatus);
        assertTrue("Move went through when it should have failed", !moveStatus.succeeds());
    }

    /**
     * @param move
     */
    public void assertTryMoveIsOk(Move move) {
        assertSetupHasBeenCalled();

        MoveStatus moveStatus = move.tryDoMove(world, Player.AUTHORITATIVE);
        assertNotNull(moveStatus);
        assertEquals("First try failed", MoveStatus.MOVE_OK, moveStatus);

        moveStatus = move.tryDoMove(world, Player.AUTHORITATIVE);
        assertNotNull(moveStatus);
        assertEquals(
                "Second try failed, this suggests that the tryDoMove method failed to leave the world unchanged!",
                MoveStatus.MOVE_OK, moveStatus);
    }

    /**
     * @param move
     */
    public void assertTryUndoMoveFails(Move move) {
        assertSetupHasBeenCalled();

        MoveStatus moveStatus = move.tryUndoMove(world, Player.AUTHORITATIVE);
        assertNotNull(moveStatus);
        assertTrue("Move went through when it should have failed", !moveStatus.succeeds());
    }

    /**
     * @param move
     */
    public void assertTryUndoMoveIsOk(Move move) {
        assertSetupHasBeenCalled();

        MoveStatus moveStatus = move.tryUndoMove(world, Player.AUTHORITATIVE);
        assertNotNull(moveStatus);
        assertEquals("First try failed", MoveStatus.MOVE_OK, moveStatus);

        moveStatus = move.tryUndoMove(world, Player.AUTHORITATIVE);
        assertNotNull(moveStatus);
        assertEquals(
                "Second try failed, this suggests that the tryDoMove method failed to leave the world unchanged!",
                MoveStatus.MOVE_OK, moveStatus);
    }

    /**
     * @param move
     */
    public void assertUndoMoveIsOk(Move move) {
        assertSetupHasBeenCalled();

        MoveStatus moveStatus = move.undoMove(world, Player.AUTHORITATIVE);
        assertNotNull(moveStatus);
        assertEquals(MoveStatus.MOVE_OK, moveStatus);
    }

    FreerailsPrincipal getPrincipal() {
        return world.getPlayer(0).getPrincipal();
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * @return
     */
    private boolean hasSetupBeenCalled() {
        return hasSetupBeenCalled;
    }

    /**
     * @param hasSetupBeenCalled
     */
    public void setHasSetupBeenCalled(boolean hasSetupBeenCalled) {
        this.hasSetupBeenCalled = hasSetupBeenCalled;
    }

    /**
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.hasSetupBeenCalled = true;
        setupWorld();
    }

    /**
     *
     */
    protected void setupWorld() {
        this.world = new FullWorld(new Vector2D(10, 10));
        // Set the time..
        world.set(WorldItem.Calendar, new GameCalendar(12000, 1840));
        world.addPlayer(MapFixtureFactory.TEST_PLAYER);
    }

    /**
     * @param x
     * @param y
     */
    protected void assertTrackHere(int x, int y) {

        FullTerrainTile tile = (FullTerrainTile) world.getTile(new Vector2D(x, y));
        assertTrue(tile.hasTrack());
    }

    /**
     * @param path
     */
    protected void assertTrackHere(PathOnTiles path) {
        Vector2D start = path.getStart();
        int x = start.x;
        int y = start.y;
        for (int i = 0; i < path.steps(); i++) {
            assertTrackHere(x, y);
            TileTransition tileTransition = path.getStep(i);
            x += tileTransition.deltaX;
            y += tileTransition.deltaY;
            assertTrackHere(x, y);
        }
    }
}