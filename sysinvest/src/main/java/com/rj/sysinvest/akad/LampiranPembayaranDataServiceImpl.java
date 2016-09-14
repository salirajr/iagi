package com.rj.sysinvest.akad;

import com.rj.sysinvest.akad.LampiranPembayaranData.Detail;
import com.rj.sysinvest.model.Acquisition;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author salirajr
 */
@Service
public class LampiranPembayaranDataServiceImpl extends LampiranPembayaranDataService {

    @Override
    public List<LampiranPembayaranData.Detail> generateDetails(Acquisition acquisition) {
        List<Detail> l = new ArrayList();
        System.out.println("acquisition.payment.length="+acquisition.getPayments().size());
        acquisition.getPayments().forEach(payment -> {
            Detail d = new Detail();
            d.setJumlah(new BigDecimal(payment.getNominal()));
            d.setNomor(payment.getIndex()+1);
            d.setKeterangan(payment.getType());
            d.setTglJatuhTempo(payment.getPaydate());
            l.add(d);
        });
        System.out.println("list.denah.length="+l.size());
        return l;
    }

}
