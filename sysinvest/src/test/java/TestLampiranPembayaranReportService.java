
import com.rj.sysinvest.akad.JasperComponent;
import com.rj.sysinvest.akad.LampiranPembayaranData.Detail;
import com.rj.sysinvest.akad.LampiranPembayaranDataService;
import com.rj.sysinvest.akad.LampiranPembayaranReportService;
import com.rj.sysinvest.model.Acquisition;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Unit test for com.rj.sysinvest.akad.LampiranPembayaranReportService
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public class TestLampiranPembayaranReportService {

    public static void main(String[] args) throws Exception {
        // setup service
        LampiranPembayaranReportService lps = createLampiranPembayaranReportService();
        // generate sample data
        Acquisition a = TestUtil.createAcquisition();
        // generate pdf
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

    public static LampiranPembayaranReportService createLampiranPembayaranReportService() {
        LampiranPembayaranReportService lps = new LampiranPembayaranReportService();
        JasperComponent jasperComponent = new JasperComponent();
        jasperComponent.setAlwaysCompile(true);
        lps.setJasperService(jasperComponent);
        lps.setJrxmlPath("template/lampiran-pembayaran.jrxml");
        LampiranPembayaranDataService dataService = createLampiranPembayaranDataService();
        lps.setDataService(dataService);
        return lps;
    }

    public static LampiranPembayaranDataService createLampiranPembayaranDataService() {
        LampiranPembayaranDataService ds = new LampiranPembayaranDataService() {
            @Override
            public List<Detail> generateDetails(Acquisition acquisition) {
                List<Detail> l = new ArrayList();
                for (int i = 1; i < 4; i++) {
                    Detail d = new Detail();
                    d.setNomor(1);
                    d.setTglJatuhTempo(new Date());
                    d.setKeterangan("Angsuran " + i);
                    d.setJumlah(new BigDecimal(1000000 * i));
                    l.add(d);
                }
                return l;
            }
        };
        return ds;
    }

}