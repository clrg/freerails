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
package freerails.model.game;

import java.io.Serializable;

// TODO possible range of values?, maybe split in two properties (boolean pause, speed)
/**
 * Represents actual game speed. If the game speed {@code speed}
 * is lesser then zero, game is paused. After unpausing, the speed should be
 * {@code -speed}.
 *
 * I.e. pausing/unpausing is equal to multiply the speed by -1.
 */
public class GameSpeed implements Serializable {

    private static final long serialVersionUID = 3257562901983081783L;
    private final int speed;

    /**
     * @param speed
     */
    public GameSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "GameSpeed:" + String.valueOf(speed);
    }

    /**
     * @return
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * @return
     */
    public boolean isPaused() {
        return speed < 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GameSpeed) {
            GameSpeed test = (GameSpeed) obj;

            return speed == test.speed;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return speed;
    }
}