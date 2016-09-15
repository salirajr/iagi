
import com.rj.sysinvest.akad.AkadFormDataMapper;
import com.rj.sysinvest.akad.AkadFormDataMapperImpl;
import com.rj.sysinvest.akad.LampiranPembayaranData.Detail;
import com.rj.sysinvest.akad.LampiranPembayaranDataService;
import com.rj.sysinvest.akad.docx.AkadDocxService;
import com.rj.sysinvest.akad.docx.DocxComponent;
import com.rj.sysinvest.model.Acquisition;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
        ads.setDataMapper(createAkadFormDataService());
        ads.setLayoutImageService(TestLayoutImageService.createLayoutImageService());
        ads.setPembayaranDataMapper(createLampiranPembayaranDataService());
        return ads;
    }

    public static AkadFormDataMapper createAkadFormDataService() {
        return new AkadFormDataMapperImpl();
    }

    public static LampiranPembayaranDataService createLampiranPembayaranDataService() {
        LampiranPembayaranDataService ds = new LampiranPembayaranDataService() {
            @Override
            public List<Detail> generateDetails(Acquisition acquisition) {
                List<Detail> l = new ArrayList();
                for (int i = 1; i <= 30; i++) {
                    Detail d = new Detail();
                    d.setNomor(i);
                    d.setTglJatuhTempo(new Date(System.currentTimeMillis()));
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
