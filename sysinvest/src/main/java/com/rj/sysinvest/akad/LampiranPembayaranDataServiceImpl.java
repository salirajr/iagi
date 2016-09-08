package com.rj.sysinvest.akad;

import com.rj.sysinvest.akad.LampiranPembayaranData.Detail;
import com.rj.sysinvest.model.Acquisition;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
        for (int i = 1; i < 4; i++) {
            Detail d = new Detail();
            d.setNomor(1);
            d.setTglJatuhTempo(new Date());
            d.setKeterangan("Angsuran " + i);
            d.setJumlah(new BigDecimal(1000000 * i));
            l.add(d);
        }
        return l;
    }

}
