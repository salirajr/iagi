package com.rj.sysinvest.akad;

import com.rj.sysinvest.model.Investment;
import javax.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service
@Data
public class AkadReportService {

    @Resource
    private PdfComponent pdfService;
    @Resource
    private LampiranLayoutService lampiranLayoutService;
    @Resource
    private LampiranPembayaranService lampiranPembayaranService;
    @Resource
    private AkadFormService akadFormService;

    /*
         1. akadPdf <- fill the akad pdf form
         2. lampiranPembayaranPdf <- load jrxml, fill data, export to pdf
         3. layoutImagePdf <- load svg, draw overlay, load jrxml, fill data, export to pdf
         4. finalAkadPdf <- merge 1,2,3 into single pdf file    
     */
    public byte[] generateCompleteAkadPdf(Investment investment) throws Exception {
        byte[] akadPdf = akadFormService.generatePdf(investment);
        byte[] lampiranPembayaranPdf = lampiranPembayaranService.generatePdf(investment);
        byte[] lampiranLayoutPdf = lampiranLayoutService.generatePdf(investment);
        return pdfService.mergePdf(akadPdf, lampiranPembayaranPdf, lampiranLayoutPdf);
    }

}
