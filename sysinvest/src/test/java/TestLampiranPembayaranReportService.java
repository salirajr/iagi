
import com.rj.sysinvest.akad.JasperComponent;
import com.rj.sysinvest.akad.LampiranPembayaranReportService;
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Aparkost;
import com.rj.sysinvest.model.Investment;
import com.rj.sysinvest.model.Investor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Unit test for com.rj.sysinvest.akad.LampiranPembayaranReportService
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public class TestLampiranPembayaranReportService {

    public static void main(String[] args) throws Exception {
        // setup service
        LampiranPembayaranReportService lps = new LampiranPembayaranReportService();
        JasperComponent jasperComponent = new JasperComponent();
        jasperComponent.setAlwaysCompile(true);
        lps.setJasperService(jasperComponent);
        lps.setJrxmlPath("template/lampiran-pembayaran.jrxml");
        // generate pdf
        Acquisition a = getAcquisition();
        byte[] pdfBytes = lps.generatePdf(a);
        // Write the pdf to file
        String fileName = a.getInvestor().getFullName();
        Path path = Paths.get("result-test", fileName + ".pdf");
        System.out.println("Writing to file " + path);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, pdfBytes);
    }

    private static Acquisition getAcquisition() {
        Acquisition a = new Acquisition();
        Investor ir = new Investor();
        ir.setNationalId("3273200103850001");
        ir.setFullName("FIRMAN SUMARWAN");
        ir.setAddress("JL MALANGBONG 4 NO 2 RT/RW 003/003 KEL ANTAPANI WETAN KEC ANTAPANI BANDUNG");
        a.setInvestor(ir);

        a.setInvestments(new ArrayList());
        Investment im = new Investment();
        Aparkost k = new Aparkost();
        k.setName("001");
        im.setAparkost(k);
        a.getInvestments().add(im);

        im = new Investment();
        k = new Aparkost();
        k.setName("121");
        im.setAparkost(k);
        a.getInvestments().add(im);

        return a;
    }
}
