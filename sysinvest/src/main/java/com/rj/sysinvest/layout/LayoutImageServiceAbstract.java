package com.rj.sysinvest.layout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rj.sysinvest.layout.LayoutTemplateInfo.LayoutRoom;
import com.rj.sysinvest.model.Room;
import com.rj.sysinvest.model.Tower;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
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

    private Font siteFont = new Font("Verdana", Font.BOLD, 24);
    private Color siteColor = Color.BLACK;
    private Font towerFont = new Font("Verdana", Font.BOLD, 24);
    private Color towerColor = Color.BLACK;
    private Font levelFont = new Font("Verdana", Font.BOLD, 24);
    private Color levelColor = Color.BLACK;
    private Font roomNameFont = new Font("Verdana", Font.BOLD, 12);
    private Color roomNameColor = Color.BLACK;
    private Color highlightedRoomColor = new Color(255, 255, 0, 127);

    static Point createPoint(int[] xy) {
        return new Point(xy[0], xy[1]);
    }

    protected void drawOverlay(Graphics2D g, LayoutTemplateInfo layout, Tower tower, List<String> listOfSelectedRoomId, String selectedLevel) {

        // Draw string for Site Name
        g.setFont(siteFont);
        g.setColor(siteColor);
        Point sitePoint = createPoint(layout.getSitePoint());
        g.drawString(tower.getSite().getName(), sitePoint.x, sitePoint.y);

        // Draw string for Tower Name
        g.setFont(towerFont);
        g.setColor(towerColor);
        Point towerPoint = createPoint(layout.getTowerPoint());
        g.drawString(tower.getName(), towerPoint.x, towerPoint.y);

        // Draw string for Level
        g.setFont(levelFont);
        g.setColor(levelColor);
        Point levelPoint = createPoint(layout.getLevelPoint());
        g.drawString(tower.getName(), levelPoint.x, levelPoint.y);

        // collect all rooms filter by selectedLevel
        List<Room> listOfRoomBySelectedLevel = tower.getRooms().stream()
                .filter(room -> selectedLevel.equals(room.getLevelId()))
                .collect(Collectors.toList());

        listOfRoomBySelectedLevel.stream()
                // filter rooms by selectedRoomId
                .filter(room -> listOfSelectedRoomId.contains(room.getId()))
                // Draw filled polygon for All Selected Rooms  
                .forEach(room -> {
                    // get LayoutRoom from LayoutTemplateInfo
                    Optional<LayoutRoom> layoutRoom = layout.findLayoutRoomByPositionId(room.getPositionId());
                    if (layoutRoom.isPresent()) {
                        g.setColor(getHighlightedRoomColor());
                        g.fill(layoutRoom.get().toPolygon());
                    } else {
                        throw new RuntimeException("No area defined for room " + room.getId());
                    }
                });

        // Draw string room names for all room
        listOfRoomBySelectedLevel.forEach(room -> {
            Optional<LayoutRoom> layoutRoom = layout.findLayoutRoomByPositionId(room.getPositionId());
            if (layoutRoom.isPresent()) {
                int[][] points = layoutRoom.get().getArea();
                if (points.length > 0) {
                    Point p = createPoint(points[0]);
                    p.translate(3, 3 + roomNameFont.getSize());
                    g.setColor(roomNameColor);
                    g.setFont(roomNameFont);
                    g.drawString(room.getName(), p.x, p.y);
                } else {
                    throw new RuntimeException("No coordinates defined for room " + room.getId());
                }
            } else {
                throw new RuntimeException("No area defined for room " + room.getId());
            }
        });
    }

    @Value("${layoutTemplateDirectory}")
    private String layoutTemplateDirectory;

    public LayoutTemplateInfo getLayoutTemplateInfo(String towerId) {
        try {
            Path path = Paths.get(getLayoutTemplateDirectory(), towerId + ".json");
            return getObjectMapper().readValue(Files.newInputStream(path), LayoutTemplateInfo.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public File getLayoutTemplateFile(LayoutTemplateInfo layoutTemplateInfo) throws IOException {
        Path path = Paths.get(getLayoutTemplateDirectory(), layoutTemplateInfo.getTemplatePath());
        return path.toFile();
    }

    public InputStream getLayoutTemplateInputStream(LayoutTemplateInfo layoutTemplateInfo) throws IOException {
        Path path = Paths.get(getLayoutTemplateDirectory(), layoutTemplateInfo.getTemplatePath());
        return Files.newInputStream(path);
    }

    public byte[] getLayoutTemplateBytes(LayoutTemplateInfo layoutTemplateInfo) throws IOException {
        Path path = Paths.get(getLayoutTemplateDirectory(), layoutTemplateInfo.getTemplatePath());
        return Files.readAllBytes(path);
    }

//    @Override
//    public Map<String, byte[]> getLayoutImages(Tower tower, List<String> listOfSelectedRoomId) {
//        // Map<Level, List<RoomId>> mapOfLevelOfSelectedRooms
//        Map<String, List<String>> mapOfLevelOfSelectedRooms = new HashMap();
//        tower.getRooms().stream()
//                .filter(r -> listOfSelectedRoomId.contains(r.getId()))
//                .forEach(r -> {
//                    List<String> rooms = mapOfLevelOfSelectedRooms.get(r.getLevelId());
//                    if (rooms == null) {
//                        rooms = new ArrayList();
//                        mapOfLevelOfSelectedRooms.put(r.getLevelId(), rooms);
//                    }
//                    rooms.add(r.getId());
//                });
//        Map<String, byte[]> listOfLayoutOfLevel = new HashMap();
//        mapOfLevelOfSelectedRooms.forEach((level, selectedRooms) -> {
//            byte[] levelLayout = getLayoutImage(tower, selectedRooms, level);
//            listOfLayoutOfLevel.put(level, levelLayout);
//        });
//        return listOfLayoutOfLevel;
//    }
    @Override
    public List<LayoutData> getLayoutImages(Tower tower, List<String> listOfSelectedRoomId) {
        // Map<Level, List<RoomId>> mapOfLevelOfSelectedRooms
        Map<String, List<String>> mapOfLevelOfSelectedRooms = new HashMap();
        tower.getRooms().stream()
                .filter(r -> listOfSelectedRoomId.contains(r.getId()))
                .forEach(r -> {
                    List<String> rooms = mapOfLevelOfSelectedRooms.get(r.getLevelId());
                    if (rooms == null) {
                        rooms = new ArrayList();
                        mapOfLevelOfSelectedRooms.put(r.getLevelId(), rooms);
                    }
                    rooms.add(r.getId());
                });
        List<LayoutData> listOfLayoutOfLevel = new ArrayList();
        mapOfLevelOfSelectedRooms.forEach((level, selectedRooms) -> {
            listOfLayoutOfLevel.add(getLayoutImage(tower, selectedRooms, level));
        });
        return listOfLayoutOfLevel;
    }
}
