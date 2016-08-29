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
import java.util.List;
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
    private Point sitePoint = new Point(10, 30);
    private Font towerFont = new Font("Verdana", Font.BOLD, 24);
    private Color towerColor = Color.BLACK;
    private Point towerPoint = new Point(10, 60);
    private Font levelFont = new Font("Verdana", Font.BOLD, 24);
    private Color levelColor = Color.BLACK;
    private Point levelPoint = new Point(10, 90);
    private Font roomNameFont = new Font("Verdana", Font.BOLD, 12);
    private Color roomNameColor = Color.BLACK;
    private Color highlightedRoomColor = new Color(255, 255, 0, 127);

    protected void drawOverlay(Graphics2D g, LayoutTemplateInfo layout, Tower tower, String level, List<String> selectedRooms) {

        // Draw string for Site Name
        g.setFont(siteFont);
        g.setColor(siteColor);
        g.drawString(tower.getSite().getName(), sitePoint.x, sitePoint.y);

        // Draw string for Tower Name
        g.setFont(towerFont);
        g.setColor(towerColor);
        g.drawString(tower.getName(), towerPoint.x, towerPoint.y);

        // Draw string for Level
        g.setFont(levelFont);
        g.setColor(levelColor);
        g.drawString(tower.getName(), levelPoint.x, levelPoint.y);

        List<Room> levelRooms = tower.getRooms().stream()
                // filter rooms by level
                .filter(room -> level.equals(room.getLevelId()))
                .collect(Collectors.toList());

        levelRooms.stream()
                // filter rooms by selectedRooms
                .filter(room -> selectedRooms.contains(room.getId()))
                // Draw filled polygon for All Selected Rooms  
                .forEach(room -> {
                    // get itLayoutRoomea from LayoutTemplateInfo
                    Optional<LayoutRoom> layoutRoom = layout.findLayoutRoomByPositionId(room.getPositionId());
                    if (layoutRoom.isPresent()) {
                        g.setColor(getHighlightedRoomColor());
                        g.fill(layoutRoom.get().toPolygon());
                    } else {
                        throw new RuntimeException("No area defined for room " + room.getId());
                    }
                });

        // Draw string room names for all room
        levelRooms.forEach(room -> {
            Optional<LayoutRoom> layoutRoom = layout.findLayoutRoomByPositionId(room.getPositionId());
            if (layoutRoom.isPresent()) {
                int[][] points = layoutRoom.get().getArea();
                if (points.length > 0) {
                    int x = points[0][0] + 3;
                    int y = points[0][1] + 3 + roomNameFont.getSize();
                    g.setColor(roomNameColor);
                    g.setFont(roomNameFont);
                    g.drawString(room.getName(), x, y);
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

    @Override
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
}
