package com.rj.sysinvest.dao;

import com.rj.sysinvest.model.Tower;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
//@Repository
public interface TowerRepository extends CrudRepository<Tower, Long> {
    
    public List<Tower> findBySiteId(Long id);

}
