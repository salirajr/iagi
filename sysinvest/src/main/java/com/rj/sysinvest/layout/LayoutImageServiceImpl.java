package com.rj.sysinvest.layout;

import com.rj.sysinvest.dao.TowerRepository;
import com.rj.sysinvest.model.Tower;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Data
public class LayoutImageServiceImpl extends LayoutImageServiceAbstract {

    @Autowired
    private TowerRepository towerRepository;

    private String layoutImageFormat = "png";

    @Override
    public byte[] getLayoutImage(String towerId, String level, List<String> selectedRooms) {

        LayoutTemplateInfo layoutTemplateInfo = getLayoutTemplateInfo(towerId);

        BufferedImage img;
        try {
            img = ImageIO.read(getLayoutTemplateFile(layoutTemplateInfo));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Graphics2D g = img.createGraphics();
        g.drawImage(img, 0, 0, null);

        Tower tower = getTowerRepository().findOne(towerId);
        drawOverlay(g, layoutTemplateInfo, tower, level, selectedRooms);

        g.dispose();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, getLayoutImageFormat(), baos);
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
