package com.rj.sysinvest.layout;

import java.util.List;
import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Data
public class LayoutData {

    private String siteId;
    private String towerId;
    private String level;
    private String imageType;
    private byte[] imageRaw;
    private List<String> selectedRooms;
}
