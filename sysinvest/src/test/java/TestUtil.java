
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Aparkost;
import com.rj.sysinvest.model.Investment;
import com.rj.sysinvest.model.Investor;
import com.rj.sysinvest.model.Payment;
import com.rj.sysinvest.model.Rank;
import com.rj.sysinvest.model.Site;
import com.rj.sysinvest.model.Staff;
import com.rj.sysinvest.model.Tower;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public class TestUtil {

    public static void writeToFile(String filepath, byte[] bytes) throws IOException {
        Path path = Paths.get(filepath);
        System.out.println("Writing to file " + path);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, bytes);
    }

    public static Acquisition createAcquisition() {
        Investor investor = createInvestor();
        Staff staff = createStaff();
        List<Investment> investments = createAparkostList(investor).stream()
                .map(aparkost -> createInvestment(aparkost))
                .collect(Collectors.toList());
        Acquisition a = new Acquisition();
        a.setInvestor(investor);
        a.setInvestments(investments);
        a.setStaff(staff);
//        a.setBookingFee(1000000);
//        a.setDpFee(50000000);
//        a.setTotalFee(85000000);
//        a.setEndDate(new Date(System.currentTimeMillis()));
        a.setInvestor(TestUtil.createInvestor());
        a.setType("INSTALLMENT");
//        a.setNPeriod(27);
        a.setRate(1000000l);
//        a.setStartDate(new Date(System.currentTimeMillis()));
        a.setAuditTime(new Timestamp(System.currentTimeMillis()));
        a.setPayments(createPaymentList());
        return a;
    }

    public static Staff createStaff() {
        Staff s = new Staff();
        s.setAddress("Address");
        s.setBirthDate(new java.sql.Date(System.currentTimeMillis()));
        s.setBirthPlace("Indonesia");
        s.setNationalId("0987654321");
        s.setGender("M");
        s.setFullName("Nama lengkap nih");
        s.setScannedNationalIdPath("template/layout/THE APARKOST DMY_Tower 0.jpg");
        Rank rank = new Rank();
        rank.setName("Marketing Manajer");
        s.setRank(rank);
        return s;
    }

    public static List<Aparkost> createAparkostList(Investor ir) {
        List<Aparkost> list = new ArrayList();
        list.add(createAparkost(1, "001", "G", "Tower 1", "THE APARKOST IPB", ir));
        list.add(createAparkost(3, "103", "1", "Tower 1", "THE APARKOST IPB", ir));
        list.add(createAparkost(1, "201", "2", "Tower 7", "THE APARKOST IPB", ir));
        list.add(createAparkost(5, "205", "2", "Tower 1", "THE APARKOST IPB", ir));
        return list;
    }

    public static Investor createInvestor() {
        Investor ir = new Investor();
        ir.setNationalId("3273200103850001");
        ir.setFullName("FIRMAN SUMARWAN");
        ir.setNickName("FIRMAN");
        ir.setAddress("JL MALANGBONG 4 NO 2 RT/RW 003/003 KEL ANTAPANI WETAN KEC ANTAPANI BANDUNG");
        ir.setBirthDate(new Date(System.currentTimeMillis()));
        ir.setOccupation("KARYAWAN SWASTA");
        ir.setScannedNationalIdPath("template/layout/THE APARKOST DMY_Tower 0.jpg");
        ir.setAccountId("12345");
        return ir;
    }

    public static List<Aparkost> createAparkostList() {
        return createAparkostList(createInvestor());
    }

    public static Investment createInvestment(long aparkostIndex, String aparkostName, String floor, String tower, String site, Investor investor) {
        Aparkost k = createAparkost(aparkostIndex, aparkostName, floor, tower, site, investor);
        return createInvestment(k);
    }

    public static Investment createInvestment(Aparkost k) {
        Investment im = new Investment();
        im.setAparkost(k);
        im.setSoldRate(90000000l);
        return im;
    }

    public static Aparkost createAparkost(long index, String aparkostName, String floor, String tower, String site, Investor investor) {
        Aparkost a = new Aparkost();
        a.setIndex(index);
        a.setName(aparkostName);
        a.setFloor(floor);
        Site s = new Site();
        s.setName(site);
        Tower t = new Tower();
        t.setId(1l);
        t.setName(tower);
        t.setSite(s);
        a.setTower(t);
        a.setInvestor(investor);
        return a;
    }

    public static List<Payment> createPaymentList() {
        List<Payment> list = new ArrayList();
        for (int i = 1; i < 27; i++) {
            Payment p = new Payment();
            p.setId(new Long(i));
            p.setIndex(i);
            p.setNominal(90000000);
            p.setPaydate(new Date(System.currentTimeMillis()));
//            p.setType();
            list.add(p);
        }
        return list;
    }
}
