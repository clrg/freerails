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
 * LegalTrackPieceProducer.java
 *
 */
package freerails.model.track;

import java.awt.*;

/**
 * Provides a method to get the eight rotations of a track template.
 * E.g. if the template is: 010 010 110 it returns: 010 001 100 010 110 111 110
 * 100 000 etc.
 */
class EightRotationsOfTrackPieceProducer {
    private EightRotationsOfTrackPieceProducer() {
    }

    /**
     * The method that returns the rotations.
     *
     * @param trackBlueprint A 9bit value that serves as the template.
     * @return An array of 8 9-bit values that have been generated by rotating
     * the template.
     */
    public static int[] getRotations(int trackBlueprint) {
        int trackTemplate = trackBlueprint;
        int[] derivedTrackPieces = new int[8];

        for (int i = 0; i < 8; i++) {
            derivedTrackPieces[i] = trackTemplate;

            boolean[][] trackBooleanArray = new boolean[3][3];

            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    if (((trackTemplate >> (3 * y + x)) & 1) == 1) {
                        trackBooleanArray[x][y] = true;
                    }
                }
            }

            boolean[][] trackTemplateBooleanArray = trackBooleanArray;
            // rotate track node clockwise
            Point[][] grabValueFrom = new Point[3][];
            grabValueFrom[0] = new Point[]{new Point(0, 1), new Point(0, 0), new Point(1, 0)};
            grabValueFrom[1] = new Point[]{new Point(0, 2), new Point(1, 1), new Point(2, 0)};
            grabValueFrom[2] = new Point[]{new Point(1, 2), new Point(2, 2), new Point(2, 1)};

            /*
             * I think there is a neater way of doing this, let me know if you know it! Luke
             */
            boolean[][] output = new boolean[3][3];

            for (int y1 = 0; y1 < 3; y1++) {
                for (int x1 = 0; x1 < 3; x1++) {
                    Point point = grabValueFrom[y1][x1];

                    /*
                     * y,x because of the way I defined grabValueFrom[][] above.
                     */
                    output[x1][y1] = trackTemplateBooleanArray[point.x][point.y];
                }
            }

            trackTemplateBooleanArray = output;
            int trackGraphicNumber = 0;

            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    if (trackTemplateBooleanArray[x][y]) {
                        trackGraphicNumber = trackGraphicNumber | (1 << (3 * y + x));
                    }
                }
            }

            trackTemplate = trackGraphicNumber;
        }

        return derivedTrackPieces;
    }

}