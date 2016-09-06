package com.rj.sysinvest.layout;

import java.util.List;
import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Data
public class LayoutImageData {

    private String siteName;
    private String towerName;
    private String floor;
    private String imageType;
    private byte[] imageRaw;
    private List<String> selectedAparkostNames;
}
