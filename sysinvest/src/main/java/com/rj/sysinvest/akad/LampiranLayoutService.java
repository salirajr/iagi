package com.rj.sysinvest.akad;

import com.rj.sysinvest.dao.AcquisitionRepository;
import com.rj.sysinvest.dao.TowerRepository;
import com.rj.sysinvest.layout.LayoutData;
import com.rj.sysinvest.layout.LayoutImageService;
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Tower;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service
@Data
public class LampiranLayoutService {

    @Resource(name = "LayoutImageServiceImpl")
    private LayoutImageService layoutImageService;
    @Resource
    private TowerRepository towerRepository;
    @Resource
    private JasperComponent jasperService;
    private String lampiranLayoutJrxmlPath;

    @Resource
    private AcquisitionRepository acquisitionRepository;

    public byte[] generatePdf(Long acquisitionId) throws Exception {
        Acquisition acquisition = acquisitionRepository.findOne(acquisitionId);
        return generatePdf(acquisition);
    }

    public byte[] generatePdf(Acquisition acquisition) throws Exception {
        Map<Long, List<String>> mapOfSelectedTowerIdAndRoomId = new TreeMap();
        acquisition.getInvestments().stream()
                .map(investment -> investment.getAparkost())
                .forEach(aparkost -> {
                    Tower tower = aparkost.getTower();
                    List<String> roomsByTower = mapOfSelectedTowerIdAndRoomId.get(tower.getId());
                    if (roomsByTower == null) {
                        roomsByTower = new ArrayList();
                        mapOfSelectedTowerIdAndRoomId.put(tower.getId(), roomsByTower);
                    }
                    roomsByTower.add(aparkost.getName());
                });
        return generateLampiranLayoutPdf(mapOfSelectedTowerIdAndRoomId);
    }

    /**
     *
     * @param mapOfSelectedTowerIdAndRoomId key=towerId, value=list of roomId
     * @return pdf raw
     * @throws Exception
     */
    public byte[] generateLampiranLayoutPdf(Map<Long, List<String>> mapOfSelectedTowerIdAndRoomId) throws Exception {
        List<LayoutData> layoutImages = new ArrayList();
        mapOfSelectedTowerIdAndRoomId.forEach((towerId, listOfSelectedRoomId) -> {
            Tower tower = towerRepository.findOne(towerId);
//            layoutImages.addAll(layoutImageService.getLayoutImages(tower, listOfSelectedRoomId));
        });
        Map<String, Object> lampiranLayoutParams = new HashMap();
        byte[] layoutImagePdf = jasperService.loadFillExportToPdf(lampiranLayoutJrxmlPath, lampiranLayoutParams, layoutImages);
        return layoutImagePdf;
    }

    public static void main(String[] args) throws Exception {
        Map<Long, List<String>> mapOfSelectedTowerIdAndAparkost = new HashMap();

        Long towerId = 100l;
        String[] aparkostList = {"100", "011", "912", "421"};
        mapOfSelectedTowerIdAndAparkost.put(towerId, Arrays.asList(aparkostList));

        LampiranLayoutService service = new LampiranLayoutService();
        byte[] pdfBytes = service.generateLampiranLayoutPdf(mapOfSelectedTowerIdAndAparkost);

        Files.write(Paths.get("tes-generate-layout.pdf"), pdfBytes);
    }

}
