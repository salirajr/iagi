package com.rj.sysinvest.akad;

import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Investor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import javax.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service
@Data
public class AkadFormService {

    @Resource
    private PdfComponent pdfService;
    private String formPath = "template/akad.pdf";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public byte[] generatePdf(Acquisition acquisition) throws IOException {
        AkadFormData d = generateAkadFormData(acquisition);
        return pdfService.loadFillSaveToBytes(formPath, d);
    }

    private AkadFormData generateAkadFormData(Acquisition acquisition) {
        AkadFormData d = new AkadFormData();

        Investor i = acquisition.getInvestor();
        d.setPihakKeduaNama(i.getFullName());
        d.setPihakKeduaPekerjaan(i.getOccupation());
        d.setPihakKeduaAlamat1(i.getAddress());
        d.setPihakKeduaKTP(i.getNationalId());
        d.setPihakKeduaTTL(i.getBirthPlace() + ", " + dateFormat.format(i.getBirthDate()));

        return d;
    }
}
