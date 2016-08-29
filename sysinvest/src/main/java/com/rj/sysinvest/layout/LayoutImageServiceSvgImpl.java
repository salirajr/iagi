package com.rj.sysinvest.layout;

import com.rj.sysinvest.dao.TowerRepository;
import com.rj.sysinvest.model.Tower;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.Data;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service
@Data
public class LayoutImageServiceSvgImpl extends LayoutImageServiceAbstract {

    @Autowired
    private TowerRepository towerRepository;

    @Override
    public byte[] getLayoutImage(String towerId, String level, List<String> selectedRooms) {

        LayoutTemplateInfo layoutTemplateInfo = getLayoutTemplateInfo(towerId);
        Document document;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(getLayoutTemplateFile(layoutTemplateInfo));
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            throw new RuntimeException(ex);
        }

        // Create an instance of the SVG Generator.
        SVGGraphics2D g = new SVGGraphics2D(document);

        Tower tower = getTowerRepository().findOne(towerId);
        drawOverlay(g, layoutTemplateInfo, tower, level, selectedRooms);

        g.dispose();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            g.stream(new OutputStreamWriter(baos));
            return baos.toByteArray();
        } catch (SVGGraphics2DIOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
