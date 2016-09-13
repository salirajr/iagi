package com.rj.sysinvest.akad;

import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Investor;
import com.rj.sysinvest.model.Staff;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service
public class AkadFormDataMapperImpl implements AkadFormDataMapper {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public AkadFormData apply(Acquisition a) {
        AkadFormData d = new AkadFormData();

        Staff s = a.getStaff();
        d.setPihakPertamaNama(s.getFullName());
        d.setPihakPertamaCompany("PT.IBNU AUF GLOBAL INVESTAMA");
        d.setPihakPertamaKTP(s.getNationalId());
        d.setPihakPertamaAlamat(s.getAddress());
        d.setPihakPertamaTTL(s.getBirthPlace() + ", " + dateFormat.format(s.getBirthDate()));

        Investor i = a.getInvestor();
        d.setPihakKeduaNama(i.getFullName());
        d.setPihakKeduaPekerjaan(i.getOccupation());
        d.setPihakKeduaAlamat(i.getAddress());
        d.setPihakKeduaKTP(i.getNationalId());
        d.setPihakKeduaTTL(i.getBirthPlace() + ", " + dateFormat.format(i.getBirthDate()));

//        d.setKuasaNama("RAIS");
//        d.setKuasaAlamat("MAKASSAR");
//        d.setKuasaJabatan("PRESIDEN");
//        d.setKuasaKTP("0987654321");
//        d.setKuasaTTL("Sungguminasa, 01 Maret 1986");
//        d.setLantaiTowerNomor("Lantai 1, tower 1, unit 1 2 3 4");
        d.setHarga(String.valueOf(a.getRate()));
//        d.setHargaTerbilang(String.valueOf(a.getTotalFee()));
//        d.setCaraPembayaran("asdfg");
        d.setTglPemesanan(dateFormat.format(a.getAuditTime()));

//        // Map<TowerName, Map<Floor, List<AparkostName>>>
//        Map<String, Map<String, List<String>>> towerFloorUnitMap = new HashMap();
//        d.setTowerFloorUnitMap(towerFloorUnitMap);
//        a.getInvestments().stream()
//                .map(inv -> inv.getAparkost())
//                .forEach(aparkost -> {
//                    String towerName = aparkost.getTower().getName();
//                    Map<String, List<String>> floorUnitMap = towerFloorUnitMap.get(towerName);
//                    if (floorUnitMap == null) {
//                        floorUnitMap = new HashMap();
//                        towerFloorUnitMap.put(towerName, floorUnitMap);
//                    }
//                    String floor = aparkost.getFloor();
//                    List<String> unitList = floorUnitMap.get(floor);
//                    if (unitList == null) {
//                        unitList = new ArrayList();
//                        floorUnitMap.put(floor, unitList);
//                    }
//                    String unitName = aparkost.getName();
//                    unitList.add(unitName);
//                });

        return d;
    }
}
