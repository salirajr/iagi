/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.rest.api;

import com.rj.sysinvest.dao.InvestorRepository;
import com.rj.sysinvest.model.Investor;
import com.rj.sysinvest.service.InvestorService;
import java.io.IOException;
import java.util.Collection;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author salirajr
 */
@RestController
@RequestMapping(ApiController.PREFIX + "/investor")
public class InvestorController {

    @Resource
    private InvestorRepository repo;
    
    @Resource
    InvestorService service;

    
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

    @RequestMapping(value = "/storeidentitycopy", method = RequestMethod.POST)
    public Investor storeIdentityCopy(@RequestParam("file") MultipartFile file, @RequestParam String value, HttpServletRequest request)
            throws ServletException, IOException {
        byte[] baFile = file.getBytes();
        return repo.findByAccountId(value);
    }

    @RequestMapping(value = "/ret", method = RequestMethod.GET)
    public Iterable<Investor> ret(HttpServletRequest request)
            throws ServletException {
        return repo.findAll();
    }

}
