package com.rj.sysinvest.layout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rj.sysinvest.layout.LayoutTemplateInfo.LayoutRoom;
import com.rj.sysinvest.model.Aparkost;
import com.rj.sysinvest.model.Tower;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Data
public abstract class LayoutImageServiceAbstract implements LayoutImageService {

    @Autowired
    private ObjectMapper objectMapper;

    private Font aparkostNameFont = new Font("Verdana", Font.BOLD, 12);
    private Color aparkostNameColor = Color.BLACK;
    private Color highlightedAparkostColor = new Color(255, 255, 0, 127);

    static Point createPoint(int[] xy) {
        return new Point(xy[0], xy[1]);
    }

    /**
     *
     * @param g is Graphics2D
     * @param layout
     * @param listOfAparkost
     * @param selectedTower
     * @param selectedFloor
     */
    protected void drawOverlay2(Graphics2D g, LayoutTemplateInfo layout, List<Aparkost> listOfAparkost, Tower selectedTower, String selectedFloor) {

        // Filter by selected tower and selected floor
        List<Aparkost> listOfApakostBySelectedTowerAndFloor = listOfAparkost.stream()
                .filter(aparkost -> aparkost.getTower().equals(selectedTower))
                .filter(aparkost -> aparkost.getFloor().equals(selectedFloor))
                .collect(Collectors.toList());

        // Draw highlighted area for selected aparkost
        listOfApakostBySelectedTowerAndFloor
                // Draw filled polygon for All Selected Aparkost  
                .forEach(aparkost -> {
                    // get LayoutRoom from LayoutTemplateInfo
                    Optional<LayoutRoom> layoutRoom = layout.findLayoutRoomByPositionId(aparkost.getIndex());
                    if (layoutRoom.isPresent()) {
                        g.setColor(getHighlightedAparkostColor());
                        g.fill(layoutRoom.get().toPolygon());
                    } else {
                        throw new RuntimeException("No area defined for aparkost " + aparkost.getName());
                    }
                });

        // Draw string room names for all room
        listOfApakostBySelectedTowerAndFloor.forEach(aparkost -> {
            Optional<LayoutRoom> layoutRoom = layout.findLayoutRoomByPositionId(aparkost.getIndex());
            if (layoutRoom.isPresent()) {
                int[][] points = layoutRoom.get().getArea();
                if (points.length > 0) {
                    Point p = createPoint(points[0]);
                    p.translate(3, 3 + aparkostNameFont.getSize());
                    g.setColor(aparkostNameColor);
                    g.setFont(aparkostNameFont);
                    g.drawString(aparkost.getName(), p.x, p.y);
                } else {
                    throw new RuntimeException("No coordinates defined for aparkost " + aparkost.getName());
                }
            } else {
                throw new RuntimeException("No area defined for aparkost " + aparkost.getName());
            }
        });
    }

    @Value("${layoutTemplateDirectory}")
    private String layoutTemplateDirectory;

    public LayoutTemplateInfo getLayoutTemplateInfo(Tower tower) {
        try {
            String fileName = tower.getSite().getName() + "_" + tower.getName() + ".json";
            Path path = Paths.get(layoutTemplateDirectory, fileName);
            return getObjectMapper().readValue(Files.newInputStream(path), LayoutTemplateInfo.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public File getLayoutTemplateFile(LayoutTemplateInfo layoutTemplateInfo) throws IOException {
        return Paths.get(layoutTemplateDirectory, layoutTemplateInfo.getTemplatePath()).toFile();
    }

    public InputStream getLayoutTemplateInputStream(LayoutTemplateInfo layoutTemplateInfo) throws IOException {
        return new FileInputStream(getLayoutTemplateFile(layoutTemplateInfo));
    }

    public byte[] getLayoutTemplateBytes(LayoutTemplateInfo layoutTemplateInfo) throws IOException {
        return Files.readAllBytes(getLayoutTemplateFile(layoutTemplateInfo).toPath());
    }

    @Override
    public List<LayoutData> getLayoutImages(List<Aparkost> selectedAparkosts, Tower selectedTower) {
        Map<String, List<Aparkost>> mapOfSelectedAparkostByLevel = new HashMap();
        selectedAparkosts.stream()
                .filter(aparkost -> selectedTower.equals(aparkost.getTower()))
                .forEach(aparkost -> {
                    List<Aparkost> rooms = mapOfSelectedAparkostByLevel.get(aparkost.getFloor());
                    if (rooms == null) {
                        rooms = new ArrayList();
                        mapOfSelectedAparkostByLevel.put(aparkost.getFloor(), rooms);
                    }
                    rooms.add(aparkost);
                });
        List<LayoutData> result = new ArrayList();
        mapOfSelectedAparkostByLevel.forEach((floor, aparkosts) -> {
            result.add(getLayoutImage(aparkosts, selectedTower, floor));
        });
        return result;
    }

    @Override
    public List<LayoutData> getLayoutImages(List<Aparkost> selectedAparkosts) {
        Map<Tower, List<Aparkost>> mapOfSelectedAparkostByTower = new HashMap();
        selectedAparkosts.stream()
                .forEach(aparkost -> {
                    Tower tower = aparkost.getTower();
                    List<Aparkost> aparkosts = mapOfSelectedAparkostByTower.get(tower);
                    if (aparkosts == null) {
                        aparkosts = new ArrayList();
                        mapOfSelectedAparkostByTower.put(tower, aparkosts);
                    }
                    aparkosts.add(aparkost);
                });
        List<LayoutData> result = new ArrayList();
        mapOfSelectedAparkostByTower.forEach((tower, aparkosts) -> {
            result.addAll(getLayoutImages(aparkosts, tower));
        });
        return result;
    }
}
