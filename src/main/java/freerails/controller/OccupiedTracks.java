/**
 * FreeRails 2 - A railroad strategy game Copyright (C) 2007 Roland Spatzenegger
 * (c@npg.net)
 * <p>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package freerails.controller;

import freerails.world.common.GameTime;
import freerails.world.player.FreerailsPrincipal;
import freerails.world.top.KEY;
import freerails.world.top.ReadOnlyWorld;
import freerails.world.track.TrackSection;
import freerails.world.train.TrainModel;

import java.util.*;

public class OccupiedTracks {

    public Map<TrackSection, Integer> occupiedTrackSections;
    public Map<Integer, List<TrackSection>> trainToTrackList;

    public OccupiedTracks(FreerailsPrincipal principal, ReadOnlyWorld w) {

        occupiedTrackSections = new HashMap<TrackSection, Integer>();
        trainToTrackList = new HashMap<Integer, List<TrackSection>>();

        for (int i = 0; i < w.size(principal, KEY.TRAINS); i++) {
            TrainModel train = (TrainModel) w.get(principal, KEY.TRAINS, i);
            if (null == train)
                continue;

            TrainAccessor ta = new TrainAccessor(w, principal, i);
            GameTime gt = w.currentTime();

            if (ta.isMoving(gt.getTicks())) {

                HashSet<TrackSection> sections = ta.occupiedTrackSection(gt
                        .getTicks());
                List<TrackSection> trackList = new ArrayList<TrackSection>(
                        sections);
                trainToTrackList.put(i, trackList);
                for (TrackSection section : sections) {
                    Integer count = occupiedTrackSections.get(section);
                    if (count == null) {
                        occupiedTrackSections.put(section, 1);
                    } else {
                        count++;
                        occupiedTrackSections.put(section, count);
                    }
                }
            }
        }
    }

    public void stopTrain(int i) {
        List<TrackSection> trackList = trainToTrackList.remove(i);
        if (trackList == null) {
            return; // already removed
        }
        for (TrackSection section : trackList) {
            Integer count = occupiedTrackSections.get(section);
            if (count > 0) {
                count--;
                occupiedTrackSections.put(section, count);
            }
        }
    }
}