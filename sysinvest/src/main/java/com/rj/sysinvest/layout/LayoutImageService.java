package com.rj.sysinvest.layout;

import com.rj.sysinvest.model.Tower;
import java.util.List;

/**
 *
 * @author salirajr
 */
public interface LayoutImageService {

    /**
     * Generate layout images for each level in selected rooms. If selected
     * rooms contains multiple level, then it will generate multiple layout
     * images for each level.
     *
     * @param tower
     * @param listOfSelectedRoomId
     * @return list of layout per level
     */
    List<LayoutData> getLayoutImages(Tower tower, List<String> listOfSelectedRoomId);

    /**
     * Generate single layout image for selected level
     *
     * @param tower
     * @param selectedLevel
     * @param listOfSelectedRoomId
     * @return single layout image for selected level
     */
    LayoutData getLayoutImage(Tower tower, List<String> listOfSelectedRoomId, String selectedLevel);

}
