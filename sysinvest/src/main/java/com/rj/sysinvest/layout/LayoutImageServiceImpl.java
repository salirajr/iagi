package com.rj.sysinvest.layout;

import com.rj.sysinvest.dao.AparkostRepository;
import com.rj.sysinvest.layout.LayoutTemplateInfo.LayoutRoom;
import com.rj.sysinvest.model.Aparkost;
import com.rj.sysinvest.model.Tower;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Component("LayoutImageServiceImpl")
@Data
public class LayoutImageServiceImpl implements LayoutImageService {

    @Resource
    private AparkostRepository aparkostRepository;
    @Resource
    private LayoutTemplateInfoRepository layoutRepo;

    private Font aparkostNameFont = new Font("Verdana", Font.BOLD, 12);
    private Color aparkostNameColor = Color.BLACK;
    private Font investorAccountIdFont = new Font("Verdana", Font.BOLD, 12);
    private Color investorAccountIdColor = Color.BLACK;
    private Color highlightedAparkostColor = new Color(255, 255, 0, 127);
    private String imageType = "png";

    @Override
    public LayoutImageData getLayoutImage(List<Aparkost> selectedAparkosts, Tower selectedTower, String selectedFloor) {

        LayoutTemplateInfo layoutTemplateInfo;
        try {
            layoutTemplateInfo = layoutRepo.get(selectedTower, selectedFloor);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        BufferedImage img;
        try {
            img = ImageIO.read(layoutRepo.getImageInputStream(layoutTemplateInfo));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Graphics2D g = img.createGraphics();
        g.drawImage(img, 0, 0, null);

        drawOverlay(g, layoutTemplateInfo, selectedAparkosts, selectedTower, selectedFloor);
        g.dispose();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, imageType, baos);
            byte[] bytes = baos.toByteArray();
            LayoutImageData layoutData = new LayoutImageData();
            layoutData.setImageType(imageType);
            layoutData.setImageRaw(bytes);
            layoutData.setFloor(selectedFloor);
            layoutData.setTowerName(selectedTower.getName());
            layoutData.setSiteName(selectedTower.getSite().getName());
            layoutData.setSelectedAparkostNames(selectedAparkosts.stream()
                    .map(Aparkost::getName)
                    .collect(Collectors.toList()));
            layoutData.setWidth(img.getWidth());
            layoutData.setHeight(img.getHeight());
            return layoutData;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<LayoutImageData> getLayoutImages(List<Aparkost> selectedAparkosts) {
        // generate map of tower - floor from selected aparkosts
        Map<Tower, Set<String>> selectedTowerFloors = new HashMap();
        selectedAparkosts
                .forEach(aparkost -> {
                    Tower tower = aparkost.getTower();
                    Set<String> floors = selectedTowerFloors.get(tower);
                    if (floors == null) {
                        selectedTowerFloors.put(tower, floors = new TreeSet());
                    }
                    floors.add(aparkost.getFloor());
                });
        // generate list of layoutImageData per tower's floor from selected aparkosts
        List<LayoutImageData> result = new ArrayList();
        selectedTowerFloors.forEach((tower, floorSet) -> {
            floorSet.forEach(floor -> {
                result.add(getLayoutImage(selectedAparkosts, tower, floor));
            });
        });
        return result;
    }

    /**
     * Draw investor account id and polygon on selected aparkosts, and draw name
     * for all
     *
     * @param g is Graphics2D
     * @param layout
     * @param listOfAparkost
     * @param selectedTower
     * @param selectedFloor
     */
    protected void drawOverlay(Graphics2D g, LayoutTemplateInfo layout, List<Aparkost> listOfAparkost, Tower selectedTower, String selectedFloor) {

        // Filter by selected tower and selected floor
        List<Aparkost> listOfApakostBySelectedTowerAndFloor = listOfAparkost.stream()
                .filter(aparkost -> selectedTower.equals(aparkost.getTower()))
                .filter(aparkost -> selectedFloor.equals(aparkost.getFloor()))
                .collect(Collectors.toList());

        // Draw colored polygon and investor name for selected aparkost
        listOfApakostBySelectedTowerAndFloor
                .forEach(aparkost -> {
                    // get LayoutRoom from LayoutTemplateInfo
                    LayoutRoom layoutRoom = layout.findLayoutRoomByIndex(aparkost.getIndex());
                    // Draw filled polygon 
                    Polygon polygon = layoutRoom.toPolygon();
                    g.setColor(getHighlightedAparkostColor());
                    g.fill(polygon);
                    // Draw string investor.accountId
                    Point p = layoutRoom.getPoint(0);
                    p.translate(3, 3 + 12 + 3 + 10);
                    g.setColor(investorAccountIdColor);
                    g.setFont(investorAccountIdFont);
                    g.drawString(aparkost.getInvestor().getAccountId(), p.x, p.y);
                });

        // Draw string aparkost.name for all aparkost in selected tower & floor
        aparkostRepository
                .findByTowerIdAndFloor(selectedTower.getId(), selectedFloor)
                .forEach(aparkost -> {
                    Point p = layout.findLayoutRoomByIndex(aparkost.getIndex())
                            .getPoint(0);
                    p.translate(3, 3 + aparkostNameFont.getSize());
                    g.setColor(aparkostNameColor);
                    g.setFont(aparkostNameFont);
                    g.drawString(aparkost.getName(), p.x, p.y);
                });
    }

}
