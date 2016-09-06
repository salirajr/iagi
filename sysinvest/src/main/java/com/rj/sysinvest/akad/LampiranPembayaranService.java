package com.rj.sysinvest.akad;

import com.rj.sysinvest.model.Acquisition;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.Data;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service
@Data
public class LampiranPembayaranService {

    @Resource
    private JasperComponent jasperService;
    private String jrxmlPath = "template/lampiran-pembayaran.jrxml";

    public byte[] generatePdf(Acquisition acquisition) throws IOException, JRException {
        List<LampiranPembayaranData> dataList = generateListOfLampiranPembayaranData(acquisition);
        Map<String, Object> params = new HashMap();
        return jasperService.loadFillExportToPdf(jrxmlPath, params, dataList);
    }

    private List<LampiranPembayaranData> generateListOfLampiranPembayaranData(Acquisition acquisition) {
        return new ArrayList();
    }
}
