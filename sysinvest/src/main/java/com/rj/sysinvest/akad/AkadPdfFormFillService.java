package com.rj.sysinvest.akad;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
//@Service
@Data
public class AkadPdfFormFillService {

    public AkadPdfFormFillService() {
        // Issue using PDFBox with Java 8
        // Due to the change of the java color management module towards “LittleCMS”, 
        // users can experience slow performance in color operations. 
        // Solution: disable LittleCMS in favour of the old KCMS (Kodak Color Management System)
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
    }

    public PDDocument fillAkadForm(AkadDataBean formData) throws IOException {
        try {
            return fillAkadForm(BeanUtils.describe(formData));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String formPath = "template/akad.pdf";

    public PDDocument fillAkadForm(Map<String, String> formData) throws IOException {
        PDDocument doc = PDDocument.load(Paths.get(formPath).toFile());
        PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
        List<PDField> fields = form.getFields();
        for (PDField f : fields) {
            String value = formData.get(f.getFullyQualifiedName());
            f.setValue(value);
            System.out.println("formData." + f.getFullyQualifiedName() + "=" + value);
        }
        // only flatten the fields which has been set
        form.flatten(fields, true);
        return doc;
    }

    public static void main(String[] args) throws IOException {
        Date now = new Date();
        AkadDataBean data = new AkadDataBean();
        data.setPihakPertamaNama("Raisssss");
        data.setPihakPertamaJabatan1("Sales Manager");
        data.setPihakPertamaAlamat1("Jl. Malino no 87");
        data.setKuasaNama("ANDI TAUFIQ YUSUF");
        data.setKuasaJabatan("DIRUT PT.IBNU AUF GLOBAL INVESTAMA");

        AkadPdfFormFillService apffs = new AkadPdfFormFillService();
        apffs.fillAkadForm(data)
                .save("akad-rais.pdf");
    }
}
