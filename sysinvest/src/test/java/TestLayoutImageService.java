
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rj.sysinvest.layout.LayoutImageData;
import com.rj.sysinvest.layout.LayoutImageService;
import com.rj.sysinvest.layout.LayoutImageServiceImpl;
import com.rj.sysinvest.model.Aparkost;
import java.io.IOException;
import java.util.List;

/**
 * Unit test for com.rj.sysinvest.layout.LayoutImageService
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public class TestLayoutImageService {

    public static void main(String[] args) throws IOException {
        // define data
        List<Aparkost> selectedAparkosts = TestUtil.createAparkostList();

        // generate layout images
        LayoutImageService impl = createLayoutImageService();
        List<LayoutImageData> result = impl.getLayoutImages(selectedAparkosts);

        // write to file
        for (LayoutImageData d : result) {
            String filepath = "result-test/" + d.getSiteName() + "_" + d.getTowerName() + "_" + d.getFloor();
            TestUtil.writeToFile(filepath, d.getImageRaw());
        }
    }

    public static LayoutImageService createLayoutImageService() {
        LayoutImageServiceImpl impl = new LayoutImageServiceImpl();
        impl.setLayoutTemplateDirectory("template/layout");
        impl.setObjectMapper(new ObjectMapper());
        impl.setAparkostRepository(new TestAparkostRepositoryImpl());
        return impl;
    }
}
