package com.rj.sysinvest.akad;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Data
public class LampiranPembayaranData {

    private Integer nomor;
    private String keterangan;
    private Date tglJatuhTempo;
    private BigDecimal jumlah;

}
