package com.rj.sysinvest.akad;

import com.rj.sysinvest.model.Investment;
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

    public byte[] generatePdf(Investment investment) throws IOException, JRException {
        Map<String, Object> params = new HashMap();
        List<LampiranPembayaranData> dataList = new ArrayList();
        return jasperService.loadFillExportToPdf(jrxmlPath, params, dataList);
    }
}
