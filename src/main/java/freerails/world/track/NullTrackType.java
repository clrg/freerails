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
 * NullTrackType.java
 *
 */
package freerails.world.track;

import freerails.world.finances.Money;
import freerails.world.terrain.TerrainCategory;

import java.util.Iterator;

/**
 * The type of a Null track piece. TODO maybe it would be simpler to get rid of
 * this and just check against null!
 */
public final class NullTrackType implements TrackRule {

    /**
     *
     */
    public static final int NULL_TRACK_TYPE_RULE_NUMBER = -999;
    private static final long serialVersionUID = 3257849891614306614L;
    private static final NullTrackType nullTrackType = new NullTrackType();

    private NullTrackType() {
    }

    /**
     * @return
     */
    public static NullTrackType getInstance() {
        return nullTrackType;
    }

    private Object readResolve() {
        return nullTrackType;
    }

    /**
     * @param TerrainType
     * @return
     */
    public boolean canBuildOnThisTerrainType(TerrainCategory TerrainType) {
        return true; // No track is possible anywhere.
    }

    /**
     * @return
     */
    public String getTypeName() {
        return "NullTrackType";
    }

    /**
     * @param trackTemplateToTest
     * @return
     */
    public boolean testTrackPieceLegality(int trackTemplateToTest) {
        return trackTemplateToTest == 0;
    }

    /**
     * @param config
     * @return
     */
    public boolean trackPieceIsLegal(TrackConfiguration config) {
        return testTrackPieceLegality(config.getTrackGraphicsID());
    }

    /**
     * @return
     */
    public Iterator<TrackConfiguration> getLegalConfigurationsIterator() {
        throw new UnsupportedOperationException("Method not implemented yet!");
    }

    /**
     * @return
     */
    public boolean isStation() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        return o == this;
    }

    @Override
    public int hashCode() {
        return 666;
    }

    /**
     * @return
     */
    public int getStationRadius() {
        return 0;
    }

    /**
     * @return
     */
    public Money getPrice() {
        return new Money(0);
    }

    /**
     * @return
     */
    public Money getMaintenanceCost() {
        return new Money(0);
    }

    /**
     * @return
     */
    public TrackCategories getCategory() {
        return TrackCategories.non;
    }

    public int compareTo(TrackRule arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @return
     */
    public boolean isDouble() {

        return false;
    }

    /**
     * @return
     */
    public Money getFixedCost() {
        return Money.ZERO_MONEY;
    }
}