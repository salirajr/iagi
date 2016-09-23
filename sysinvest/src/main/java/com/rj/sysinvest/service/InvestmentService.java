/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.service;

import ch.qos.logback.core.CoreConstants;
import com.rj.sysinvest.dao.AparkostRepository;
import com.rj.sysinvest.dao.InvestmentRepository;
import com.rj.sysinvest.model.Aparkost;
import com.rj.sysinvest.model.Investment;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author salirajr
 */
@Service
public class InvestmentService {
    
    @Autowired
    EntityManager manager;
    
    @Resource
    private InvestmentRepository repo;
    
    @Resource
    private AparkostRepository repoAparkost;
    
    public Investment retById(Long id) {
        Investment result = repo.findOne(id);
        return result;
    }
  
    @Transactional
    public Investment save(Investment payload) {
        System.out.println("payload.getAparkost(): "+payload.getAparkost());
        System.out.println("payload.getAparkost().getId(): "+payload.getAparkost().getId());
        if (payload.getAparkost().getId() == null) {
            Aparkost temp = repoAparkost.findByTowerAndName(payload.getAparkost().getTower(), payload.getAparkost().getName());
            if (temp == null) {
                repoAparkost.save(payload.getAparkost());
            } else {
                payload.setAparkost(temp);
            }
            
        }
        
        repo.save(payload);
        return payload;
    }
}
