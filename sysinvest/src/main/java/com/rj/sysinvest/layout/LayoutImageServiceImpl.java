package com.rj.sysinvest.layout;

import com.rj.sysinvest.model.Aparkost;
import com.rj.sysinvest.model.Tower;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
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
    public LayoutImageData getLayoutImage(List<Aparkost> selectedAparkosts, Tower selectedTower, String selectedFloor) {

        LayoutTemplateInfo layoutTemplateInfo = getLayoutTemplateInfo(selectedTower, selectedFloor);

        BufferedImage img;
        try {
            img = ImageIO.read(getLayoutTemplateFile(layoutTemplateInfo));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Graphics2D g = img.createGraphics();
        g.drawImage(img, 0, 0, null);

        drawOverlay(g, layoutTemplateInfo, selectedAparkosts, selectedTower, selectedFloor);
        g.dispose();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, layoutImageFormat, baos);
            byte[] bytes = baos.toByteArray();
            LayoutImageData layoutData = new LayoutImageData();
            layoutData.setImageType(layoutImageFormat);
            layoutData.setImageRaw(bytes);
            layoutData.setFloor(selectedFloor);
            layoutData.setTowerName(selectedTower.getName());
            layoutData.setSiteName(selectedTower.getSite().getName());
            layoutData.setSelectedAparkostNames(selectedAparkosts.stream()
                    .map(aparkost -> aparkost.getName())
                    .collect(Collectors.toList()));
            return layoutData;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
