package com.rj.sysinvest.layout;

import java.awt.Image;

/**
 *
 * @author salirajr
 */
public interface LayoutService {

    /**
     * siteName + towerName => template Layout Image => know n of Rooms => put
     * layout title, draw room label, colored selected room
     *
     * sample: http://stackoverflow.com/questions/2318020/merging-two-images
     *
     * @param towerId
     * @param level
     * @param selectedRooms
     * @return
     */
    public Image generateAparkostLayout(String towerId, String level, String[] selectedRooms);

}
