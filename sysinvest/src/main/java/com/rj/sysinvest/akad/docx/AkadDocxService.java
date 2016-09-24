package com.rj.sysinvest.akad.docx;

import com.rj.sysinvest.akad.AkadFormData;
import com.rj.sysinvest.akad.AkadFormDataMapper;
import com.rj.sysinvest.layout.LayoutImageData;
import com.rj.sysinvest.layout.LayoutImageService;
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Aparkost;
import com.rj.sysinvest.model.Investment;
import com.rj.sysinvest.model.Investor;
import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Service;
import com.rj.sysinvest.akad.LampiranPembayaranDataMapper;
import com.rj.sysinvest.util.ImageUtil;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;

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
    private LampiranPembayaranDataMapper pembayaranDataMapper;
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
        List<List<String>> table = a.getPayments().stream()
                .map(pembayaranDataMapper)
                .collect(Collectors.toList());
        // count total
        AtomicLong total = new AtomicLong();
        a.getPayments().forEach(p -> total.getAndUpdate(t -> t + p.getNominal()));
        table.add(Arrays.asList("", "", "Harga", moneyFormat.format(total.get())));
        return table;
    }

    private List<List<String>> generateTableDataForTowerFloorUnits(Acquisition a) {
        return a.getInvestments().stream()
                .map(investment -> {
                    Aparkost aparkost = investment.getAparkost();
                    return Arrays.asList(
                            aparkost.getTower().getName(),
                            aparkost.getFloor(),
                            aparkost.getName(),
                            moneyFormat.format(investment.getSoldRate())
                    );
                })
                .sorted((row1, row2) -> {
                    // compare tower
                    String t1 = row1.get(0), t2 = row2.get(0);
                    int ct = t1.compareTo(t2);
                    if (ct != 0) {
                        return ct;
                    }
                    // compare floor
                    String f1 = row1.get(1), f2 = row2.get(1);
                    int cf = f1.compareTo(f2);
                    if (cf != 0) {
                        return cf;
                    }
                    // compare unit
                    String u1 = row1.get(2), u2 = row2.get(2);
                    int cu = u1.compareTo(u2);
                    return cu;
                })
                .collect(Collectors.toList());
    }

    private void generateLampiranDenah(XWPFDocument doc, Acquisition acquisition) throws InvalidFormatException, IOException {
        List<Aparkost> aparkostList = acquisition.getInvestments().stream()
                .map(Investment::getAparkost)
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
        try (InputStream imgStream = new ByteArrayInputStream(layoutImageData.getImageRaw());) {
            String towerName = layoutImageData.getTowerName();
            String floor = layoutImageData.getFloor();
            int w = layoutImageData.getWidth();
            int h = layoutImageData.getHeight();
            String imgType = layoutImageData.getImageType();
            return addDenah(doc, towerName, floor, investor, imgStream, imgType, w, h);
        }
    }

    private XWPFTable addDenah(XWPFDocument doc, String towerName, String floor, Investor investor, InputStream img, String imgType, int width, int height) throws InvalidFormatException, IOException {
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
        Dimension dim = ImageUtil.getScaledDimension(width, height, 460, -1);
        int w = Units.toEMU(dim.getWidth());
        int h = Units.toEMU(dim.getHeight());
        formatRowAndGetCell.apply(table.createRow())
                .getParagraphs().get(0)
                .insertNewRun(0)
                .addPicture(img, docxComp.getImageFormat(imgType), null, w, h);

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

    private void generateLampiranKTP(XWPFDocument doc, Acquisition a) throws IOException, InvalidFormatException {
        // page title
        XWPFParagraph par = addPageBreak(doc);
        par.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = par.createRun();
        run.setBold(true);
        run.setText("LAMPIRAN KTP");

        BiFunction<String, String, XWPFTable> createTableKtp = (caption, imgPath) -> {
            Path path = Paths.get(imgPath);
            Dimension dim;
            try (InputStream in = Files.newInputStream(path);) {
                dim = ImageUtil.getImageDimension(in);
                dim = ImageUtil.getScaledDimension(dim, new Dimension(460, -1));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try (InputStream in = Files.newInputStream(path);) {
                XWPFTable table = doc.createTable();
                formatRowAndGetCell.apply(table.getRow(0))
                        .setText(caption);
                formatRowAndGetCell.apply(table.createRow())
                        .getParagraphs().get(0)
                        .createRun()
                        .addPicture(
                                in,
                                docxComp.getImageFormat(path.toString()),
                                path.toString(),
                                Units.toEMU(dim.width),
                                Units.toEMU(dim.height)
                        );
                return table;
            } catch (IOException | InvalidFormatException ex) {
                throw new RuntimeException(ex);
            }
        };
        // pihak pertama
        createTableKtp.apply("Pihak Pertama", a.getStaff().getScannedNationalIdPath());
        // add space break
        doc.createParagraph();
        // pihak kedua
        createTableKtp.apply("Pihak Kedua", a.getInvestor().getScannedNationalIdPath());
    }

}
