
import com.rj.sysinvest.akad.JasperComponent;
import com.rj.sysinvest.akad.LampiranLayoutReportService;
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Aparkost;
import com.rj.sysinvest.model.Investment;
import com.rj.sysinvest.model.Investor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for com.rj.sysinvest.akad.LampiranLayoutReportService
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public class TestLampiranLayoutReportService {

    public static void main(String[] args) throws Exception {
        // setup service
        LampiranLayoutReportService svc = createLampiranLayoutReportService();
        // generate sample data
        Acquisition a = createAcquisition();
        // generate pdf
        byte[] pdfBytes = svc.generatePdf(createAcquisition());
        // Write the pdf to file
        String fileName = a.getInvestor().getFullName();
        writeToFile(fileName, pdfBytes);
    }

    static void writeToFile(String filename, byte[] bytes) throws IOException {
        Path path = Paths.get("result-test/lampiran-layout/", filename + ".pdf");
        System.out.println("Writing to file " + path);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, bytes);
    }

    public static LampiranLayoutReportService createLampiranLayoutReportService() {
        // setup LampiranLayoutReportService
        LampiranLayoutReportService svc = new LampiranLayoutReportService();
        svc.setJasperService(new JasperComponent());
        svc.setLayoutImageService(TestLayoutImageService.createLayoutImageService());
        svc.setJrxmlPath("template/lampiran-layout.jrxml");
        return svc;
    }

    static Acquisition createAcquisition() {
        Acquisition a = new Acquisition();
        Investor ir = new Investor();
        ir.setNationalId("3273200103850001");
        ir.setFullName("FIRMAN SUMARWAN");
        ir.setAddress("JL MALANGBONG 4 NO 2 RT/RW 003/003 KEL ANTAPANI WETAN KEC ANTAPANI BANDUNG");
        a.setInvestor(ir);

        List<Investment> list = new ArrayList();
        a.setInvestments(list);
        list.add(createAparkostInvestment(1, "001", "G", "Tower1", "Site1", ir));
        list.add(createAparkostInvestment(9, "009", "G", "Tower1", "Site1", ir));
        list.add(createAparkostInvestment(1, "201", "2", "Tower1", "Site1", ir));
        list.add(createAparkostInvestment(8, "208", "2", "Tower1", "Site1", ir));

        return a;
    }

    public static Investment createAparkostInvestment(long aparkostIndex, String aparkostName, String floor, String tower, String site, Investor investor) {
        Aparkost k = TestLayoutImageService.createAparkost(aparkostIndex, aparkostName, floor, tower, site, investor);
        Investment im = new Investment();
        im.setAparkost(k);
        return im;
    }
}
