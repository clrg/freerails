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
package freerails.model.train;

import freerails.model.world.SharedKey;
import freerails.model.world.PlayerKey;
import freerails.util.ImmutableList;
import freerails.util.Vector2D;
import freerails.model.*;
import freerails.model.cargo.CargoBatchBundle;
import freerails.model.cargo.ImmutableCargoBatchBundle;
import freerails.model.player.FreerailsPrincipal;
import freerails.model.station.Station;
import freerails.model.terrain.TileTransition;
import freerails.model.track.TrackSection;
import freerails.model.train.schedule.ImmutableSchedule;
import freerails.model.train.schedule.Schedule;
import freerails.model.world.ReadOnlyWorld;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Provides convenience methods to access the properties of a train from the world object.
 */
public class TrainAccessor {

    private final ReadOnlyWorld world;
    private final FreerailsPrincipal p;
    private final int id;

    /**
     * @param world
     * @param p
     * @param id
     */
    public TrainAccessor(final ReadOnlyWorld world, final FreerailsPrincipal p, final int id) {
        this.world = world;
        this.p = p;
        this.id = id;
    }

    /**
     * @param row
     * @param onTrain
     * @param consist
     * @return
     */
    public static ImmutableList<Integer> spaceAvailable2(ReadOnlyWorld row, CargoBatchBundle onTrain, ImmutableList<Integer> consist) {
        // This array will store the amount of space available on the train for
        // each cargo type.
        final int NUM_CARGO_TYPES = row.size(SharedKey.CargoTypes);
        Integer[] spaceAvailable = new Integer[NUM_CARGO_TYPES];
        Arrays.fill(spaceAvailable, 0);

        // First calculate the train's total capacity.
        for (int j = 0; j < consist.size(); j++) {
            int cargoType = consist.get(j);
            spaceAvailable[cargoType] += WagonType.UNITS_OF_CARGO_PER_WAGON;
        }

        for (int cargoType = 0; cargoType < NUM_CARGO_TYPES; cargoType++) {
            spaceAvailable[cargoType] = spaceAvailable[cargoType] - onTrain.getAmountOfType(cargoType);
        }
        return new ImmutableList<>(spaceAvailable);
    }

    /**
     * @param time
     * @return
     */
    public TrainState getStatus(double time) {
        TrainMotion tm = findCurrentMotion(time);
        return tm.getActivity();
    }

    /**
     * @return the id of the station the train is currently at, or -1 if no
     * current station.
     */
    public int getStationId(double time) {

        TrainMotion tm = findCurrentMotion(time);
        PositionOnTrack positionOnTrack = tm.getFinalPosition();
        Vector2D pp = positionOnTrack.getLocation();

        // loop through the station list to check if train is at the same Point2D as a station
        for (int i = 0; i < world.size(p, PlayerKey.Stations); i++) {
            Station tempPoint = (Station) world.get(p, PlayerKey.Stations, i);

            if (null != tempPoint && pp.equals(tempPoint.location)) {
                return i; // train is at the station at location tempPoint
            }
        }

        return -1;
    }

    /**
     * @param time
     * @param view
     * @return
     */
    public TrainPositionOnMap findPosition(double time, Rectangle view) {
        ActivityIterator ai = world.getActivities(p, id);

        // goto last
        ai.gotoLastActivity();
        // search backwards
        while (ai.getFinishTime() >= time && ai.hasPrevious()) {
            ai.previousActivity();
        }
        boolean afterFinish = ai.getFinishTime() < time;
        while (afterFinish && ai.hasNext()) {
            ai.nextActivity();
            afterFinish = ai.getFinishTime() < time;
        }
        double dt = time - ai.getStartTime();
        dt = Math.min(dt, ai.getDuration());
        TrainMotion tm = (TrainMotion) ai.getActivity();

        Vector2D start = tm.getPath().getStart();
        int trainLength = tm.getTrainLength();
        Rectangle trainBox = new Rectangle(start.x * WorldConstants.TILE_SIZE - trainLength * 2, start.y * WorldConstants.TILE_SIZE - trainLength * 2, trainLength * 4, trainLength * 4);
        if (!view.intersects(trainBox)) {
            return null; // TODO doesn't work
        }
        return tm.getStateAtTime(dt);
    }

    /**
     * @param time
     * @return
     */
    public TrainMotion findCurrentMotion(double time) {
        ActivityIterator ai = world.getActivities(p, id);
        boolean afterFinish = ai.getFinishTime() < time;
        if (afterFinish) {
            ai.gotoLastActivity();
        }
        return (TrainMotion) ai.getActivity();
    }

    /**
     * @return
     */
    public Train getTrain() {
        return (Train) world.get(p, PlayerKey.Trains, id);
    }

    /**
     * @return
     */
    public ImmutableSchedule getSchedule() {
        Train train = getTrain();
        return (ImmutableSchedule) world.get(p, PlayerKey.TrainSchedules, train.getScheduleID());
    }

    /**
     * @return
     */
    public CargoBatchBundle getCargoBundle() {
        Train train = getTrain();
        return (ImmutableCargoBatchBundle) world.get(p, PlayerKey.CargoBundles, train.getCargoBundleID());
    }

    /**
     * Returns true iff all the following hold.
     * <ol>
     * <li>The train is waiting for a full load at some station X.</li>
     * <li>The current train order tells the train to goto station X.</li>
     * <li>The current train order tells the train to wait for a full load.</li>
     * <li>The current train order specifies a consist that matches the train's
     * current consist.</li>
     * </ol>
     */
    public boolean keepWaiting() {
        double time = world.currentTime().getTicks();
        int stationId = getStationId(time);
        if (stationId == -1) return false;
        TrainState act = getStatus(time);
        if (act != TrainState.WAITING_FOR_FULL_LOAD) return false;
        ImmutableSchedule shedule = getSchedule();
        TrainOrders order = shedule.getOrder(shedule.getOrderToGoto());
        if (order.stationId != stationId) return false;
        if (!order.waitUntilFull) return false;
        Train train = getTrain();
        return order.getConsist().equals(train.getConsist());
    }

    /**
     * @return the location of the station the train is currently heading
     * towards.
     */
    public Vector2D getTargetLocation() {
        Train train = (Train) world.get(p, PlayerKey.Trains, id);
        int scheduleID = train.getScheduleID();
        Schedule schedule = (ImmutableSchedule) world.get(p, PlayerKey.TrainSchedules, scheduleID);
        int stationNumber = schedule.getStationToGoto();

        if (-1 == stationNumber) {
            // There are no stations on the schedule.
            return Vector2D.ZERO;
        }

        Station station = (Station) world.get(p, PlayerKey.Stations, stationNumber);
        return station.location;
    }

    /**
     * @param time
     * @return
     */
    public HashSet<TrackSection> occupiedTrackSection(double time) {
        TrainMotion tm = findCurrentMotion(time);
        PathOnTiles path = tm.getPath();
        HashSet<TrackSection> sections = new HashSet<>();
        Vector2D start = path.getStart();
        int x = start.x;
        int y = start.y;
        for (int i = 0; i < path.steps(); i++) {
            TileTransition s = path.getStep(i);
            Vector2D tile = new Vector2D(x, y);
            x += s.deltaX;
            y += s.deltaY;
            sections.add(new TrackSection(s, tile));
        }
        return sections;
    }

    /**
     * @param time
     * @return
     */
    public boolean isMoving(double time) {
        TrainMotion tm = findCurrentMotion(time);
        double speed = tm.getSpeedAtEnd();
        return speed != 0;
    }

    /**
     * The space available on the train measured in cargo units.
     */
    public ImmutableList<Integer> spaceAvailable() {

        Train train = (Train) world.get(p, PlayerKey.Trains, id);
        CargoBatchBundle bundleOnTrain = (ImmutableCargoBatchBundle) world.get(p, PlayerKey.CargoBundles, train.getCargoBundleID());
        return spaceAvailable2(world, bundleOnTrain, train.getConsist());
    }

}
