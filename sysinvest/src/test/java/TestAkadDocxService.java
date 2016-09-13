
import com.rj.sysinvest.akad.docx.AkadDocxService;
import com.rj.sysinvest.akad.docx.DocxComponent;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public class TestAkadDocxService {

    public static void main(String[] args) throws Exception {
        AkadDocxService ads = createAkadDocxService();
        byte[] docxBytes = ads.generateAkadDocx(TestUtil.createAcquisition());
        TestUtil.writeToFile("result-test/test-akad-form.docx", docxBytes);
    }

    public static AkadDocxService createAkadDocxService() {
        AkadDocxService ads = new AkadDocxService();
        ads.setDocxComp(new DocxComponent());
        ads.setDataMapper(TestAkadFormReportService.createAkadFormDataService());
        ads.setLayoutImageService(TestLayoutImageService.createLayoutImageService());
        ads.setPembayaranDataMapper(TestLampiranPembayaranReportService.createLampiranPembayaranDataService());
        return ads;
    }
}
