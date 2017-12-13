/*
 * Created on Sep 10, 2004
 *
 */
package jfreerails.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Vector;
import jfreerails.move.MapDiffMove;
import jfreerails.move.TimeTickMove;
import jfreerails.network.MoveReceiver;
import jfreerails.network.ServerGameModel;
import jfreerails.world.common.GameCalendar;
import jfreerails.world.common.GameSpeed;
import jfreerails.world.common.GameTime;
import jfreerails.world.player.FreerailsPrincipal;
import jfreerails.world.player.Player;
import jfreerails.world.top.ITEM;
import jfreerails.world.top.KEY;
import jfreerails.world.top.World;
import jfreerails.world.top.WorldDifferences;


/**
 * @author Luke
 *
 */
public class ServerGameModelImpl implements ServerGameModel {
    public World world;
    private transient CalcSupplyAtStations calcSupplyAtStations;
    private TrainBuilder tb;
    private final ArrayList trainMovers;

    /**
     * List of the ServerAutomaton objects connected to this game.
     */
    private final Vector serverAutomata;

    /**
     * Number of ticks since the last time we did an infrequent update.
     */
    private int ticksSinceUpdate = 0;
    private transient long nextModelUpdateDue;
    private transient MoveReceiver moveExecuter;

    public ServerGameModelImpl() {
        this(new ArrayList(), null, new Vector());
    }

    public ServerGameModelImpl(ArrayList trainMovers, World w,
        Vector serverAutomata) {
        this.world = w;
        this.serverAutomata = serverAutomata;
        this.trainMovers = trainMovers;
        nextModelUpdateDue = System.currentTimeMillis();
    }

    public void listUpdated(KEY key, int index, FreerailsPrincipal principal) {
        calcSupplyAtStations.listUpdated(key, index, principal);
    }

    public void itemAdded(KEY key, int index, FreerailsPrincipal principal) {
        calcSupplyAtStations.itemAdded(key, index, principal);
    }

    public void itemRemoved(KEY key, int index, FreerailsPrincipal principal) {
        calcSupplyAtStations.itemRemoved(key, index, principal);
    }

    /** This is called on the last tick of each year. */
    private void yearEnd() {
        TrackMaintenanceMoveGenerator tmmg = new TrackMaintenanceMoveGenerator(moveExecuter);
        tmmg.update(world);

        TrainMaintenanceMoveGenerator trainMaintenanceMoveGenerator = new TrainMaintenanceMoveGenerator(moveExecuter);
        trainMaintenanceMoveGenerator.update(world);

        InterestChargeMoveGenerator interestChargeMoveGenerator = new InterestChargeMoveGenerator(moveExecuter);
        interestChargeMoveGenerator.update(world);

        //Grow cities.
        WorldDifferences wd = new WorldDifferences(world);
        NewCityTilePositioner ctp = new NewCityTilePositioner(wd);
        ctp.growCities();

        MapDiffMove move = new MapDiffMove(world, wd);
        moveExecuter.processMove(move);
    }

    /** This is called at the start of each new month. */
    private void monthEnd() {
        calcSupplyAtStations.doProcessing();

        CargoAtStationsGenerator cargoAtStationsGenerator = new CargoAtStationsGenerator();
        cargoAtStationsGenerator.update(world, moveExecuter);
    }

    private void updateGameTime() {
        moveExecuter.processMove(TimeTickMove.getMove(world));
    }

    /**

     */
    public synchronized void update() {
        long frameStartTime = System.currentTimeMillis();

        while (nextModelUpdateDue <= frameStartTime) {
            /* First do the things that need doing whether or not the game is paused.*/
            tb.buildTrains(world);

            int gameSpeed = ((GameSpeed)world.get(ITEM.GAME_SPEED)).getSpeed();

            if (gameSpeed > 0) {
                /* Update the time first, since other updates might need
                to know the current time.*/
                updateGameTime();

                //now do the other updates
                tb.moveTrains(world);

                //Check whether we are about to start a new year..
                GameTime time = (GameTime)world.get(ITEM.TIME);
                GameCalendar calendar = (GameCalendar)world.get(ITEM.CALENDAR);
                int yearNextTick = calendar.getYear(time.getTime() + 1);
                int yearThisTick = calendar.getYear(time.getTime());

                if (yearThisTick != yearNextTick) {
                    yearEnd();
                }

                //And a new month..
                int monthThisTick = calendar.getMonth(time.getTime());
                int monthNextTick = calendar.getMonth(time.getTime() + 1);

                if (monthNextTick != monthThisTick) {
                    monthEnd();
                }

                /* calculate "ideal world" time for next tick */
                nextModelUpdateDue = nextModelUpdateDue + (1000 / gameSpeed);

                //            int delay = (int)(nextModelUpdateDue - frameStartTime);
                //
                //            /* wake up any waiting client threads - we could be
                //             * more agressive, and only notify them if delay > 0? */
                //            this.notifyAll();
                //
                //            try {
                //                if (delay > 0) {
                //                    this.wait(delay);
                //                } else {
                //                    this.wait(1);
                //                }
                //            } catch (InterruptedException e) {
                //                // do nothing
                //            }
                ticksSinceUpdate++;
            } else {
                //            try {
                //                //When the game is frozen we don't want to be spinning in a
                //                //loop.
                //                Thread.sleep(200);
                //            } catch (InterruptedException e) {
                //                // do nothing
                //            }
                nextModelUpdateDue = System.currentTimeMillis();
            }
        }
    }

    public void write(ObjectOutputStream objectOut) throws IOException {
        objectOut.writeObject(tb.getTrainMovers());
        objectOut.writeObject(world);
        objectOut.writeObject(serverAutomata);

        /**
         * save player private data
         */
        for (int i = 0; i < world.getNumberOfPlayers(); i++) {
            Player player = world.getPlayer(i);
            player.saveSession(objectOut);
        }
    }

    public void init(MoveReceiver moveExecuter) {
        this.moveExecuter = moveExecuter;
        tb = new TrainBuilder(moveExecuter, trainMovers);
        calcSupplyAtStations = new CalcSupplyAtStations(world, moveExecuter);

        for (int i = 0; i < serverAutomata.size(); i++) {
            ((ServerAutomaton)serverAutomata.get(i)).initAutomaton(moveExecuter);
        }

        tb.initAutomaton(moveExecuter);
        nextModelUpdateDue = System.currentTimeMillis();
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
        this.trainMovers.clear();
        this.serverAutomata.clear();
    }
}