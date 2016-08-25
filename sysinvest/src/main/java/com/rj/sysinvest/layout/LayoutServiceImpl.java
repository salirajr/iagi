package com.rj.sysinvest.layout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rj.sysinvest.dao.TowerRepository;
import com.rj.sysinvest.model.Room;
import com.rj.sysinvest.model.Tower;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Data
public class LayoutServiceImpl implements LayoutService {

    public static void main(String[] args) {
        Runnable r = () -> {
            LayoutServiceImpl lp = new LayoutServiceImpl();
            String siteName = "Site Name";
            String towerId = "Tower Name";
            String level = "Level 1";
            String[] selectedRooms = new String[]{"001", "005", "009"};
            Image image = lp.generateAparkostLayout(towerId, level, selectedRooms);

            JPanel gui = new JPanel();
            gui.add(new JLabel(new ImageIcon(image)));
            JOptionPane.showMessageDialog(null, gui);
        };
        SwingUtilities.invokeLater(r);
    }
    @Autowired
    private ObjectMapper jacksonObjectMapper;
    @Autowired
    private TowerRepository towerRepository;

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
    public Image generateAparkostLayout(String towerId, String level, String[] selectedRooms) {

        Tower tower = towerRepository.findOne(towerId);//getTower(towerId);

        Image bg;
        try {
            byte[] raw = tower.getLayoutTemplateRaw();
            InputStream inputStream = new ByteArrayInputStream(raw);
            bg = ImageIO.read(inputStream);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        int w = bg.getWidth(null), h = bg.getHeight(null);
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = img.createGraphics();
        g.drawImage(bg, 0, 0, null);

        // Draw string for Site Name
        g.setFont(siteFont);
        g.setColor(siteColor);
        g.drawString("Site: " + tower.getSiteName(), sitePoint.x, sitePoint.y);

        // Draw string for  Tower Name
        g.setFont(towerFont);
        g.setColor(towerColor);
        g.drawString("Tower: " + tower.getName(), towerPoint.x, towerPoint.y);

        List<Room> roomList = tower.getRooms();

        // Draw Filled polygon for All Selected Rooms
        List<String> selectedRoomNumberList = Arrays.asList(selectedRooms);
        roomList.stream().filter((Room r) -> {
            return selectedRoomNumberList.contains(r.getName());
        }).forEach((Room r) -> {
            g.setColor(selectedRoomColor);
            try {
                g.fill(parseAreaJson(r.getAreaAsJsonArray()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Draw string for all room numbers
        roomList.forEach((Room room) -> {
            Polygon area = null;
            try {
                area = parseAreaJson(room.getAreaAsJsonArray());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            if (area != null && area.npoints > 0) {
                int x = area.xpoints[0] + 3;
                int y = area.ypoints[0] + 3 + roomNumberFont.getSize();
                g.setColor(roomNumberColor);
                g.setFont(roomNumberFont);
                g.drawString(room.getName(), x, y);
            } else {
                throw new RuntimeException("No area defined for room " + room.getId());
            }
        });

        g.dispose();

        return img;
    }

    /**
     *
     * @param json [[x,y],[x,y],[x,y],[x,y],[x,y],[x,y]...[x,y]]
     * @return Polygon
     * @throws IOException
     */
    private Polygon parseAreaJson(String json) throws IOException {
        int[][] points = jacksonObjectMapper.readValue(json, int[][].class);
        Polygon polygon = new Polygon();
        for (int[] xy : points) {
            int x = xy[0], y = xy[1];
            polygon.addPoint(x, y);
        }
        return polygon;
    }

}
