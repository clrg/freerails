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
 * ProgressMonitorModel.java
 *
 */
package freerails.util.ui;

/**
 * This interface defines callbacks that can be used to let the user know how a
 * slow task is progressing.
 */
public interface ProgressMonitorModel {

    /**
     *
     */
    ProgressMonitorModel EMPTY = new ProgressMonitorModel() {};

    /**
     * @param i
     */
    default void setValue(int i) {}

    /**
     * @param max
     */
    default void nextStep(int max) {}

    /**
     *
     */
    default void finished() {}
}