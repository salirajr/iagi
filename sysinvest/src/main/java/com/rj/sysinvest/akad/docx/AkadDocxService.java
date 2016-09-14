package com.rj.sysinvest.akad.docx;

import com.rj.sysinvest.akad.AkadFormData;
import com.rj.sysinvest.akad.AkadFormDataMapper;
import com.rj.sysinvest.akad.LampiranPembayaranData.Detail;
import com.rj.sysinvest.akad.LampiranPembayaranDataService;
import com.rj.sysinvest.layout.LayoutImageData;
import com.rj.sysinvest.layout.LayoutImageService;
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Aparkost;
import com.rj.sysinvest.model.Investment;
import com.rj.sysinvest.model.Investor;
import com.rj.sysinvest.model.Tower;
import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
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
import org.springframework.stereotype.Service;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service
@Data
public class AkadDocxService {

    @Resource
    private DocxComponent docxComp;
    @Resource
    private AkadFormDataMapper dataMapper;
    @Resource(name = "LayoutImageServiceImpl")
    private LayoutImageService layoutImageService;
    @Resource
    private LampiranPembayaranDataService pembayaranDataMapper;
    //
    private String templatePath = "template/akad-form.docx";
    private NumberFormat moneyFormat = NumberFormat.getInstance();

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

        try {
            // generate layout
            generateLampiranDenah(doc, acquisition);
        } catch (InvalidFormatException ex) {
            throw new RuntimeException(ex);
        }

        try {
            // generate lampiran ktp images
            generateLampiranKTP(doc, acquisition);
        } catch (InvalidFormatException ex) {
            throw new RuntimeException(ex);
        }

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
            row.add(moneyFormat.format(d.getJumlah()));
            table.add(row);
            totalHarga = totalHarga.add(d.getJumlah());
        }
        List<String> row = new ArrayList();
        row.add("");
        row.add("");
        row.add("Harga");
        row.add(moneyFormat.format(totalHarga));
        table.add(row);
        return table;
    }
    private SimpleDateFormat tglJatuhTempoFormatter = new SimpleDateFormat("dd-MM-yyyy");
    private NumberFormat jumlahFormatter = NumberFormat.getInstance();

    private List<List<String>> generateTableDataForTowerFloorUnits(Acquisition a) {
        // aggregate data
        // Map<Tower, Map<Floor, List<Investment>>>
        Map<Tower, Map<String, List<Investment>>> towerFloorUnitMap = new HashMap();
        a.getInvestments().stream()
                .forEach(investment -> {
                    Aparkost aparkost = investment.getAparkost();
                    Tower tower = aparkost.getTower();
                    Map<String, List<Investment>> floorUnitMap = towerFloorUnitMap.get(tower);
                    if (floorUnitMap == null) {
                        floorUnitMap = new HashMap();
                        towerFloorUnitMap.put(tower, floorUnitMap);
                    }
                    String floor = aparkost.getFloor();
                    List<Investment> unitList = floorUnitMap.get(floor);
                    if (unitList == null) {
                        unitList = new ArrayList();
                        floorUnitMap.put(floor, unitList);
                    }
                    unitList.add(investment);
                });
        // convert into matrix 2d
        List<List<String>> tableData = new ArrayList();
        towerFloorUnitMap
                .forEach((tower, floorUnitMap)
                        -> floorUnitMap.forEach((floor, investmentList) -> {
                            List<String> rowData = new ArrayList();
                            investmentList.forEach(investment -> {
                                rowData.add(tower.getName());
                                rowData.add(floor);
                                rowData.add(investment.getAparkost().getName());
                                rowData.add(moneyFormat.format(investment.getSoldRate()));
                            });
                            tableData.add(rowData);
                        })
                );
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

    private void generateLampiranDenah(XWPFDocument doc, Acquisition acquisition) throws InvalidFormatException, IOException {
        List<Aparkost> aparkostList = acquisition.getInvestments().stream()
                .map(inv -> inv.getAparkost())
                .collect(Collectors.toList());
        List<LayoutImageData> layoutImageDataList = layoutImageService.getLayoutImages(aparkostList);
        int l = layoutImageDataList.size();
        for (int i = 0; i < l - 1; i++) {
            addDenah(doc, layoutImageDataList.get(i), acquisition.getInvestor());
            addPageBreak(doc);
        }
        addDenah(doc, layoutImageDataList.get(l - 1), acquisition.getInvestor());
    }

    private XWPFParagraph addPageBreak(XWPFDocument doc) {
        XWPFParagraph p = doc.createParagraph();
        p.setPageBreak(true);
        return p;
    }

    private XWPFTable addDenah(XWPFDocument doc, LayoutImageData layoutImageData, Investor investor) throws InvalidFormatException, IOException {
        String towerName = layoutImageData.getTowerName();
        String floor = layoutImageData.getFloor();
        InputStream imgStream = layoutImageData.getImageInputStream();
        int w = layoutImageData.getWidth();
        int h = layoutImageData.getHeight();
        String imgType = layoutImageData.getImageType();
        return addDenah(doc, towerName, floor, investor, imgStream, w, h, imgType, null);
    }

    private XWPFTable addDenah(XWPFDocument doc, String towerName, String floor, Investor investor, InputStream img, int width, int height, String imgType, String fileName) throws InvalidFormatException, IOException {
        // create table
        XWPFTable table = doc.createTable();
        // 1st row
        formatRowAndGetCell.apply(table.getRow(0))
                .setText("Tower : " + towerName);
        // 2nd row
        formatRowAndGetCell.apply(table.createRow())
                .setText("Lantai : " + floor);
        // 3rd row
        formatRowAndGetCell.apply(table.createRow())
                .setText("Account Id : " + investor.getAccountId());
        // 4th row
        formatRowAndGetCell.apply(table.createRow())
                .setText("Investor : " + investor.getFullName());
        // 5th row
        Dimension dim = getScaledDimension(new Dimension(width, height), new Dimension(460, -1));
        formatRowAndGetCell.apply(table.createRow())
                .getParagraphs().get(0)
                .insertNewRun(0)
                .addPicture(img, docxComp.getImageFormat(imgType), fileName, Units.toEMU(dim.getWidth()), Units.toEMU(dim.getHeight()));

        return table;
    }

    // format the row and return the cell object
    Function<XWPFTableRow, XWPFTableCell> formatRowAndGetCell = row -> {
        row.setCantSplitRow(true);
        XWPFTableCell cell = row.getCell(0);
        XWPFParagraph par = cell.getParagraphs().get(0);
        par.setSpacingAfter(0);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        return cell;
    };

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

    private void generateLampiranKTP(XWPFDocument doc, Acquisition a) throws IOException, InvalidFormatException {

//        Path imgPath2 = Paths.get(a.getInvestor().getScannedNationalIdPath());

        addPageBreak(doc);
        // create table
        XWPFTable table = doc.createTable(2, 1);
        XWPFParagraph par = formatRowAndGetCell.apply(table.getRow(0))
                .getParagraphs().get(0);
        XWPFRun run = par.insertNewRun(0);
        run.setText("Pihak Pertama");
        run.addBreak();
        Path imgPath1 = Paths.get(a.getStaff().getScannedNationalIdPath());
        InputStream inputStream = Files.newInputStream(imgPath1);
        Dimension dim = getImageDimension(imgPath1.toFile());
        int pictType = docxComp.getImageFormat(imgPath1.toString());
        run.addPicture(inputStream, pictType, imgPath1.toString(), dim.width, dim.height);
    }

    /**
     * Gets image dimensions for given file http://stackoverflow.com/a/12164026
     *
     * @param imgFile image file
     * @return dimensions of image
     * @throws IOException if the file is not a known image
     */
    public static Dimension getImageDimension(File imgFile) throws IOException {
        int pos = imgFile.getName().lastIndexOf(".");
        if (pos == -1) {
            throw new IOException("No extension for file: " + imgFile.getAbsolutePath());
        }
        String suffix = imgFile.getName().substring(pos + 1);
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
        if (iter.hasNext()) {
            ImageReader reader = iter.next();
            try {
                ImageInputStream stream = new FileImageInputStream(imgFile);
                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                return new Dimension(width, height);
            } finally {
                reader.dispose();
            }
        }
        throw new IOException("Not a known image file: " + imgFile.getAbsolutePath());
    }
}
