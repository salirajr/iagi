package com.rj.sysinvest.akad;

import com.rj.sysinvest.akad.util.JavaTerbilang;
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Investor;
import com.rj.sysinvest.model.Payment;
import com.rj.sysinvest.model.Staff;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service
public class AkadFormDataMapperImpl implements AkadFormDataMapper {

    private SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM-yyyy");
    private SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("d");
    private NumberFormat moneyFormatter = NumberFormat.getInstance();

    private Map<String, String> acquisitionTypeTemplate = new HashMap();

    public AkadFormDataMapperImpl() {
        acquisitionTypeTemplate.put("CASH", "Kontan yang telah dibayarkan sisa pembayarannya sebelum akad ini dilakukan.");
        acquisitionTypeTemplate.put("INSTALLMENT", "Bertahap yang jumlah angsuran (cicilan) dan waktu (tanggal) pembayaran angsuran berdasarkan daftar jadwal pembayaran (payment schedule) dalam lampiran perjanjian ini.");
        acquisitionTypeTemplate.put("SOFT_INSTALLMENT", "Bertahap yang jumlah angsuran (cicilan) dan waktu (tanggal) telah disepakati bersama.");
    }

    @Override
    public AkadFormData apply(Acquisition a) {
        AkadFormData d = new AkadFormData();

        Staff s = a.getStaff();
        d.setPihakPertamaNama(upper(s.getFullName()));
        d.setPihakPertamaJabatan(upper(s.getRank().getName()));
        d.setPihakPertamaCompany("PT.IBNU AUF GLOBAL INVESTAMA");
        d.setPihakPertamaKTP(s.getNationalId());
        d.setPihakPertamaAlamat(upper(s.getAddress()));
        d.setPihakPertamaTempatLahir(upper(s.getBirthPlace()));
        d.setPihakPertamaTglLahir(upper(shortDateFormat.format(s.getBirthDate())));

        d.setKuasaNama("ANDI TAUFIQ YUSUF");
        d.setKuasaAlamat("JL. PELANDUK NOMOR 56C, MAKASSAR");
        d.setKuasaJabatan("DIRUT PT.IBNU AUF GLOBAL INVESTAMA");
        d.setKuasaKTP("7371031701900005");
        d.setKuasaTempatLahir("UJUNG PANDANG");
        d.setKuasaTglLahir("17-01-1990");

        Investor i = a.getInvestor();
        d.setPihakKeduaNama(upper(i.getFullName()));
        d.setPihakKeduaPekerjaan(upper(i.getOccupation()));
        d.setPihakKeduaAlamat(upper(i.getAddress()));
        d.setPihakKeduaKTP(i.getNationalId());
        d.setPihakKeduaTempatLahir(upper(i.getBirthPlace()));
        d.setPihakKeduaTglLahir(upper(shortDateFormat.format(i.getBirthDate())));

        d.setHarga(moneyFormatter.format(a.getRate()));
        d.setHargaTerbilang(new JavaTerbilang(a.getRate()).toString());
        d.setCaraPembayaran(acquisitionTypeTemplate.get(a.getType()));

        d.setTglPemesanan(monthFormat.format(a.getAuditTime()));
        for (Payment p : a.getPayments()) {
            Date tglJatuhTempo = p.getPaydate();
            if (tglJatuhTempo != null) {
                d.setTglJatuhTempo(dateOnlyFormat.format(tglJatuhTempo));
                d.setTglJatuhTempoTerbilang(new JavaTerbilang(d.getTglJatuhTempo()).toString());
                break;
            }
        }
//        a.getInvestments().iterator().next().getAparkost().getTower().getSite().getCity();
        d.setTempatAkad("Jakarta");
        d.setTglAkad(shortDateFormat.format(a.getAuditTime()));

        return d;
    }

    String upper(String s) {
        return s == null ? null : s.toUpperCase();
    }
}
