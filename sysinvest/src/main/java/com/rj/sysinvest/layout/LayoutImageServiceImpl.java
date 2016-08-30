package com.rj.sysinvest.layout;

import com.rj.sysinvest.model.Tower;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Component("LayoutImageServiceImpl")
@Data
public class LayoutImageServiceImpl extends LayoutImageServiceAbstract {

    private String layoutImageFormat = "png";

    @Override
    public LayoutData getLayoutImage(Tower tower, List<String> selectedRooms, String level) {

        LayoutTemplateInfo layoutTemplateInfo = getLayoutTemplateInfo(tower.getId());

        BufferedImage img;
        try {
            img = ImageIO.read(getLayoutTemplateFile(layoutTemplateInfo));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Graphics2D g = img.createGraphics();
        g.drawImage(img, 0, 0, null);

        drawOverlay(g, layoutTemplateInfo, tower, selectedRooms, level);

        g.dispose();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, getLayoutImageFormat(), baos);
            byte[] bytes = baos.toByteArray();
            LayoutData layoutImage = new LayoutData();
            layoutImage.setImageType(layoutImageFormat);
            layoutImage.setImageRaw(bytes);
            layoutImage.setLevel(level);
            layoutImage.setTowerId(tower.getId());
            layoutImage.setSiteId(tower.getSite().getId());
            layoutImage.setSelectedRooms(selectedRooms);
            return layoutImage;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
