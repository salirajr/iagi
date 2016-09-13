package com.rj.sysinvest.akad.docx;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.xwpf.usermodel.PositionInParagraph;
import org.apache.poi.xwpf.usermodel.TextSegement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.springframework.stereotype.Component;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Component
public class DocxComponent {

    public XWPFDocument load(String filepath) throws IOException {
        return new XWPFDocument(Files.newInputStream(Paths.get(filepath)));
    }

    public void fill(XWPFDocument doc, Map<String, String> data) {
        replaceInParagraphs(doc.getParagraphs(), data, "${", "}");
    }

    public int getImageFormat(String imgType) {
        if ("emf".equalsIgnoreCase(imgType)) {
            return XWPFDocument.PICTURE_TYPE_EMF;
        } else if ("wmf".equalsIgnoreCase(imgType)) {
            return XWPFDocument.PICTURE_TYPE_WMF;
        } else if ("pict".equalsIgnoreCase(imgType)) {
            return XWPFDocument.PICTURE_TYPE_PICT;
        } else if ("jpeg".equalsIgnoreCase(imgType) || "jpg".equalsIgnoreCase(imgType)) {
            return XWPFDocument.PICTURE_TYPE_JPEG;
        } else if ("png".equalsIgnoreCase(imgType)) {
            return XWPFDocument.PICTURE_TYPE_PNG;
        } else if ("dib".equalsIgnoreCase(imgType)) {
            return XWPFDocument.PICTURE_TYPE_DIB;
        } else if ("gif".equalsIgnoreCase(imgType)) {
            return XWPFDocument.PICTURE_TYPE_GIF;
        } else if ("tiff".equalsIgnoreCase(imgType)) {
            return XWPFDocument.PICTURE_TYPE_TIFF;
        } else if ("eps".equalsIgnoreCase(imgType)) {
            return XWPFDocument.PICTURE_TYPE_EPS;
        } else if ("bmp".equalsIgnoreCase(imgType)) {
            return XWPFDocument.PICTURE_TYPE_BMP;
        } else if ("wpg".equalsIgnoreCase(imgType)) {
            return XWPFDocument.PICTURE_TYPE_WPG;
        } else {
            throw new RuntimeException("Unsupported picture: " + imgType
                    + ". Expected emf|wmf|pict|jpeg|png|dib|gif|tiff|eps|bmp|wpg");
        }
    }

    public void addToTable(XWPFDocument doc, int tablePosition, String[][] dataList) {
        List list = new ArrayList();
        for (int i = 0, l = dataList.length; i < l; i++) {
            list.add(Arrays.asList(dataList[i]));
        }
        addToTable(doc, tablePosition, list);
    }

    public void addToTable(XWPFDocument doc, int tablePosition, List<List<String>> dataList) {
        addToTable(doc.getTables().get(tablePosition), dataList);
    }

    private void addToTable(XWPFTable table, List<List<String>> dataList) {
        dataList.forEach(dataRow -> {
            Iterator<String> data = dataRow.iterator();
            table.createRow().getTableCells()
                    .forEach(cell -> cell.setText(data.next()));
        });
    }

    // http://stackoverflow.com/a/28740659
    private void replaceInParagraphs(List<XWPFParagraph> paragraphs, Map<String, String> replacements, String prefixKey, String suffixKey) {
        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();

            for (Map.Entry<String, String> replPair : replacements.entrySet()) {
                String find = prefixKey + replPair.getKey() + suffixKey;
                String repl = replPair.getValue();
                if (repl == null) {
                    repl = "";
                }
                TextSegement found = paragraph.searchText(find, new PositionInParagraph());
                if (found != null) {
                    if (found.getBeginRun() == found.getEndRun()) {
                        // whole search string is in one Run
                        XWPFRun run = runs.get(found.getBeginRun());
                        String runText = run.getText(run.getTextPosition());
                        String replaced = runText.replace(find, repl);
                        run.setText(replaced, 0);
                    } else {
                        // The search string spans over more than one Run
                        // Put the Strings together
                        StringBuilder b = new StringBuilder();
                        for (int runPos = found.getBeginRun(); runPos <= found.getEndRun(); runPos++) {
                            XWPFRun run = runs.get(runPos);
                            b.append(run.getText(run.getTextPosition()));
                        }
                        String connectedRuns = b.toString();
                        String replaced = connectedRuns.replace(find, repl);

                        // The first Run receives the replaced String of all connected Runs
                        XWPFRun partOne = runs.get(found.getBeginRun());
                        partOne.setText(replaced, 0);
                        // Removing the text in the other Runs.
                        for (int runPos = found.getBeginRun() + 1; runPos <= found.getEndRun(); runPos++) {
                            XWPFRun partNext = runs.get(runPos);
                            partNext.setText("", 0);
                        }
                    }
                }
            }
        }
    }

}
