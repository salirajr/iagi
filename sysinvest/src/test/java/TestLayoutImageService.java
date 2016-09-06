
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rj.sysinvest.layout.LayoutData;
import com.rj.sysinvest.layout.LayoutImageServiceImpl;
import com.rj.sysinvest.model.Aparkost;
import com.rj.sysinvest.model.Site;
import com.rj.sysinvest.model.Tower;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public class TestLayoutImageService {

    public static void main(String[] args) throws IOException {
        // define data
        List<Aparkost> selectedAparkosts = getData();

        // generate layout images
        LayoutImageServiceImpl impl = new LayoutImageServiceImpl();
        impl.setLayoutTemplateDirectory("template/layout");
        impl.setObjectMapper(new ObjectMapper());
        List<LayoutData> result = impl.getLayoutImages(selectedAparkosts);

        // write to file
        for (LayoutData d : result) {
            String fileName = d.getSiteName() + "_" + d.getTowerName() + "_" + d.getLevel();
            Path path = Paths.get("result-test", fileName + "." + d.getImageType());
            System.out.println("Writing to file " + path);
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, d.getImageRaw());
        }
    }

    private static List<Aparkost> getData() {
        List<Aparkost> list = new ArrayList();
        list.add(createAparkost(1, "001", "G", "Tower1", "Site1"));
        list.add(createAparkost(9, "009", "G", "Tower1", "Site1"));
        list.add(createAparkost(1, "201", "2", "Tower1", "Site1"));
        list.add(createAparkost(8, "208", "2", "Tower1", "Site1"));
        return list;
    }

    static Aparkost createAparkost(long index, String aparkostName, String floor, String tower, String site) {
        Aparkost a = new Aparkost();
        a.setIndex(index);
        a.setName(aparkostName);
        a.setFloor(floor);
        Site s = new Site();
        s.setName(site);
        Tower t = new Tower();
        t.setName(tower);
        t.setSite(s);
        a.setTower(t);
        return a;
    }

}
