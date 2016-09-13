package com.rj.sysinvest.akad.docx;

import com.rj.sysinvest.akad.AkadFormData;
import com.rj.sysinvest.akad.AkadFormDataMapper;
import com.rj.sysinvest.akad.LampiranPembayaranData;
import com.rj.sysinvest.akad.LampiranPembayaranData.Detail;
import com.rj.sysinvest.akad.LampiranPembayaranDataService;
import com.rj.sysinvest.layout.LayoutImageData;
import com.rj.sysinvest.layout.LayoutImageService;
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Aparkost;
import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Data
public class AkadDocxService {

    @Resource
    private DocxComponent docxComp;
    @Resource
    private AkadFormDataMapper dataMapper;
    @Resource
    private LayoutImageService layoutImageService;
    @Resource
    private LampiranPembayaranDataService pembayaranDataMapper;
    //
    private String templatePath = "template/akad-form.docx";

    public byte[] generateAkadDocx(Acquisition acquisition) throws IOException {
        // load akad template
        XWPFDocument doc = docxComp.load(templatePath);

        // fill akad form
        AkadFormData data = dataMapper.apply(acquisition);
        try {
            Map<String, String> map = BeanUtils.describe(data);
            docxComp.fill(doc, map);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }

        // generate table for tower, floor, units
        List<List<String>> towerFloorUnits = generateTableDataForTowerFloorUnits(acquisition);
        docxComp.addToTable(doc, 0, towerFloorUnits);
        // at lampiran pembayaran
        docxComp.addToTable(doc, 1, towerFloorUnits);

        // lampiran pembayaran 
        docxComp.addToTable(doc, 2, generateTableDataForPembayaran(acquisition));

        // generate layout
        List<Aparkost> aparkostList = acquisition.getInvestments().stream()
                .map(inv -> inv.getAparkost())
                .collect(Collectors.toList());
        layoutImageService.getLayoutImages(aparkostList)
                .forEach(layoutImageData -> {
                    try {
                        addDenah(doc, layoutImageData);
                    } catch (InvalidFormatException | IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.write(baos);
        return baos.toByteArray();
    }

    private List<List<String>> generateTableDataForPembayaran(Acquisition a) {
        List<List<String>> table = new ArrayList();
        BigDecimal totalHarga = BigDecimal.ZERO;
        for (Detail d : pembayaranDataMapper.generateDetails(a)) {
            List<String> row = new ArrayList();
            row.add(d.getNomor().toString());
            row.add(d.getKeterangan());
            row.add(tglJatuhTempoFormatter.format(d.getTglJatuhTempo()));
            row.add(jumlahFormatter.format(d.getJumlah()));
            table.add(row);
            totalHarga = totalHarga.add(d.getJumlah());
        }
        List<String> row = new ArrayList();
        row.add("");
        row.add("");
        row.add("Harga");
        row.add(jumlahFormatter.format(totalHarga));
        table.add(row);
        return table;
    }
    private SimpleDateFormat tglJatuhTempoFormatter = new SimpleDateFormat("d");
    private NumberFormat jumlahFormatter = NumberFormat.getInstance();

    private List<List<String>> generateTableDataForTowerFloorUnits(Acquisition a) {
        Map<String, Map<String, List<String>>> towerFloorUnitMap = new HashMap();
        a.getInvestments().stream()
                .map(inv -> inv.getAparkost())
                .forEach(aparkost -> {
                    String towerName = aparkost.getTower().getName();
                    Map<String, List<String>> floorUnitMap = towerFloorUnitMap.get(towerName);
                    if (floorUnitMap == null) {
                        floorUnitMap = new HashMap();
                        towerFloorUnitMap.put(towerName, floorUnitMap);
                    }
                    String floor = aparkost.getFloor();
                    List<String> unitList = floorUnitMap.get(floor);
                    if (unitList == null) {
                        unitList = new ArrayList();
                        floorUnitMap.put(floor, unitList);
                    }
                    String unitName = aparkost.getName();
                    unitList.add(unitName);
                });
        List<List<String>> tableData = new ArrayList();
        towerFloorUnitMap
                .forEach((towerName, floorUnitMap) -> {
                    floorUnitMap.forEach((floor, unitList) -> {
                        String units = buildString(unitList, null, ",", null);
                        List<String> rowData = new ArrayList();
                        rowData.add(towerName);
                        rowData.add(floor);
                        rowData.add(units);
                        tableData.add(rowData);
                    });
                });
        return tableData;
    }

    private String buildString(List<String> list, String prefix, String infix, String suffix) {
        StringBuilder sb = new StringBuilder();
        if (prefix != null) {
            sb.append(prefix);
        }
        for (int i = 0; i < list.size() - 1; i++) {
            sb.append(list.get(i));
            if (infix != null) {
                sb.append(infix);
            }
        }
        sb.append(list.get(list.size() - 1));
        if (suffix != null) {
            sb.append(suffix);
        }
        return sb.toString();
    }

    private void addDenah(XWPFDocument doc, LayoutImageData layoutImageData) throws InvalidFormatException, IOException {
        String towerName = layoutImageData.getTowerName();
        String floor = layoutImageData.getFloor();
        InputStream imgStream = layoutImageData.getImageInputStream();
        int w = layoutImageData.getWidth();
        int h = layoutImageData.getHeight();
        String imgType = layoutImageData.getImageType();
        addDenah(doc, towerName, floor, imgStream, w, h, imgType, null);
    }

    private void addDenah(XWPFDocument doc, String towerName, String floor, InputStream img, int width, int height, String imgType, String fileName) throws InvalidFormatException, IOException {
        // create table
        XWPFTable table = doc.createTable();
        doc.createParagraph();
        //.createRun();
        //.addBreak();

        // create first row
        XWPFTableRow row = table.getRow(0);
        row.setCantSplitRow(true);
        XWPFTableCell cell = row.getCell(0);
        XWPFParagraph par = cell.getParagraphs().get(0);
        par.setSpacingAfter(0);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        cell.setText("Tower : " + towerName);

        // create second row
        row = table.createRow();
        row.setCantSplitRow(true);
        cell = row.getCell(0);
        par = cell.getParagraphs().get(0);
        par.setSpacingAfter(0);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        cell.setText("Lantai : " + floor);

        // create third row
        row = table.createRow();
        row.setCantSplitRow(true);
        cell = row.getCell(0);
        par = cell.getParagraphs().get(0);
        par.setSpacingAfter(0);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

        // insert image into the third row
        par = cell.getParagraphs().get(0);
        par.setSpacingAfter(0);
        XWPFRun run = par.insertNewRun(0);

        Dimension boundary = new Dimension(460, -1);
        Dimension dim = getScaledDimension(new Dimension(width, height), boundary);

        run.addPicture(img, docxComp.getImageFormat(imgType), fileName, Units.toEMU(dim.getWidth()), Units.toEMU(dim.getHeight()));
    }

    // http://stackoverflow.com/a/10245583
    private Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width && bound_width > 0) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height && bound_height > 0) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }
}
