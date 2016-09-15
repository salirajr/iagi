package com.rj.sysinvest.dao;


import com.rj.sysinvest.model.Investor;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */


public interface InvestorRepository extends CrudRepository<Investor, Long> {
    
    public Investor findByAccountId(String accountId);

}
