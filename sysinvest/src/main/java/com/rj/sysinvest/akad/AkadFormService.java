package com.rj.sysinvest.akad;

import com.rj.sysinvest.model.Investment;
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

    public byte[] generatePdf(Investment investment) throws IOException {
        AkadFormData d = new AkadFormData();
        setPihakKeduaFromInvestor(d, investment.getInvestor());
        return pdfService.loadFillSaveToBytes(formPath, d);
    }

    private void setPihakKeduaFromInvestor(AkadFormData d, Investor i) {
        d.setPihakKeduaNama(i.getFullName());
        d.setPihakKeduaPekerjaan(i.getOccupation());
        d.setPihakKeduaAlamat1(i.getAddress());
        d.setPihakKeduaKTP(i.getNationalId());
        d.setPihakKeduaTTL(i.getBirthPlace() + ", " + dateFormat.format(i.getBirthDate()));
    }
}
