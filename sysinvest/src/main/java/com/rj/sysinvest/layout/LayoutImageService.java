package com.rj.sysinvest.layout;

import java.util.List;

/**
 *
 * @author salirajr
 */
public interface LayoutImageService {

    /**
     *
     * @param towerId
     * @param level
     * @param selectedRooms
     * @return
     */
    byte[] getLayoutImage(String towerId, String level, List<String> selectedRooms);

    LayoutTemplateInfo getLayoutTemplateInfo(String towerId);
}
