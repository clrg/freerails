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
package freerails.server;

import freerails.move.Move;
import freerails.move.TimeTickMove;
import freerails.move.WorldDiffMove;
import freerails.move.WorldDiffMoveCause;
import freerails.world.FullWorldDiffs;
import freerails.world.ITEM;
import freerails.world.World;
import freerails.world.game.GameCalendar;
import freerails.world.game.GameSpeed;
import freerails.world.game.GameTime;

import java.util.ArrayList;
import java.util.List;

/**
 * A ServerGameModel that contains the automations used in the actual game.
 */
public class FullServerGameModel implements ServerGameModel {

    private static final long serialVersionUID = 3978144352788820021L;
    /**
     * List of the ServerAutomaton objects connected to this game.
     */
    private final List<ServerAutomaton> serverAutomata;
    private World world;
    private transient SupplyAtStationsUpdater supplyAtStationsUpdater;
    private TrainUpdater trainUpdater;
    private String[] passwords;
    /**
     * Number of ticks since the last time we did an infrequent update.
     */
    private int ticksSinceUpdate = 0;
    private transient long nextModelUpdateDue;
    private transient MoveReceiver moveReceiver;

    /**
     *
     */
    public FullServerGameModel() {
        this(null, new ArrayList<>());
    }

    /**
     * @param w
     * @param serverAutomata
     */
    private FullServerGameModel(World w, List<ServerAutomaton> serverAutomata) {
        world = w;
        this.serverAutomata = serverAutomata;
        nextModelUpdateDue = System.currentTimeMillis();
    }

    /**
     * This is called on the last tick of each year.
     */
    private void yearEnd() {
        TrackMaintenanceMoveGenerator trackMaintenanceMoveGenerator = new TrackMaintenanceMoveGenerator(moveReceiver);
        trackMaintenanceMoveGenerator.update(world);

        TrainMaintenanceMoveGenerator trainMaintenanceMoveGenerator = new TrainMaintenanceMoveGenerator(moveReceiver);
        trainMaintenanceMoveGenerator.update(world);

        BondInterestMoveGenerator bondInterestMoveGenerator = new BondInterestMoveGenerator(moveReceiver);
        bondInterestMoveGenerator.update(world);

        // Grow cities.
        FullWorldDiffs fullWorldDiffs = new FullWorldDiffs(world);
        CityTilePositioner cityTilePositioner = new CityTilePositioner(fullWorldDiffs);
        cityTilePositioner.growCities();

        Move move = new WorldDiffMove(world, fullWorldDiffs, WorldDiffMoveCause.YearEnd);
        moveReceiver.process(move);
    }

    /**
     * This is called at the start of each new month.
     */
    private void monthEnd() {
        supplyAtStationsUpdater.update();

        CargoAtStationsUpdater cargoAtStationsUpdater = new CargoAtStationsUpdater();
        cargoAtStationsUpdater.update(world, moveReceiver);
    }

    private void updateGameTime() {
        moveReceiver.process(TimeTickMove.getMove(world));
    }

    /**
     *
     */
    public synchronized void update() {
        long frameStartTime = System.currentTimeMillis();

        while (nextModelUpdateDue <= frameStartTime) {
            /*
             * First do the things that need doing whether or not the game is
             * paused.
             */
            trainUpdater.buildTrains(world);

            int gameSpeed = ((GameSpeed) world.get(ITEM.GAME_SPEED)).getSpeed();

            if (gameSpeed > 0) {
                /*
                 * Update the time first, since other updates might need to know
                 * the current time.
                 */
                updateGameTime();

                // now do the other updates
                trainUpdater.moveTrains(world);

                // Check whether we are about to start a new year..
                GameTime time = world.currentTime();
                GameCalendar calendar = (GameCalendar) world.get(ITEM.CALENDAR);
                int yearNextTick = calendar.getYear(time.getTicks() + 1);
                int yearThisTick = calendar.getYear(time.getTicks());

                if (yearThisTick != yearNextTick) {
                    yearEnd();
                }

                // And a new month..
                int monthThisTick = calendar.getMonth(time.getTicks());
                int monthNextTick = calendar.getMonth(time.getTicks() + 1);

                if (monthNextTick != monthThisTick) {
                    monthEnd();
                }

                // calculate "ideal world" time for next tick
                nextModelUpdateDue = nextModelUpdateDue + (1000 / gameSpeed);

                // int delay = (int)(nextModelUpdateDue - frameStartTime);
                //
                // /* wake up any waiting client threads - we could be
                // * more aggressive, and only notify them if delay > 0? */
                // this.notifyAll();
                //
                // try {
                // if (delay > 0) {
                // this.wait(delay);
                // } else {
                // this.wait(1);
                // }
                // } catch (InterruptedException e) {
                //
                // }
                ticksSinceUpdate++;
            } else {
                // try {
                // //When the game is frozen we don't want to be spinning in a
                // //loop.
                // Thread.sleep(200);
                // } catch (InterruptedException e) {
                //
                // }
                nextModelUpdateDue = System.currentTimeMillis();
            }
        }
    }

    /**
     * @param moveReceiver
     */
    public void initialize(MoveReceiver moveReceiver) {
        this.moveReceiver = moveReceiver;
        trainUpdater = new TrainUpdater(moveReceiver);
        supplyAtStationsUpdater = new SupplyAtStationsUpdater(world, moveReceiver);

        for (ServerAutomaton aServerAutomata : serverAutomata) {
            aServerAutomata.initAutomaton(moveReceiver);
        }

        trainUpdater.initAutomaton(moveReceiver);
        nextModelUpdateDue = System.currentTimeMillis();
    }

    /**
     * @return
     */
    public World getWorld() {
        return world;
    }

    /**
     * @param world
     * @param passwords
     */
    public void setWorld(World world, String[] passwords) {
        this.world = world;
        serverAutomata.clear();
        this.passwords = passwords.clone();
    }

    /**
     * @return
     */
    public String[] getPasswords() {
        return passwords.clone();
    }
}