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

        d.setKuasaNama("RAIS");
        d.setKuasaAlamat("MAKASSAR");
        d.setKuasaJabatan("PRESIDEN");
        d.setKuasaKTP("0987654321");
        d.setKuasaTTL("Sungguminasa, 01 Maret 1986");

//        d.setLantaiTowerNomor("Lantai 1, tower 1, unit 1 2 3 4");
        d.setHarga(String.valueOf(a.getTotalFee()));
//        d.setHargaTerbilang(String.valueOf(a.getTotalFee()));
//        d.setCaraPembayaran("asdfg");
        d.setTglPemesanan(dateFormat.format(a.getTrxTimestamp()));

        return d;
    }
}
