package com.rj.sysinvest.akad;

import com.rj.sysinvest.model.Acquisition;
import java.io.IOException;
import javax.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service
@Data
public class AkadFormReportService {

    @Resource
    private PdfComponent pdfService;
    @Resource
    private AkadFormDataService dataService;
    private String formPath = "template/akad.pdf";

    public byte[] generatePdf(Acquisition acquisition) throws IOException {
        AkadFormData data = dataService.generateAkadFormData(acquisition);
        return pdfService.loadFillSaveToBytes(formPath, data);
    }

}
