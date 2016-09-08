package com.rj.sysinvest.akad;

import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Investor;
import java.text.SimpleDateFormat;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service
public class AkadFormDataServiceImpl {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public AkadFormData generateAkadFormData(Acquisition acquisition) {

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
