
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Aparkost;
import com.rj.sysinvest.model.Investment;
import com.rj.sysinvest.model.Investor;
import com.rj.sysinvest.model.Site;
import com.rj.sysinvest.model.Staff;
import com.rj.sysinvest.model.Tower;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.sql.Date;
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
        List<Investment> investments = createAparkostList(investor).stream()
                .map(aparkost -> createInvestment(aparkost))
                .collect(Collectors.toList());
        Acquisition a = new Acquisition();
        a.setInvestor(investor);
        a.setInvestments(investments);
        a.setBookingFee(1000000);
        a.setDpFee(50000000);
        a.setTotalFee(85000000);
        a.setEndDate(new Date(System.currentTimeMillis()));
        a.setInvestor(TestUtil.createInvestor());
        a.setType("INSTALLMENT");
        a.setNPeriod(27);
        a.setRate(1000000);
        a.setStartDate(new Date(System.currentTimeMillis()));
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
        return s;
    }

    public static List<Aparkost> createAparkostList(Investor ir) {
        List<Aparkost> list = new ArrayList();
        list.add(createAparkost(1, "001", "G", "Tower1", "Site1", ir));
        list.add(createAparkost(9, "009", "G", "Tower1", "Site1", ir));
        list.add(createAparkost(1, "201", "2", "Tower1", "Site1", ir));
        list.add(createAparkost(8, "208", "2", "Tower1", "Site1", ir));
        return list;
    }

    public static Investor createInvestor() {
        Investor ir = new Investor();
        ir.setNationalId("3273200103850001");
        ir.setFullName("FIRMAN SUMARWAN");
        ir.setNickName("FIRMAN");
        ir.setAddress("JL MALANGBONG 4 NO 2 RT/RW 003/003 KEL ANTAPANI WETAN KEC ANTAPANI BANDUNG");
        ir.setBirthDate(new Date(System.currentTimeMillis()));
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
}
