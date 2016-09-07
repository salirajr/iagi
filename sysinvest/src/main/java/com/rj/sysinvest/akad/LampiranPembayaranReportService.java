package com.rj.sysinvest.akad;

import com.rj.sysinvest.model.Acquisition;
import java.io.IOException;
import javax.annotation.Resource;
import lombok.Data;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service
@Data
public class LampiranPembayaranReportService {

    @Resource
    private LampiranPembayaranDataService dataService;
    @Resource
    private JasperComponent jasperService;
    @Value("${LampiranPembayaranReportService.jrxmlPath}")
    private String jrxmlPath;

    public byte[] generatePdf(Acquisition acquisition) throws IOException, JRException {
        LampiranPembayaranData data = dataService.generatePembayaranReportData(acquisition);
        return jasperService.loadFillExportToPdf(jrxmlPath, data.getHeader().toMap(), data.getDetails());
    }

//    private List<LampiranPembayaranDetailData> generateListOfLampiranPembayaranData(Acquisition a) {
//        List<LampiranPembayaranDetailData> list = new ArrayList();
//
//        LampiranPembayaranDetailData d = new LampiranPembayaranDetailData();
//        d.setNomor(1);
//        d.setKeterangan("Tanda Jadi");
//        d.setTglJatuhTempo(null);
//        d.setJumlah(new BigDecimal("2000000"));
//        list.add(d);
//
//        d = new LampiranPembayaranDetailData();
//        d.setNomor(2);
//        d.setKeterangan("Uang Muka");
//        d.setTglJatuhTempo(null);
//        d.setJumlah(new BigDecimal("90000000"));
//        list.add(d);
//
//        d = new LampiranPembayaranDetailData();
//        d.setNomor(3);
//        d.setKeterangan("Angsuran 1");
//        d.setTglJatuhTempo(new Date());
//        d.setJumlah(new BigDecimal("2000000"));
//        list.add(d);
//
//        return list;
//    }
//
//    private Map<String, Object> generateParameter(Acquisition a) {
//        Map<String, Object> p = new HashMap();
//        p.put("unit", generateUnitNames(a));
//        p.put("nama", a.getInvestor().getFullName());
//        p.put("alamat", a.getInvestor().getAddress());
//        p.put("nomorKTP", a.getInvestor().getNationalId());
//        return p;
//    }
//    private String generateUnitNames(Acquisition a) {
//        StringBuilder unitNames = new StringBuilder();
//        List<String> unitNameList = a.getInvestments().stream()
//                .map(investment -> investment.getAparkost())
//                .map(aparkost -> aparkost.getName())
//                .collect(Collectors.toList());
//        Collections.sort(unitNameList);
//        for (int i = 0; i < unitNameList.size() - 1; i++) {
//            unitNames.append(unitNameList.get(i)).append(',');
//        }
//        unitNames.append(unitNameList.get(unitNameList.size() - 1));
//        return unitNames.toString();
//    }
}
