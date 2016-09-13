
import com.rj.sysinvest.akad.AkadFormData;
import com.rj.sysinvest.akad.AkadFormDataMapper;
import com.rj.sysinvest.akad.AkadFormDataMapperImpl;
import com.rj.sysinvest.akad.AkadFormReportService;
import com.rj.sysinvest.akad.PdfComponent;
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Investor;
import com.rj.sysinvest.model.Staff;
import java.text.SimpleDateFormat;

/**
 *
 * @author salirajr
 */
public class TestAkadFormReportService {

    public static void main(String[] args) throws Exception {
        AkadFormReportService s = createAkadFormReportService();
        Acquisition a = TestUtil.createAcquisition();
        byte[] bytes = s.generatePdf(a);
        String filepath = "result-test/akadform-" + a.getInvestor().getNickName() + ".pdf";
        TestUtil.writeToFile(filepath, bytes);
    }

    public static AkadFormReportService createAkadFormReportService() {
        AkadFormReportService s = new AkadFormReportService();
        s.setDataService(createAkadFormDataService());
        s.setPdfService(new PdfComponent());
        s.setFormPath("template/akad-form.pdf");
        return s;
    }

    public static AkadFormDataMapper createAkadFormDataService() {
        return new AkadFormDataMapperImpl();
//        return (Acquisition a) -> {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//            AkadFormData d = new AkadFormData();
//
//            Staff s = a.getStaff();
//            d.setPihakPertamaNama(s.getFullName());
//            d.setPihakPertamaJabatan2("PT.IBNU AUF GLOBAL INVESTAMA");
//            d.setPihakPertamaKTP(s.getNationalId());
//            d.setPihakPertamaAlamat(s.getAddress());
//            d.setPihakPertamaTTL(s.getBirthPlace() + ", " + dateFormat.format(s.getBirthDate()));
//
//            Investor i = a.getInvestor();
//            d.setPihakKeduaNama(i.getFullName());
//            d.setPihakKeduaPekerjaan(i.getOccupation());
//            d.setPihakKeduaAlamat(i.getAddress());
//            d.setPihakKeduaKTP(i.getNationalId());
//            d.setPihakKeduaTTL(i.getBirthPlace() + ", " + dateFormat.format(i.getBirthDate()));
//
//            d.setKuasaNama("RAIS");
//            d.setKuasaAlamat("MAKASSAR");
//            d.setKuasaJabatan("PRESIDEN");
//            d.setKuasaKTP("0987654321");
//            d.setKuasaTTL("Sungguminasa, 01 Maret 1986");
//
//            d.setLantaiTowerNomor("Lantai 1, tower 1, unit 1 2 3 4");
//            d.setHarga("Rp 1234567890");
//            d.setCaraPembayaran("asdfg");
//            d.setTglPemesanan("1 Januari 2016");
//
//            return d;
//        };
    }
}
