package com.rj.sysinvest.akad;

import com.rj.sysinvest.model.Acquisition;
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
    private LampiranLayoutReportService lampiranLayoutService;
    @Resource
    private LampiranPembayaranReportService lampiranPembayaranService;
    @Resource
    private AkadFormReportService akadFormService;

    /*
         1. akadPdf <- fill the akad pdf form
         2. lampiranPembayaranPdf <- load jrxml, fill data, export to pdf
         3. layoutImagePdf <- load image, draw overlay, load jrxml, fill data, export to pdf
         4. finalAkadPdf <- merge 1,2,3 into single pdf file    
     */
    public byte[] generateCompleteAkadPdf(Acquisition acquisition) throws Exception {
        byte[] akadPdf = akadFormService.generatePdf(acquisition);
        byte[] lampiranPembayaranPdf = lampiranPembayaranService.generatePdf(acquisition);
        byte[] lampiranLayoutPdf = lampiranLayoutService.generatePdf(acquisition);
        return pdfService.mergePdf(akadPdf, lampiranPembayaranPdf, lampiranLayoutPdf);
    }

}
