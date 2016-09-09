package com.rj.sysinvest.akad;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Data
public class LampiranPembayaranData {

    private Header header;
    private List<Detail> details;

    @Data
    public static class Header {

        private String site, unit, nama, alamat, nomorKTP;

        public Map toMap() {
            Map m = new HashMap();
            m.put("site", site);
            m.put("unit", unit);
            m.put("nama", nama);
            m.put("alamat", alamat);
            m.put("nomorKTP", nomorKTP);
            return m;
        }
    }

    @Data
    public static class Detail {

        private Integer nomor;
        private String keterangan;
        private Date tglJatuhTempo;
        private BigDecimal jumlah;
    }

}
