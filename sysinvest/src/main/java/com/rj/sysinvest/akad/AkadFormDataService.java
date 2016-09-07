package com.rj.sysinvest.akad;

import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Investor;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public interface AkadFormDataService {

    AkadFormData generateAkadFormData(Acquisition acquisition);
//      AkadFormData generateAkadFormData(Acquisition acquisition) {
//        AkadFormData d = new AkadFormData();
//
//        Investor i = acquisition.getInvestor();
//        d.setPihakKeduaNama(i.getFullName());
//        d.setPihakKeduaPekerjaan(i.getOccupation());
//        d.setPihakKeduaAlamat1(i.getAddress());
//        d.setPihakKeduaKTP(i.getNationalId());
//        d.setPihakKeduaTTL(i.getBirthPlace() + ", " + dateFormat.format(i.getBirthDate()));
//
//        return d;
//    }
}
