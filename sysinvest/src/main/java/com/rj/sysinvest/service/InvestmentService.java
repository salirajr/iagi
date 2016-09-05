/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.service;

import com.rj.sysinvest.dao.InvestmentRepository;
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.model.Investment;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
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
    
    public Investment retById(Long id){
        Investment result = repo.findOne(id);
//        if(result.getAcquisition() != null && result.getAcquisition().getId() != null){
//             Acquisition acquisition = manager.find(Acquisition.class, result.getId());
//             result.setAcquisition(acquisition);
//             System.out.println(result.getAcquisition().getEndDate());
//        }
        return result;
    }
}
