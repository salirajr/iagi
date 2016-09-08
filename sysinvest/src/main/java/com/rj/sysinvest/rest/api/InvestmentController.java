/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.rest.api;

import com.rj.sysinvest.dao.InvestmentRepository;
import com.rj.sysinvest.model.Investment;
import com.rj.sysinvest.service.InvestmentService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author salirajr
 */
@RestController
@RequestMapping(ApiController.PREFIX+"/investment")
public class InvestmentController {

    @Resource
    private InvestmentRepository repo;
    
    @Autowired
    private InvestmentService service;

    @RequestMapping(value = "/ret/onsale/bytowerid", method = RequestMethod.GET)
    public List<Investment> retByAccountId(@RequestParam Long value, HttpServletRequest request)
            throws ServletException {
        List<Investment> result = repo.findOnSaleByTowerId(value);
        return result;
    }
    
    @RequestMapping(value = "/ret/byid", method = RequestMethod.GET)
    public Investment retById(@RequestParam Long value, HttpServletRequest request)
            throws ServletException {
        return service.retById(value);
    }

}
