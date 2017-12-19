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

package freerails.world.cargo;

import java.io.Serializable;

/**
 * Represents a type of cargo.
 */
final public class CargoType implements Serializable {
    private static final long serialVersionUID = 3834874680581369912L;
    private final CargoCategory category;
    private final String name;
    private final int unitWeight;

    /**
     * @param weight
     * @param s
     * @param cat
     */
    public CargoType(int weight, String s, CargoCategory cat) {
        unitWeight = weight;
        category = cat;
        name = s;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CargoType))
            return false;
        CargoType other = (CargoType) obj;
        return other.unitWeight == this.unitWeight && other.name.equals(name)
                && other.category.equals(category);
    }

    /**
     * @return
     */
    public CargoCategory getCategory() {
        return category;
    }

    /**
     * Returns the name, replacing any underscores with spaces.
     *
     * @return
     */
    public String getDisplayName() {
        return this.name.replace('_', ' ');
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public int getUnitWeight() {
        return unitWeight;
    }

    @Override
    public int hashCode() {

        int result;
        result = unitWeight;
        result = 29 * result + category.hashCode();
        result = 29 * result + name.hashCode();
        return result;

    }

    @Override
    public String toString() {
        return name;
    }

}