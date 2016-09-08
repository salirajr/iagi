package com.rj.sysinvest.akad;

import com.rj.sysinvest.akad.LampiranPembayaranData.Detail;
import com.rj.sysinvest.akad.LampiranPembayaranData.Header;
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Aparkost;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public abstract class LampiranPembayaranDataService {

    public abstract List<Detail> generateDetails(Acquisition acquisition);

    public LampiranPembayaranData generatePembayaranReportData(Acquisition acquisition) {
        LampiranPembayaranData data = new LampiranPembayaranData();
        data.setHeader(generateHeader(acquisition));
        data.setDetails(generateDetails(acquisition));
        return data;
    }

    private Header generateHeader(Acquisition a) {
        Header h = new Header();
        List<Aparkost> list = a.getInvestments().stream()
                .map(i -> i.getAparkost())
                .collect(Collectors.toList());
        h.setUnit(generateUnitNames(list));
        h.setNama(a.getInvestor().getFullName());
        h.setAlamat(a.getInvestor().getAddress());
        h.setNomorKTP(a.getInvestor().getNationalId());
        return h;
    }

    private String generateUnitNames(List<Aparkost> aparkostList) {
        StringBuilder unitNames = new StringBuilder();
        List<String> unitNameList = aparkostList.stream()
                .map(aparkost -> aparkost.getName())
                .sorted()
                .collect(Collectors.toList());
        for (int i = 0; i < unitNameList.size() - 1; i++) {
            unitNames.append(unitNameList.get(i)).append(',');
        }
        unitNames.append(unitNameList.get(unitNameList.size() - 1));
        return unitNames.toString();
    }

}
