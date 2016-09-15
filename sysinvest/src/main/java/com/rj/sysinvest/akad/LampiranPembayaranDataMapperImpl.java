package com.rj.sysinvest.akad;

import com.rj.sysinvest.model.Payment;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

/**
 *
 * @author salirajr
 */
@Service
public class LampiranPembayaranDataMapperImpl implements LampiranPembayaranDataMapper {

    @Override
    public LampiranPembayaranData apply(Payment payment) {
        LampiranPembayaranData d = new LampiranPembayaranData();
        d.setJumlah(new BigDecimal(payment.getNominal()));
        d.setNomor(payment.getIndex() + 1);
        d.setKeterangan(payment.getType());
        d.setTglJatuhTempo(payment.getPaydate());
        return d;
    }

}
