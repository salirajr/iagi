package com.rj.sysinvest.akad;

import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Investor;
import com.rj.sysinvest.model.Staff;
import java.text.SimpleDateFormat;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service
public class AkadFormDataServiceImpl implements AkadFormDataService {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public AkadFormData generateAkadFormData(Acquisition a) {

        AkadFormData d = new AkadFormData();

        Staff s = a.getStaff();
        d.setPihakPertamaNama(s.getFullName());
        d.setPihakPertamaJabatan2("PT.IBNU AUF GLOBAL INVESTAMA");
        d.setPihakPertamaKTP(s.getNationalId());
        d.setPihakPertamaAlamat(s.getAddress());
        d.setPihakPertamaTTL(s.getBirthPlace() + ", " + dateFormat.format(s.getBirthDate()));

        Investor i = a.getInvestor();
        d.setPihakKeduaNama(i.getFullName());
        d.setPihakKeduaPekerjaan(i.getOccupation());
        d.setPihakKeduaAlamat(i.getAddress());
        d.setPihakKeduaKTP(i.getNationalId());
        d.setPihakKeduaTTL(i.getBirthPlace() + ", " + dateFormat.format(i.getBirthDate()));

        d.setKuasaNama("ANDI TAUFIQ YUSUF");
        d.setKuasaJabatan("DIREKTUR UTAMA PT.IBNU AUF GLOBAL INVESTAMA");
        d.setKuasaAlamat("JL. PELANDUK NOMOR 56C, MAKASSAR.");
        d.setKuasaKTP("7371031701900005");
        d.setKuasaTTL("UJUNG PANDANG, 17-01-1990");

        d.setLantaiTowerNomor("Lantai 3 , Tower 1, Nomor : 330");
        d.setHarga("Rp. 75.900.000,00");
        d.setHargaTerbilang("(Tujuh Puluh Lima Juta Sembilan Ratus Ribu Rupiah) perunit");
        d.setCaraPembayaran("Bertahap yang jumlah angsuran (cicilan) dan waktu (tanggal) pembayaran angsuran berdasarkan daftar jadual pembayaran (payment schedule) dalam lampiran perjanjian ini.");
//        d.setTglPemesanan(dateFormat.format(a.getTrxTimestamp()));

        return d;
    }
}
