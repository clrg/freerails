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
package freerails.model.world;

import freerails.util.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * An implementation of World that only stores differences relative to an
 * underlying world object. Below is some stylised code showing what this class
 * does. The {@code key} object could be a location on the map, a
 * position in a list etc.
 *
 * {@code
 * HashMap underlyingWorldObject;
 *
 * HashMap differences;
 *
 * public void put(Object key, Object value) {
 * if (underlyingWorldObject.get(key).equals(value)) {
 * if (differences.containsKey(key)) {
 * differences.remove(key);
 * }
 * } else {
 * differences.put(key, value);
 * }
 * }
 *
 * public Object get(Object key) {
 * if (differences.containsKey(key)) {
 * return differences.get(key);
 * } else {
 * return underlyingWorldObject.get(key);
 * }
 * }
 * }
 *
 * The advantages of using an instance of this class instead of a copy of the
 * world object are:
 * <ol>
 * <li> Uses less memory.</li>
 * <li> Lets you pinpoint where differences on the map are, so you don't need to
 * check every tile. </li>
 * </ol>
 */
public class FullWorldDiffs extends FullWorld {

    private static final long serialVersionUID = -5993786533926919956L;
    private final SortedMap<ListKey, Object> listDiff;

    /**
     * Stores the differences on the map, Point2D are used as keys.
     */
    private final HashMap<Vector2D, Object> mapDiff;
    private final FullWorld underlying;

    /**
     * @param row
     */
    public FullWorldDiffs(ReadOnlyWorld row) {

        listDiff = new TreeMap<>();
        mapDiff = new HashMap<>();

        // Bit of a hack but it's not clear there is a better way, LL
        underlying = (FullWorld) row;

        activityLists = new List3DDiff<>(listDiff, underlying.activityLists, FullWorldDiffsListID.ACTIVITY_LISTS);
        bankAccounts = new List2DDiff<>(listDiff, underlying.bankAccounts, FullWorldDiffsListID.BANK_ACCOUNTS);
        currentBalance = new List1DDiff<>(listDiff, underlying.currentBalance, FullWorldDiffsListID.CURRENT_BALANCE);
        items = new List1DDiff<>(listDiff, underlying.items, FullWorldDiffsListID.ITEMS);
        lists = new List3DDiff<>(listDiff, underlying.lists, FullWorldDiffsListID.LISTS);
        players = new List1DDiff<>(listDiff, underlying.players, FullWorldDiffsListID.PLAYERS);
        sharedLists = new List2DDiff<>(listDiff, underlying.sharedLists, FullWorldDiffsListID.SHARED_LISTS);
        time = underlying.time;
    }

    /**
     * The iterator returns instances of java.awt.Point2D that store the
     * coordinates of tiles that are different to the underlying world object.
     */
    public Iterator<Vector2D> getMapDiffs() {
        return mapDiff.keySet().iterator();
    }

    /**
     * @return
     */
    public Iterator<ListKey> getListDiffs() {
        return listDiff.keySet().iterator();
    }

    /**
     * @param key
     * @return
     */
    public Object getDiff(ListKey key) {
        return listDiff.get(key);
    }

    @Override
    public Vector2D getMapSize() {
        return underlying.getMapSize();
    }

    @Override
    public Serializable getTile(Vector2D location) {
        if (mapDiff.containsKey(location)) {
            return (Serializable) mapDiff.get(location);
        }
        return underlying.getTile(location);
    }

    /**
     * Used by unit tests.
     */
    public int numberOfMapDifferences() {
        return mapDiff.size();
    }

    /**
     * Used by unit tests.
     */
    public int listDiffs() {
        return listDiff.size();
    }

    /**
     * After this method returns, all differences are cleared and calls to
     * methods on this object should produce the same results as calls the the
     * corresponding methods on the underlying world object.
     */
    public void reset() {
        time = underlying.currentTime();
        mapDiff.clear();
        listDiff.clear();
    }

    @Override
    public void setTile(Vector2D p, Serializable element) {

        if (Utils.equal(underlying.getTile(p), element)) {
            if (mapDiff.containsKey(p)) {
                mapDiff.remove(p);
            }
        } else {
            mapDiff.put(p, element);
        }
    }

    /**
     * @return
     */
    public ReadOnlyWorld getUnderlying() {
        return underlying;
    }

}
