package com.rj.sysinvest.akad;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Component
public class PdfComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfComponent.class);

    public PdfComponent() {
        // Issue using PDFBox with Java 8
        // Due to the change of the java color management module towards “LittleCMS”, 
        // users can experience slow performance in color operations. 
        // Solution: disable LittleCMS in favour of the old KCMS (Kodak Color Management System)
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
    }

    public PDDocument fillAcroForm(PDDocument doc, Map<String, String> formData) throws IOException {
        PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
        List<PDField> fields = form.getFields();
        for (PDField f : fields) {
            String value = formData.get(f.getFullyQualifiedName());
            f.setValue(value);
            LOGGER.debug("{}={}", f.getFullyQualifiedName(), value);
        }
        form.flatten(fields, true);
        return doc;
    }

    public PDDocument fillAcroForm(PDDocument doc, Object formData) throws IOException {
        try {
            return fillAcroForm(doc, BeanUtils.describe(formData));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    public byte[] loadFillSaveToBytes(String acroFormFilePath, Map<String, String> formData) throws IOException {
        PDDocument doc = PDDocument.load(Paths.get(acroFormFilePath).toFile());
        fillAcroForm(doc, formData);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.save(baos);
        return baos.toByteArray();
    }

    public byte[] loadFillSaveToBytes(String acroFormFilePath, Object formData) throws IOException {
        try {
            return loadFillSaveToBytes(acroFormFilePath, BeanUtils.describe(formData));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

//    public byte[] mergePdf(List<byte[]> listOfPdfBytes) throws IOException {
//        PDFMergerUtility merger = new PDFMergerUtility();
//        ByteArrayOutputStream destStream = new ByteArrayOutputStream();
//        merger.setDestinationStream(destStream);
//        for (byte[] bytes : listOfPdfBytes) {
//            merger.addSource(createInputStream(bytes));
//        }
//        merger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
//        return destStream.toByteArray();
//    }
    // http://stackoverflow.com/questions/37589590/merge-pdf-files-using-pdfbox 
    public byte[] mergePdf(byte[] pdf, byte[]... otherPdfs) throws IOException {
        PDFMergerUtility merger = new PDFMergerUtility();
        PDDocument destination = PDDocument.load(pdf);
        for (byte[] otherPdf : otherPdfs) {
            merger.appendDocument(destination, PDDocument.load(otherPdf));
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        destination.save(baos);
        return baos.toByteArray();
    }

    private InputStream createInputStream(byte[] bytes) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(bytes);
        PipedInputStream inputStream = new PipedInputStream();
        baos.writeTo(new PipedOutputStream(inputStream));
        return inputStream;
    }

    public static void main(String[] args) throws Exception {

        AkadFormData data = new AkadFormData();
        data.setPihakPertamaNama("Raisssss");
        data.setPihakPertamaJabatan1("Sales Manager");
        data.setPihakPertamaAlamat1("Jl. Malino no 87");
        data.setKuasaNama("ANDI TAUFIQ YUSUF");
        data.setKuasaJabatan("DIRUT PT.IBNU AUF GLOBAL INVESTAMA");

        PdfComponent service = new PdfComponent();
        String formPath = "template/akad-ipb.pdf";

        byte[] bytes = service.loadFillSaveToBytes(formPath, BeanUtils.describe(data));
        Files.write(Paths.get("akad-rais.pdf"), bytes);
    }
}
