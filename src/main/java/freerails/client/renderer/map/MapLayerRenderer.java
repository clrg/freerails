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
 * MapView.java
 *
 */
package freerails.client.renderer.map;

import freerails.util.Vector2D;

import java.awt.*;

/**
 * Paints a layer of the map which might be buffered.
 */
public interface MapLayerRenderer {

    /**
     * @param g
     * @param tileLocation
     */
    void paintTile(Graphics g, Vector2D tileLocation);

    /**
     * @param tileLocation
     */
    void refreshTile(Vector2D tileLocation);

    /**
     *
     */
    void refreshAll();

    /**
     * @param g
     * @param visibleRect
     */
    void paintRect(Graphics g, Rectangle visibleRect);
}