package com.rj.sysinvest.akad;

import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Data
public class AkadFormData {

    private String pihakPertamaNama,
            pihakPertamaJabatan, pihakPertamaCompany,
            pihakPertamaAlamat,
            pihakPertamaKTP,
            pihakPertamaTTL;

    private String kuasaNama,
            kuasaJabatan,
            kuasaAlamat,
            kuasaKTP,
            kuasaTTL;

    private String pihakKeduaNama,
            pihakKeduaPekerjaan,
            pihakKeduaAlamat,
            pihakKeduaKTP,
            pihakKeduaTTL;

//    private Map<String, Map<String, List<String>>> towerFloorUnitMap;
    private String harga, hargaTerbilang, tglPemesanan, caraPembayaran;
    private String tglJatuhTempo, tglJatuhTempoTerbilang;

    private String tempatDanTglAkad;

}
