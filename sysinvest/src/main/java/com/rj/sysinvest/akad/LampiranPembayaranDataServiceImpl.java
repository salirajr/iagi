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
        int i = 1;
        long totalFee = acquisition.getTotalFee();
        // Tanda Jadi
        Detail d = new Detail();
        d.setNomor(i++);
        d.setKeterangan("Tanda Jadi");
        d.setJumlah(new BigDecimal(acquisition.getBookingFee()));
        totalFee -= acquisition.getBookingFee();
        l.add(d);
        // Uang Muka
        d = new Detail();
        d.setNomor(i++);
        d.setKeterangan("Uang Muka");
        d.setJumlah(new BigDecimal(acquisition.getDpFee()));
        totalFee -= acquisition.getDpFee();
        l.add(d);

        String tKet = "Angsuran";
        long nRow = 0;
        switch (acquisition.getType()) {
            case "CASH":
                nRow = 0;
                tKet = "LUNAS";
                d.setNomor(i++);
                d.setKeterangan(tKet);
                d.setJumlah(new BigDecimal(totalFee));
                break;
            case "SOFT_INSTALLMENT":
                nRow = acquisition.getNPeriod();
                break;
            case "INSTALLMENT":
                nRow = 27;
                break;
        }
        double installment = totalFee / nRow;
        for (; i < nRow; i++) {
            d = new Detail();
            d.setNomor(i);
            d.setTglJatuhTempo(new Date());
            d.setKeterangan(tKet + " " + i);
            d.setJumlah(new BigDecimal(installment));
            l.add(d);
        }
        return l;
    }

}
