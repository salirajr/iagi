package com.rj.sysinvest.dao;

import com.rj.sysinvest.model.Investment;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author salirajr
 */
public interface InvestmentRepository extends CrudRepository<Investment, Long> {

    @Query("from Investment where aparkost.tower.id = :towerId and flag = '1'")
    public List<Investment> findOnSaleByTowerId(@Param("towerId") Long towerId);
    
    @Query("from Investment where aparkost.tower.id = :towerId and aparkost.floor = :floor and flag != '3' ")
    public List<Investment> findOnByFloorOfTower(@Param("towerId") Long towerId, @Param("floor") String floor);
    
    @Query("from Investment where aparkost.tower.id = :towerId and aparkost.floor = :floor and flag != '3' and aparkost.index = :index order by id")
    public Investment findOnByAparkostIndex(@Param("towerId") Long towerId, @Param("floor") String floor, @Param("index") Long index);
}
