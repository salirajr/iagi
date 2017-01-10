/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.service;

import com.rj.sysinvest.dao.InvestorRepository;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author salirajr
 */
@Service
public class InvestorService {

    @Autowired
    EntityManager manager;
    
    @Resource
    private InvestorRepository repo;
    
}
