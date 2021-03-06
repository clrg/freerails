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

package freerails.model.train;

import freerails.util.ImmutableList;
import freerails.util.LineSegment;
import freerails.util.Pair;
import freerails.model.track.PathIterator;
import freerails.model.track.SimplePathIteratorImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO does not use any Train motion
/**
 * This <b>immutable</b> class represents the position of a train as a String
 * of points. There must be at least two points. The first point is the position
 * of the front of the train; the last point is the position of the end of the
 * train. Any intermediate points are positions of 'kinks' in the track.
 *
 * Coordinates are expressed in display coordinates relative to the map origin
 * (as opposed to map squares).
 *
 * Train positions can be combined and divided as illustrated below (notice what
 * happens to the head and tail that are combined)
 *
 * <table width="100%" border="0">
 * <caption>??</caption>
 * <tr>
 * <td>if</td>
 * <td>{@code a}</td>
 * <td>{@code =}</td>
 * <td><code>{<strong>(10, 10)</strong>, (20,20), (30,30), (40,40) }</code></td>
 * </tr>
 * <tr>
 * <td>and</td>
 * <td>{@code b}</td>
 * <td>{@code =}</td>
 * <td><code>{(1,1), (4,4), (5,5), <strong>(10, 10)</strong>}</code></td>
 * </tr>
 * <tr>
 * <td>then</td>
 * <td>{@code a.addToHead(b)}</td>
 * <td>{@code =}</td>
 * <td>{@code {(1,1), (4,4), (5,5), (20,20), (30,30), (40,40) }}</td>
 * </tr>
 * <tr>
 * <td>and</td>
 * <td>{@code b.addToTail(a)}</td>
 * <td>{@code =}</td>
 * <td>{@code {(1,1), (4,4), (5,5), (20,20), (30,30), (40,40) }}</td>
 * </tr>
 * <tr>
 * <td>and if</td>
 * <td>{@code c}</td>
 * <td>{@code =}</td>
 * <td>{@code {(1,1), (4,4), (5,5), (20,20), (30,30), (40,40) }}</td>
 * </tr>
 * <tr>
 * <td>then</td>
 * <td>{@code c.removeFromTail(a)}</td>
 * <td>{@code =}</td>
 * <td>{@code {(1,1), (4,4), (5,5), (10, 10)}}</td>
 * </tr>
 * <tr>
 * <td>and</td>
 * <td>{@code c.removeFromHead(b)}</td>
 * <td>{@code =}</td>
 * <td>{@code {(10, 10), (20,20), (30,30), (40,40) }}</td>
 * </tr>
 * </table>
 */
public class TrainPositionOnMap implements Serializable {

    private static final long serialVersionUID = 3979269144611010865L;
    private final ImmutableList<Integer> xpoints;
    private final ImmutableList<Integer> ypoints;
    private final double speed, acceleration;
    private final TrainState activity;

    private TrainPositionOnMap(Integer[] xs, Integer[] ys, double speed, double acceleration, TrainState activity) {
        if (xs.length != ys.length) {
            throw new IllegalArgumentException();
        }

        xpoints = new ImmutableList<>(xs);
        ypoints = new ImmutableList<>(ys);
        this.acceleration = acceleration;
        this.speed = speed;
        this.activity = activity;
    }

    /**
     * @param xpoints
     * @param ypoints
     * @return
     */
    public static TrainPositionOnMap createInstance(Integer[] xpoints, Integer[] ypoints) {
        return new TrainPositionOnMap(xpoints, ypoints, 0.0d, 0.0d, TrainState.READY);
    }

    /**
     * @param path
     * @return
     */
    public static TrainPositionOnMap createInSameDirectionAsPath(PathIterator path) {
        return createInSameDirectionAsPath(path, 0.0d, 0.0d, TrainState.READY);
    }

    /**
     * @param path
     * @param speed
     * @param acceleration
     * @param activity
     * @return
     */
    public static TrainPositionOnMap createInSameDirectionAsPathReversed(Pair<PathIterator, Integer> path, double speed, double acceleration, TrainState activity) {

        LineSegment line = new LineSegment();
        PathIterator pathIt = path.getA();
        int pathSize = path.getB();

        if (pathSize > 10000) {
            throw new IllegalStateException("The TrainPosition has more than 10,000 points, which suggests that something is wrong.");
        }
        Integer[] xPoints = new Integer[pathSize];
        Integer[] yPoints = new Integer[pathSize];

        for (int i = pathSize - 1; i > 0; i--) {
            if (!pathIt.hasNext()) {
                throw new IllegalStateException("Programming error at:" + i + " from:" + pathSize);
            }
            pathIt.nextSegment(line);
            xPoints[i] = line.getX1();
            yPoints[i] = line.getY1();
        }

        xPoints[0] = line.getX2();
        yPoints[0] = line.getY2();

        return new TrainPositionOnMap(xPoints, yPoints, speed, acceleration, activity);
    }

    /**
     * @param path
     * @param speed
     * @param acceleration
     * @param activity
     * @return
     */
    private static TrainPositionOnMap createInSameDirectionAsPath(PathIterator path, double speed, double acceleration, TrainState activity) {
        List<Integer> xPointsIntArray = new ArrayList<>();
        List<Integer> yPointsIntArray = new ArrayList<>();
        LineSegment line = new LineSegment();
        int i = 0;

        while (path.hasNext()) {
            path.nextSegment(line);
            xPointsIntArray.add(i, line.getX1());
            yPointsIntArray.add(i, line.getY1());
            i++;

            if (i > 10000) {
                throw new IllegalStateException("The TrainPosition has more than 10,000 points, which suggests that something is wrong.");
            }
        }

        xPointsIntArray.add(i, line.getX2());
        yPointsIntArray.add(i, line.getY2());

        Integer[] xPoints;
        Integer[] yPoints;

        xPoints = xPointsIntArray.toArray(new Integer[0]);
        yPoints = yPointsIntArray.toArray(new Integer[0]);

        return new TrainPositionOnMap(xPoints, yPoints, speed, acceleration, activity);
    }

    /**
     * @param a
     * @param b
     * @return
     */
    private static boolean headsAreEqual(TrainPositionOnMap a, TrainPositionOnMap b) {
        int aHeadX = a.getX(0);
        int aHeadY = a.getY(0);
        int bHeadX = b.getX(0);
        int bHeadY = b.getY(0);

        return aHeadX == bHeadX && aHeadY == bHeadY;
    }

    /**
     * @param a
     * @param b
     * @return
     */
    private static boolean tailsAreEqual(TrainPositionOnMap a, TrainPositionOnMap b) {
        int aTailX = a.getX(a.getLength() - 1);
        int aTailY = a.getY(a.getLength() - 1);
        int bTailX = b.getX(b.getLength() - 1);
        int bTailY = b.getY(b.getLength() - 1);

        return aTailX == bTailX && aTailY == bTailY;
    }

    /**
     * @param a
     * @param b
     * @return
     */
    private static boolean aHeadEqualsBTail(TrainPositionOnMap a, TrainPositionOnMap b) {
        int aHeadX = a.getX(0);
        int aHeadY = a.getY(0);

        int bTailX = b.getX(b.getLength() - 1);
        int bTailY = b.getY(b.getLength() - 1);

        return aHeadX == bTailX && aHeadY == bTailY;
    }

    /**
     * @return
     */
    public static boolean isCrashSite() {
        return false;
    }

    /**
     * @return
     */
    public static int getFrameCt() {
        return 1;
    }

    private static TrainPositionOnMap addBtoHeadOfA(TrainPositionOnMap b, TrainPositionOnMap a) {
        if (aHeadEqualsBTail(a, b)) {
            int newLength = a.getLength() + b.getLength() - 2;

            Integer[] newXpoints = new Integer[newLength];
            Integer[] newYpoints = new Integer[newLength];

            int aLength = a.getLength();
            int bLength = b.getLength();

            // First copy the points from B
            for (int i = 0; i < bLength - 1; i++) {
                newXpoints[i] = b.getX(i);
                newYpoints[i] = b.getY(i);
            }

            // Second copy the points from A.
            for (int i = 1; i < aLength; i++) {
                newXpoints[i + bLength - 2] = a.getX(i);
                newYpoints[i + bLength - 2] = a.getY(i);
            }

            return new TrainPositionOnMap(newXpoints, newYpoints, b.acceleration, b.speed, b.activity);
        }
        throw new IllegalArgumentException("Tried to add " + b.toString() + " to the head of " + a.toString());
    }

    @Override
    public int hashCode() {
        int result = 0;

        // TODO is there are danger of overflow here?
        for (int i = 0; i < xpoints.size(); i++) {
            result = 29 * result + xpoints.get(i);
        }

        for (int i = 0; i < ypoints.size(); i++) {
            result = 29 * result + ypoints.get(i);
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (obj instanceof TrainPositionOnMap) {
            TrainPositionOnMap other = (TrainPositionOnMap) obj;
            int thisLength = getLength();
            int otherLength = other.getLength();

            if (thisLength == otherLength) {
                PathIterator path1;
                PathIterator path2;
                LineSegment line1 = new LineSegment();
                LineSegment line2 = new LineSegment();

                path1 = other.path();
                path2 = path();

                while (path1.hasNext() && path2.hasNext()) {
                    path1.nextSegment(line1);
                    path2.nextSegment(line2);

                    if (line1.getX1() != line2.getX1() || line1.getY1() != line2.getY1() || line1.getX2() != line2.getX2() || line1.getY2() != line2.getY2()) {
                        return false;
                    }
                }

                return !path1.hasNext() && !path2.hasNext();
            }
            return false;
        }
        return false;
    }

    /**
     * @return
     */
    public int getLength() {
        return xpoints.size();
    }

    /**
     * @param position
     * @return
     */
    public int getX(int position) {
        return xpoints.get(position);
    }

    /**
     * @param position
     * @return
     */
    public int getY(int position) {
        return ypoints.get(position);
    }

    /**
     * @return
     */
    public PathIterator path() {
        return new SimplePathIteratorImpl(xpoints, ypoints);
    }

    /**
     * @return
     */
    public PathIterator reversePath() {
        int length = xpoints.size();
        Integer[] reversed_xpoints = new Integer[length];
        Integer[] reversed_ypoints = new Integer[length];

        for (int i = 0; i < length; i++) {
            reversed_xpoints[i] = xpoints.get(length - i - 1);
            reversed_ypoints[i] = ypoints.get(length - i - 1);
        }

        return new SimplePathIteratorImpl(reversed_xpoints, reversed_ypoints);
    }

    /**
     * @return
     */
    public TrainPositionOnMap reverse() {
        int length = xpoints.size();
        Integer[] reversed_xpoints = new Integer[length];
        Integer[] reversed_ypoints = new Integer[length];

        for (int i = 0; i < length; i++) {
            reversed_xpoints[i] = xpoints.get(length - i - 1);
            reversed_ypoints[i] = ypoints.get(length - i - 1);
        }

        return new TrainPositionOnMap(reversed_xpoints, reversed_ypoints, speed, acceleration, activity);
    }

    /**
     * @param b
     * @return
     */
    public TrainPositionOnMap addToHead(TrainPositionOnMap b) {
        TrainPositionOnMap a = this;

        return addBtoHeadOfA(b, a);
    }

    /**
     * @param b
     * @return
     */
    public boolean canAddToHead(TrainPositionOnMap b) {
        return aHeadEqualsBTail(this, b);
    }

    /**
     * @param a
     * @return
     */
    public TrainPositionOnMap addToTail(TrainPositionOnMap a) {
        TrainPositionOnMap b = this;

        return addBtoHeadOfA(b, a);
    }

    /**
     * @param b
     * @return
     */
    public boolean canAddToTail(TrainPositionOnMap b) {
        return aHeadEqualsBTail(b, this);
    }

    /**
     * @param b
     * @return
     */
    public boolean canRemoveFromHead(TrainPositionOnMap b) {
        if (headsAreEqual(this, b)) {
            PathIterator path = b.path();
            int i = 0;
            LineSegment line = new LineSegment();

            while (path.hasNext()) {
                path.nextSegment(line);

                if (getX(i) != line.getX1() || getY(i) != line.getY1()) {
                    return false;
                }

                i++;
            }

            return true;
        }
        return false;
    }

    /**
     * @param b
     * @return
     */
    public TrainPositionOnMap removeFromTail(TrainPositionOnMap b) {
        if (tailsAreEqual(this, b)) {
            int newLength = getLength() - b.getLength() + 2;

            Integer[] newXpoints = new Integer[newLength];
            Integer[] newYpoints = new Integer[newLength];

            // Copy from this
            for (int i = 0; i < newLength - 1; i++) {
                newXpoints[i] = getX(i);
                newYpoints[i] = getY(i);
            }

            // Copy tail from b
            newXpoints[newLength - 1] = b.getX(0);
            newYpoints[newLength - 1] = b.getY(0);

            return new TrainPositionOnMap(newXpoints, newYpoints, speed, acceleration, activity);
        }
        throw new IllegalArgumentException();
    }

    /**
     * @param b
     * @return
     */
    public boolean canRemoveFromTail(TrainPositionOnMap b) {
        if (tailsAreEqual(this, b)) {
            PathIterator path = b.reversePath();
            int i = getLength() - 1;
            LineSegment line = new LineSegment();

            while (path.hasNext()) {
                path.nextSegment(line);

                if (getX(i) != line.getX1() || getY(i) != line.getY1()) {
                    return false;
                }

                i--;
            }

            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TrainPosition {");

        for (int i = 0; i < xpoints.size(); i++) {
            sb.append('(');
            sb.append(xpoints.get(i));
            sb.append(", ");
            sb.append(ypoints.get(i));
            sb.append("), ");
        }

        sb.append('}');

        return sb.toString();
    }

    /**
     * @return
     */
    public double getSpeed() {
        return speed;
    }
}