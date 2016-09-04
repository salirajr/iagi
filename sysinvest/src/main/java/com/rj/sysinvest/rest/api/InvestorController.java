/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.rest.api;


import com.rj.sysinvest.dao.InvestorRepository;
import com.rj.sysinvest.model.Investor;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author salirajr
 */
@RestController
@RequestMapping("/investor")
public class InvestorController {
    
    @Resource
    private InvestorRepository repo;

    @RequestMapping(value = "/addnew", method = RequestMethod.POST)
    public Investor addNew(@RequestBody Investor payload, HttpServletRequest request)
            throws ServletException {
        repo.save(payload);
        return payload;
    }
    
    @RequestMapping(value = "/ret/byaccountid", method = RequestMethod.GET)
    public Investor retByAccountId(@RequestParam String value, HttpServletRequest request)
            throws ServletException {
        return repo.findByAccountId(value);
    }

}
