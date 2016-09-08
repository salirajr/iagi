
import com.rj.sysinvest.akad.AkadFormDataService;
import com.rj.sysinvest.akad.AkadFormReportService;
import com.rj.sysinvest.akad.PdfComponent;
import com.rj.sysinvest.model.Acquisition;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author salirajr
 */
public class TestAkadFormReportService {

    public static void main(String[] args) throws Exception {
        AkadFormReportService s = createAkadFormReportService();
        Acquisition a = TestUtil.createAcquisition();
        byte[] bytes = s.generatePdf(a);
        String filepath = "result-test/akadform/" + a.getInvestor().getNickName() + ".pdf";
        TestUtil.writeToFile(filepath, bytes);
    }

    public static AkadFormReportService createAkadFormReportService() {
        AkadFormReportService s = new AkadFormReportService();
        s.setDataService(new AkadFormDataService());
        s.setPdfService(new PdfComponent());
        return s;
    }
}
