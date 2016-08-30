package com.rj.sysinvest.akad;

import com.rj.sysinvest.dao.TowerRepository;
import com.rj.sysinvest.layout.LayoutData;
import com.rj.sysinvest.layout.LayoutImageService;
import com.rj.sysinvest.model.Investment;
import com.rj.sysinvest.model.Room;
import com.rj.sysinvest.model.Tower;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Resource(name = "LayoutImageServiceSvgImpl")
    private LayoutImageService layoutImageService;
    @Resource
    private TowerRepository towerRepository;
    @Resource
    private JasperComponent jasperService;
    private String lampiranLayoutJrxmlPath;

    public byte[] generatePdf(Investment investment) throws Exception {
        Map<String, List<String>> mapOfSelectedTowerIdAndRoomId = new HashMap();
        investment.getRooms().forEach(room -> {
            String towerId = room.getTower().getId();
            List<String> roomsByTower = mapOfSelectedTowerIdAndRoomId.get(towerId);
            if (roomsByTower == null) {
                roomsByTower = new ArrayList();
                mapOfSelectedTowerIdAndRoomId.put(room.getTower().getId(), roomsByTower);
            }
            roomsByTower.add(room.getId());
        });
        return generateLampiranLayoutPdf(mapOfSelectedTowerIdAndRoomId);
    }

    /**
     *
     * @param mapOfSelectedTowerIdAndRoomId key=towerId, value=list of roomId
     * @return pdf raw
     * @throws Exception
     */
    public byte[] generateLampiranLayoutPdf(Map<String, List<String>> mapOfSelectedTowerIdAndRoomId) throws Exception {
        List<LayoutData> layoutImages = new ArrayList();
        mapOfSelectedTowerIdAndRoomId.forEach((towerId, listOfSelectedRoomId) -> {
            Tower tower = towerRepository.findOne(towerId);
            layoutImages.addAll(layoutImageService.getLayoutImages(tower, listOfSelectedRoomId));
        });
        Map<String, Object> lampiranLayoutParams = new HashMap();
        byte[] layoutImagePdf = jasperService.loadFillExportToPdf(lampiranLayoutJrxmlPath, lampiranLayoutParams, layoutImages);
        return layoutImagePdf;
    }

    public static void main(String[] args) {
        Map<String, List<String>> mapOfSelectedTowerIdAndRoomId = new HashMap();
        List<String> listofRoomId = Arrays.asList(new String[]{"001", "112"});
        mapOfSelectedTowerIdAndRoomId.put("tower1", listofRoomId);
    }
}
