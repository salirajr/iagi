package com.rj.sysinvest.akad;

import com.rj.sysinvest.dao.AcquisitionRepository;
import com.rj.sysinvest.dao.TowerRepository;
import com.rj.sysinvest.layout.LayoutImageData;
import com.rj.sysinvest.layout.LayoutImageService;
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Aparkost;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${lampiranLayoutJrxmlPath}")
    private String lampiranLayoutJrxmlPath;

    @Resource
    private AcquisitionRepository acquisitionRepository;

    public byte[] generatePdf(Acquisition acquisition) throws Exception {
        // get list of selected aparkost
        List<Aparkost> selectedAparkosts = acquisition.getInvestments().stream()
                .map(investment -> investment.getAparkost())
                .collect(Collectors.toList());
        return generatePdf(selectedAparkosts);
    }

    public byte[] generatePdf(List<Aparkost> selectedAparkosts) throws Exception {
        // get list of layout data
        List<LayoutImageData> layoutDataList = layoutImageService.getLayoutImages(selectedAparkosts);
        // generate report to pdf
        Map<String, Object> params = new HashMap();
        return jasperService.loadFillExportToPdf(lampiranLayoutJrxmlPath, params, layoutDataList);
    }

}
