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

package freerails.model.terrain;

import freerails.util.Vector2D;
import freerails.model.world.ReadOnlyWorld;
import freerails.model.world.SharedKey;
import freerails.model.terrain.City;

import java.util.NoSuchElementException;

/**
 * Finds the nearest city and returns that name, so that a station can be
 * named appropriately.
 */
public class NearestCityFinder {

    private final Vector2D location;
    private final ReadOnlyWorld world;

    /**
     * @param world
     * @param location
     */
    public NearestCityFinder(ReadOnlyWorld world, Vector2D location) {
        this.world = world;
        this.location = location;
    }

    /**
     * @return
     */
    public String findNearestCity() {
        double cityDistance;
        String cityName;
        double tempDistance;
        City tempCity;

        if (world.size(SharedKey.Cities) > 0) {
            tempCity = (City) world.get(SharedKey.Cities, 0);
            cityDistance = getDistance(tempCity.getLocation());
            cityName = tempCity.getName();

            for (int i = 1; i < world.size(SharedKey.Cities); i++) {
                tempCity = (City) world.get(SharedKey.Cities, i);
                tempDistance = getDistance(tempCity.getLocation());

                if (tempDistance < cityDistance) {
                    cityDistance = tempDistance;
                    cityName = tempCity.getName();
                }
            }

            return cityName;
        }

        throw new NoSuchElementException();
    }

    private double getDistance(Vector2D cityLocation) {
        Vector2D delta = Vector2D.subtract(location, cityLocation);
        return delta.norm();
    }
}