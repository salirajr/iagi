package com.rj.sysinvest.akad;

import com.rj.sysinvest.layout.LayoutImageData;
import com.rj.sysinvest.layout.LayoutImageService;
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Aparkost;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.Data;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service
@Data
public class LampiranLayoutReportService {

    @Resource(name = "LayoutImageServiceImpl")
    private LayoutImageService layoutImageService;
    @Resource
    private JasperComponent jasperService;
    
    @Value("${LampiranLayoutReportService.jrxmlPath}")
    private String jrxmlPath;

    public byte[] generatePdf(Acquisition acquisition) throws IOException, JRException {
        // get list of selected aparkost
        List<Aparkost> selectedAparkosts = acquisition.getInvestments().stream()
                .map(investment -> investment.getAparkost())
                .collect(Collectors.toList());
        return generatePdf(selectedAparkosts);
    }

    public byte[] generatePdf(List<Aparkost> selectedAparkosts) throws IOException, JRException {
        // get list of layout data
        List<LayoutImageData> layoutDataList = layoutImageService.getLayoutImages(selectedAparkosts);
        // generate report to pdf
        Map<String, Object> params = new HashMap();
        return jasperService.loadFillExportToPdf(jrxmlPath, params, layoutDataList);
    }

}
