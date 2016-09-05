/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.service;

import com.rj.sysinvest.dao.AcquisitionRepository;
import com.rj.sysinvest.dao.InvestmentRepository;
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Investor;
import java.util.Collection;
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
    InvestmentRepository repoInvestment;
    
    @Autowired
    EntityManager manager;
    
    
    @Transactional
    public Acquisition save(Acquisition payload){
        
        repoAcquisition.save(payload);
//        payload.getInvestments().forEach(investment->{
//            investment.setAcquisition(payload);
//            repoInvestment.save(investment);
//        });
        
        return payload;
    }
    
}