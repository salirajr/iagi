package com.rj.sysinvest.layout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rj.sysinvest.dao.AparkostRepository;
import com.rj.sysinvest.layout.LayoutTemplateInfo.LayoutRoom;
import com.rj.sysinvest.model.Aparkost;
import com.rj.sysinvest.model.Tower;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
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
import java.util.Set;
import java.util.TreeSet;
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
    @Autowired
    private AparkostRepository aparkostRepository;

    private Font aparkostNameFont = new Font("Verdana", Font.BOLD, 12);
    private Color aparkostNameColor = Color.BLACK;
    private Font investorNameFont = new Font("Verdana", Font.BOLD, 12);
    private Color investorNameColor = Color.BLACK;
    private Color highlightedAparkostColor = new Color(255, 255, 0, 127);

    static Point createPoint(int[] xy) {
        return new Point(xy[0], xy[1]);
    }

    /**
     * Draw investor name and polygon on selected aparkosts, and draw name for all 
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
                    Optional<LayoutRoom> layoutRoom = layout.findLayoutRoomByIndex(aparkost.getIndex());
                    if (layoutRoom.isPresent()) {
                        // Draw filled polygon 
                        Polygon polygon = layoutRoom.get().toPolygon();
                        g.setColor(getHighlightedAparkostColor());
                        g.fill(polygon);
                        // Draw string investor.name
                        int[][] points = layoutRoom.get().getArea();
                        if (points.length > 0) {
                            Point p = createPoint(points[0]);
                            p.translate(3, 3 + 12 + 3 + 10);
                            g.setColor(investorNameColor);
                            g.setFont(investorNameFont);
                            g.drawString(aparkost.getInvestor().getNickName(), p.x, p.y);
                        } else {
                            throw new RuntimeException("No coordinates defined for aparkost " + aparkost.getName());
                        }
                    } else {
                        throw new RuntimeException("No area defined for aparkost " + aparkost.getName());
                    }
                });

        // Draw string aparkost.name for all aparkost in selected tower & floor
        aparkostRepository
                .findByTowerIdAndFloor(selectedTower.getId(), selectedFloor)
                .forEach(aparkost -> {
                    Optional<LayoutRoom> layoutRoom = layout.findLayoutRoomByIndex(aparkost.getIndex());
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

    public LayoutTemplateInfo getLayoutTemplateInfo(Tower tower, String floor) {
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
    public List<LayoutImageData> getLayoutImages(List<Aparkost> selectedAparkosts) {
        // generate map of tower - floor from selected aparkosts
        Map<Tower, Set<String>> selectedTowerFloors = new HashMap();
        selectedAparkosts.stream()
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
}
