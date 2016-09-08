/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.service;

import com.rj.sysinvest.dao.AcquisitionRepository;
import com.rj.sysinvest.dao.AparkostRepository;
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Aparkost;
import com.rj.sysinvest.model.Investor;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author salirajr
 */
@Service
public class AcquisitionService {
    
    @Autowired
    AcquisitionRepository repoAcquisition;
   
    
    @Autowired
    AparkostRepository repoAparkost;
    
    @Autowired
    EntityManager manager;
    
    
    @Transactional
    public Acquisition save(Acquisition payload){
        
        repoAcquisition.save(payload);
        Investor investor = payload.getInvestor();
        payload.getInvestments().forEach(investment->{
            Aparkost t = investment.getAparkost();
            t.setInvestor(investor);
            repoAparkost.save(t);
        });
        return payload;
    }
    
}
