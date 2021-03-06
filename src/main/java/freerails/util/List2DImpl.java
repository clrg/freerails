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
package freerails.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T>
 */
public class List2DImpl<T> implements List2D<T> {

    private static final long serialVersionUID = 7614246212629595959L;
    private final List<ArrayList<T>> elementData = new ArrayList<>();

    /**
     * @param d1
     */
    public List2DImpl(int d1) {
        for (int i = 0; i < d1; i++) {
            elementData.add(new ArrayList<>());
        }
    }

    /**
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(List2D a, List2D b) {
        if (a.sizeD1() != b.sizeD1()) return false;
        for (int d1 = 0; d1 < a.sizeD1(); d1++) {
            if (a.sizeD2(d1) != b.sizeD2(d1)) return false;
            for (int d2 = 0; d2 < a.sizeD2(d1); d2++) {
                if (!Utils.equal(a.get(d1, d2), b.get(d1, d2))) return false;
            }
        }
        return true;
    }

    /**
     * @return
     */
    public int sizeD1() {
        return elementData.size();
    }

    /**
     * @param d1
     * @return
     */
    public int sizeD2(int d1) {
        return elementData.get(d1).size();
    }

    /**
     * @param d1
     * @param d2
     * @return
     */
    public T get(int d1, int d2) {
        return elementData.get(d1).get(d2);
    }

    /**
     * @param d1
     * @return
     */
    public T removeLastD2(int d1) {

        int last = elementData.get(d1).size() - 1;
        T element = elementData.get(d1).get(last);
        elementData.get(d1).remove(last);
        return element;
    }

    /**
     * @return
     */
    public int removeLastD1() {
        int last = elementData.size() - 1;
        if (sizeD2(last) != 0) throw new IllegalStateException(String.valueOf(last));
        elementData.remove(last);
        return last;
    }

    /**
     * @return
     */
    public int addD1() {
        elementData.add(new ArrayList<>());
        return elementData.size() - 1;
    }

    /**
     * @param d1
     * @param element
     * @return
     */
    public int addD2(int d1, T element) {
        ArrayList<T> d2 = elementData.get(d1);
        int index = d2.size();
        d2.add(element);
        return index;
    }

    /**
     * @param d1
     * @param d2
     * @param element
     */
    public void set(int d1, int d2, T element) {
        elementData.get(d1).set(d2, element);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof List2D && equals(this, (List2D) obj);
    }

    @Override
    public int hashCode() {
        return sizeD1();
    }

}
