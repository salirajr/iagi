package com.rj.sysinvest.aparkost.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Data
public class LayoutProcessingImpl implements LayoutProcessing {

    private Font siteFont = new Font("Verdana", Font.BOLD, 24);
    private Color siteColor = Color.BLACK;
    private Point sitePoint = new Point(10, 30);
    private Font towerFont = new Font("Verdana", Font.BOLD, 24);
    private Color towerColor = Color.BLACK;
    private Point towerPoint = new Point(10, 60);
    private Font roomNumberFont = new Font("Verdana", Font.BOLD, 12);
    private Color roomNumberColor = Color.BLACK;
    private Color selectedRoomColor = new Color(255, 255, 0, 127);

    @Override
    public Image generateAparkostLayout(String siteName, String towerName, String level, String[] selectedRoomNumbers) {

        // Get Layout Template Image
        Image bg = getLayoutTemplate(siteName, towerName, level);
        int w = bg.getWidth(null), h = bg.getHeight(null);
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = img.createGraphics();
        g.drawImage(bg, 0, 0, null);

        // Draw string for Site Name
        g.setFont(siteFont);
        g.setColor(siteColor);
        g.drawString("Site: " + siteName, sitePoint.x, sitePoint.y);

        // Draw string for  Tower Name
        g.setFont(towerFont);
        g.setColor(towerColor);
        g.drawString("Tower: " + towerName, towerPoint.x, towerPoint.y);

        List<Room> roomList = getAllRooms(siteName, towerName, level);

        // Draw Filled polygon for All Selected Rooms
        List<String> selectedRoomNumberList = Arrays.asList(selectedRoomNumbers);
        roomList.stream().filter((Room r) -> {
            return selectedRoomNumberList.contains(r.getNumber());
        }).forEach((Room r) -> {
            g.setColor(selectedRoomColor);
            g.fill(r.getArea());
        });

        // Draw string for all room numbers
        roomList.forEach((Room room) -> {
            if (room.getArea() != null && room.getArea().npoints > 0) {
                int x = room.getArea().xpoints[0] + 3;
                int y = room.getArea().ypoints[0] + 3 + roomNumberFont.getSize();
                g.setColor(roomNumberColor);
                g.setFont(roomNumberFont);
                g.drawString(room.getNumber(), x, y);
            } else {
                throw new RuntimeException("No area defined for room " + room.getCode());
            }
        });

        g.dispose();

        return img;
    }

    private Image getLayoutTemplate(String siteName, String towerName, String level) {
        try {
            return ImageIO.read(new File("/home/is/tmp/rect3336.png"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private List<Room> getAllRooms(String siteName, String towerName, String level) {
        List<Room> list = new ArrayList();

        Room r = new Room();
        r.setCode("S1T1L0-001");
        r.setNumber("001");
        r.setLevel("Level 0");
        r.setTower("Tower 1");
        r.setSite("Site 1");
        r.setOwner("Rais");
        r.setGuest("Rahim");
        Polygon area = new Polygon();
        area.addPoint(70, 70);
        area.addPoint(140, 70);
        area.addPoint(140, 140);
        area.addPoint(70, 140);
        r.setArea(area);
        list.add(r);

        r = new Room();
        r.setCode("S1T1L0-009");
        r.setNumber("009");
        r.setLevel("Level 0");
        r.setTower("Tower 1");
        r.setSite("Site 1");
        r.setOwner("Rais");
        r.setGuest("Rahim");
        area = new Polygon();
        area.addPoint(150, 70);
        area.addPoint(220, 70);
        area.addPoint(220, 140);
        area.addPoint(150, 140);
        r.setArea(area);
        list.add(r);

        return list;
    }

    public static void main(String[] args) {
        Runnable r = () -> {
            LayoutProcessingImpl lp = new LayoutProcessingImpl();
            String siteName = "Site Name";
            String towerName = "Tower Name";
            String level = "Level";
            String[] rooms = new String[]{"001", "005", "009"};
            Image image = lp.generateAparkostLayout(siteName, towerName, level, rooms);

            JPanel gui = new JPanel();
            gui.add(new JLabel(new ImageIcon(image)));
            JOptionPane.showMessageDialog(null, gui);
        };
        SwingUtilities.invokeLater(r);
    }

    @Data
    public static class Room {

        private String code;
        private String number;
        private String level;
        private String tower;
        private String site;
        private String owner;
        private String guest;
        private Polygon area;
    }
}
