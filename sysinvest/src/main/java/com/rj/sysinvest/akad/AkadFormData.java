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
            pihakPertamaTempatLahir,
            pihakPertamaTglLahir;

    private String kuasaNama,
            kuasaJabatan,
            kuasaAlamat,
            kuasaKTP,
            kuasaTempatLahir,
            kuasaTglLahir;

    private String pihakKeduaNama,
            pihakKeduaPekerjaan,
            pihakKeduaAlamat,
            pihakKeduaKTP,
            pihakKeduaTempatLahir,
            pihakKeduaTglLahir;

//    private Map<String, Map<String, List<String>>> towerFloorUnitMap;
    private String harga, hargaTerbilang, tglPemesanan, caraPembayaran;
    private String tglJatuhTempo, tglJatuhTempoTerbilang;

    private String tempatAkad, tglAkad;

}
