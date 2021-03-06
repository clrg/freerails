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

package freerails.model.track;

import freerails.model.finances.Money;

import java.io.Serializable;

/**
 * Stores some of the properties of a track type.
 */
public class TrackRuleProperties implements Serializable {

    private static final long serialVersionUID = 3618704101752387641L;
    private final boolean enableDoubleTrack;
    private final Money maintenanceCost;
    private final Money price;
    private final Money fixedCost;
    private final TrackCategories category;
    // TODO rgbvalue part of the client, not part of the world model
    private final int rGBvalue;
    private final int stationRadius;
    private final String typeName;

    /**
     * @param rgb
     * @param doubleTrack
     * @param name
     * @param c
     * @param radius
     * @param price
     * @param maintenance
     * @param fixedCost
     */
    public TrackRuleProperties(int rgb, boolean doubleTrack, String name, TrackCategories c, int radius, int price, int maintenance, int fixedCost) {
        stationRadius = radius;
        rGBvalue = rgb;
        enableDoubleTrack = doubleTrack;
        typeName = name;
        category = c;
        this.price = new Money(price);
        maintenanceCost = new Money(maintenance);
        this.fixedCost = new Money(fixedCost);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TrackRuleProperties) {
            TrackRuleProperties test = (TrackRuleProperties) obj;

            return rGBvalue == test.rGBvalue && enableDoubleTrack == test.enableDoubleTrack && typeName.equals(test.typeName) && category == test.category && stationRadius == test.stationRadius;
        }
        return false;
    }

    /**
     * @return
     */
    public Money getMaintenanceCost() {
        return maintenanceCost;
    }

    /**
     * @return
     */
    public Money getPrice() {
        return price;
    }

    /**
     * @return
     */
    public int getStationRadius() {
        return stationRadius;
    }

    /**
     * @return
     */
    public String getTypeName() {
        return typeName;
    }

    @Override
    public int hashCode() {
        int result;
        result = rGBvalue;
        result = 29 * result + (enableDoubleTrack ? 1 : 0);
        result = 29 * result + typeName.hashCode();
        result = 29 * result + category.hashCode();
        result = 29 * result + stationRadius;
        result = 29 * result + price.hashCode();
        result = 29 * result + fixedCost.hashCode();
        result = 29 * result + maintenanceCost.hashCode();

        return result;
    }

    /**
     * @return
     */
    public boolean isEnableDoubleTrack() {
        return enableDoubleTrack;
    }

    /**
     * @return
     */
    public boolean isStation() {
        return category == TrackCategories.station;
    }

    /**
     * @return
     */
    public TrackCategories getCategory() {
        return category;
    }

    /**
     * @return
     */
    public Money getFixedCost() {
        return fixedCost;
    }
}