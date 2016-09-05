/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.rest.api;

import com.rj.sysinvest.dao.AcquisitionRepository;
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.service.AcquisitionService;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/acquisition")
public class AcquisitionController {

    @Resource
    private AcquisitionRepository repo;
    
    @Autowired
    private AcquisitionService service;

    @RequestMapping(value = "/ret/byid", method = RequestMethod.GET)
    public Acquisition ret(@RequestParam Long value, HttpServletRequest request)
            throws ServletException {
        return repo.findOne(value);
    }
    
    @RequestMapping(value = "/addnew", method = RequestMethod.POST)
    public Acquisition addnew(@RequestBody Acquisition payload, HttpServletRequest request)
            throws ServletException {
        System.out.println(payload.getDpFee());
        return service.save(payload);
    }

}
