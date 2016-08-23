/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.aparkost.util;

/**
 *
 * @author salirajr
 */
public interface LayoutProcessing {

    /**
     * siteName + towerName => template Layout Image => know n of Rooms, put
     * layout title, draw room label, colored selected room
     */
    public byte[] generateAparkostLayout(String siteName, String towerName, int level, int[] selectedRoom);

}
