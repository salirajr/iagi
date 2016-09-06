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
    public LayoutData getLayoutImage(List<Aparkost> selectedAparkosts, Tower selectedTower, String selectedFloor) {

        LayoutTemplateInfo layoutTemplateInfo = getLayoutTemplateInfo(selectedTower);

        BufferedImage img;
        try {
            img = ImageIO.read(getLayoutTemplateFile(layoutTemplateInfo));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Graphics2D g = img.createGraphics();
        g.drawImage(img, 0, 0, null);

        drawOverlay2(g, layoutTemplateInfo, selectedAparkosts, selectedTower, selectedFloor);
        g.dispose();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, layoutImageFormat, baos);
            byte[] bytes = baos.toByteArray();
            LayoutData layoutImage = new LayoutData();
            layoutImage.setImageType(layoutImageFormat);
            layoutImage.setImageRaw(bytes);
            layoutImage.setLevel(selectedFloor);
            layoutImage.setTowerName(selectedTower.getName());
            layoutImage.setSiteName(selectedTower.getSite().getName());
            List<String> listOfAparkostId = selectedAparkosts.stream()
                    .map(aparkost -> aparkost.getFloor())
                    .collect(Collectors.toList());
            layoutImage.setSelectedRooms(listOfAparkostId);
            return layoutImage;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
