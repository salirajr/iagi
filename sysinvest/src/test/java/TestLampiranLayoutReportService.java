
import com.rj.sysinvest.akad.JasperComponent;
import com.rj.sysinvest.akad.LampiranLayoutReportService;
import com.rj.sysinvest.model.Acquisition;

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
        Acquisition a = TestUtil.createAcquisition();
        // generate pdf
        byte[] pdfBytes = svc.generatePdf(a);
        // Write the pdf to file
        String fileName = a.getInvestor().getFullName();
        TestUtil.writeToFile("result-test/lampiran-layout/" + fileName, pdfBytes);
    }

    public static LampiranLayoutReportService createLampiranLayoutReportService() {
        // setup LampiranLayoutReportService
        LampiranLayoutReportService svc = new LampiranLayoutReportService();
        svc.setJasperService(new JasperComponent());
        svc.setLayoutImageService(TestLayoutImageService.createLayoutImageService());
        svc.setJrxmlPath("template/lampiran-layout.jrxml");
        return svc;
    }

}
