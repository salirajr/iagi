package com.rj.sysinvest.akad;

import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Data
public class AkadFormData {

    private String pihakPertamaNama,
            pihakPertamaJabatan1, pihakPertamaJabatan2,
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

    private String lantaiTowerNomor;

    private String harga, hargaTerbilang, tglPemesanan, caraPembayaran;

    private String tempatDanTanggal;

}
