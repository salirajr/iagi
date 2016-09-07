
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rj.sysinvest.layout.LayoutImageData;
import com.rj.sysinvest.layout.LayoutImageService;
import com.rj.sysinvest.layout.LayoutImageServiceImpl;
import com.rj.sysinvest.model.Aparkost;
import com.rj.sysinvest.model.Investor;
import com.rj.sysinvest.model.Site;
import com.rj.sysinvest.model.Tower;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for com.rj.sysinvest.layout.LayoutImageService
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public class TestLayoutImageService {

    public static LayoutImageService createLayoutImageService() {
        LayoutImageServiceImpl impl = new LayoutImageServiceImpl();
        impl.setLayoutTemplateDirectory("template/layout");
        impl.setObjectMapper(new ObjectMapper());
        impl.setAparkostRepository(new TestAparkostRepositoryImpl());
        return impl;
    }

    public static void main(String[] args) throws IOException {
        // define data
        List<Aparkost> selectedAparkosts = getData();

        // generate layout images
        LayoutImageService impl = createLayoutImageService();
        List<LayoutImageData> result = impl.getLayoutImages(selectedAparkosts);

        // write to file
        for (LayoutImageData d : result) {
            String fileName = d.getSiteName() + "_" + d.getTowerName() + "_" + d.getFloor();
            Path path = Paths.get("result-test", fileName + "." + d.getImageType());
            System.out.println("Writing to file " + path);
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, d.getImageRaw());
        }
    }

    public static List<Aparkost> getData() {
        Investor ir = new Investor();
        ir.setNationalId("3273200103850001");
        ir.setFullName("FIRMAN SUMARWAN");
        ir.setAddress("JL MALANGBONG 4 NO 2 RT/RW 003/003 KEL ANTAPANI WETAN KEC ANTAPANI BANDUNG");
        List<Aparkost> list = new ArrayList();
        list.add(createAparkost(1, "001", "G", "Tower1", "Site1", ir));
        list.add(createAparkost(9, "009", "G", "Tower1", "Site1", ir));
        list.add(createAparkost(1, "201", "2", "Tower1", "Site1", ir));
        list.add(createAparkost(8, "208", "2", "Tower1", "Site1", ir));
        return list;
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
