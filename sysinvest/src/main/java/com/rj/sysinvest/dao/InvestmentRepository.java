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

    @Query("from Investment where aparkost.tower.id = :towerId and state = 'ON_SALE'")
    public List<Investment> findOnSaleByTowerId(@Param("towerId") Long towerId);
    

}
