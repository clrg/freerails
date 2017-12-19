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

/**
 *
 */
public enum CargoCategory {

    /**
     *
     */
    Mail(0),

    /**
     *
     */
    Passengers(1),

    /**
     *
     */
    Fast_Freight(2),

    /**
     *
     */
    Slow_Freight(3),

    /**
     *
     */
    Bulk_Freight(4);


    private final int id;

    CargoCategory(int id) {
        this.id = id;
    }

    /**
     * @param category_name
     * @return
     */
    public static CargoCategory getCategoryByName(String category_name) {
        for (CargoCategory category : values()) {
            if (category.toString().equals(category_name)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Category:" + category_name + " unknown.");
    }

    /**
     * @return
     */
    public static int getNumberOfCategories() {
        return values().length;
    }

    /**
     * @return
     */
    public int getID() {
        return id;
    }
}
