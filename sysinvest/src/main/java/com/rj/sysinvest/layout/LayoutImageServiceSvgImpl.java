package com.rj.sysinvest.layout;

import com.rj.sysinvest.model.Aparkost;
import com.rj.sysinvest.model.Tower;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.Data;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Component("LayoutImageServiceSvgImpl")
@Data
public class LayoutImageServiceSvgImpl extends LayoutImageServiceAbstract {

    @Override
    public LayoutImageData getLayoutImage(List<Aparkost> selectedAparkosts, Tower selectedTower, String selectedFloor) {

        LayoutTemplateInfo layoutTemplateInfo = getLayoutTemplateInfo(selectedTower, selectedFloor);
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

        drawOverlay(g, layoutTemplateInfo, selectedAparkosts, selectedTower, selectedFloor);

        g.dispose();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            g.stream(new OutputStreamWriter(baos));
            byte[] bytes = baos.toByteArray();
            LayoutImageData layoutImage = new LayoutImageData();
            layoutImage.setImageType("svg");
            layoutImage.setImageRaw(bytes);
            layoutImage.setFloor(selectedFloor);
            layoutImage.setTowerName(selectedTower.getName());
            layoutImage.setSiteName(selectedTower.getSite().getName());
            layoutImage.setSelectedAparkostNames(selectedAparkosts.stream()
                    .map(aparkost -> aparkost.getName())
                    .collect(Collectors.toList()));
            return layoutImage;
        } catch (SVGGraphics2DIOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
