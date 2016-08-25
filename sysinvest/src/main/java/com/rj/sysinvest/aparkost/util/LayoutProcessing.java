package com.rj.sysinvest.aparkost.util;

import java.awt.Image;

/**
 *
 * @author salirajr
 */
public interface LayoutProcessing {

    /**
     * siteName + towerName => template Layout Image => know n of Rooms => put
     * layout title, draw room label, colored selected room
     *
     * sample: http://stackoverflow.com/questions/2318020/merging-two-images
     *
     * @param siteName
     * @param towerName
     * @param level
     * @param selectedRoomNumbers
     * @return
     */
    public Image generateAparkostLayout(String siteName, String towerName, String level, String[] selectedRoomNumbers);

}
