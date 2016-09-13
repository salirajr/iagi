package com.rj.sysinvest.dao;

import com.rj.sysinvest.model.Aparkost;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public interface AparkostRepository extends CrudRepository<Aparkost, Long> {

    @Query("from Aparkost a where a.tower.id = :towerId")
    List<Aparkost> findByTowerId(@Param("towerId") Long towerId);

    @Query("from Aparkost a where a.tower.id = :towerId and a.floor = :floor")
    List<Aparkost> findByTowerIdAndFloor(@Param("towerId") Long towerId, @Param("floor") String floor);
    

}
