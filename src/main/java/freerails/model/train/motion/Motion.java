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
package freerails.model.train.motion;

import java.io.Serializable;

/**
 *
 */
public interface Motion extends Serializable {

    /**
     * @return The time taken to travel the distance given by getDistance().
     */
    double getTotalTime();

    /**
     * @return The distance traveled during at time given by getTime().
     */
    double getTotalDistance();

    /**
     * Returns the distance travelled at time t. The returned value, s,
     * satisfies the following conditions:
     * <ol>
     * <li>s &ge; 0</li>
     * <li>s &le; getDistance()</li>
     * <li>s = 0 if t = 0 </li>
     * <li>s = getDistance() if t = getTime()</li>
     * </ol>
     *
     * @return distance
     * @throws IllegalArgumentException if t &lt; 0 or t &gt; getTime()
     */
    double calculateDistanceAtTime(double time);

    /**
     * Returns the time taken to travel distance s. The returned value, t,
     * satisfies the following conditions:
     * <ol>
     * <li>t &ge; 0</li>
     * <li>t &le; getTime()</li>
     * <li>t = 0 if s = 0 </li>
     * <li>t = getTime() if s = getDistance()</li>
     * </ol>
     *
     * @return time
     * @throws IllegalArgumentException if s &lt; 0 or s &gt; getDistance()
     */
    double calculateTimeAtDistance(double distance);

    /**
     * @throws IllegalArgumentException if t &lt; 0 or t &gt; getTime()
     */
    double calculateSpeedAtTime(double time);

    /**
     * @throws IllegalArgumentException if t &lt; 0 or t &gt; getTime()
     */
    double calculateAccelerationAtTime(double time);
}