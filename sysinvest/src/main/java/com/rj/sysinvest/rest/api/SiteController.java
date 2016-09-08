/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.rest.api;

import com.rj.sysinvest.dao.SiteRepository;
import com.rj.sysinvest.model.Site;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author salirajr
 */
@RestController
@RequestMapping(ApiController.PREFIX+"/site")
public class SiteController {

    @Resource
    private SiteRepository repo;

    @RequestMapping(value = "/ret", method = RequestMethod.GET)
    public Iterable<Site> ret(@RequestParam String key, HttpServletRequest request)
            throws ServletException {
        Iterable<Site> result = null;
        switch (key.toLowerCase()) {
            default:
                result = repo.findAll();
        }
        return result;
    }
    
    

}
