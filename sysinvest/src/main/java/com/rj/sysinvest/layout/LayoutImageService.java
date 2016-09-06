package com.rj.sysinvest.layout;

import com.rj.sysinvest.model.Aparkost;
import com.rj.sysinvest.model.Tower;
import java.util.List;

/**
 *
 * @author salirajr
 */
public interface LayoutImageService {

    /**
     * Generate layout images for each tower and level in selected rooms. If
     * selected rooms contains multiple tower and/or level, then it will
     * generate multiple layout images for each tower and level.
     *
     * @param selectedAparkosts
     * @return list of layout per level
     */
    List<LayoutData> getLayoutImages(List<Aparkost> selectedAparkosts);

    /**
     * Generate layout images for each level in selected rooms. If selected
     * rooms contains multiple level, then it will generate multiple layout
     * images for each level.
     *
     * @param selectedTower
     * @param selectedAparkosts
     * @return list of layout per level
     */
    List<LayoutData> getLayoutImages(List<Aparkost> selectedAparkosts, Tower selectedTower);

    /**
     * Generate single layout image for selected level
     *
     * @param selectedAparkosts
     * @param selectedTower
     * @param selectedFloor
     * @return single layout image for selected level
     */
    LayoutData getLayoutImage(List<Aparkost> selectedAparkosts, Tower selectedTower, String selectedFloor);

}
