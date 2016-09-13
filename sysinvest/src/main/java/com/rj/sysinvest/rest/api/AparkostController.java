/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.rest.api;

import com.rj.sysinvest.dao.AparkostRepository;
import com.rj.sysinvest.model.Aparkost;
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
@RequestMapping(ApiController.PREFIX + "/aparkost")
public class AparkostController {

    @Resource
    private AparkostRepository repo;

    @RequestMapping(value = "/ret/byid", method = RequestMethod.GET)
    public Aparkost retById(@RequestParam Long value, HttpServletRequest request)
            throws ServletException {
        return repo.findOne(value);
    }

    @RequestMapping(value = "/ret", method = RequestMethod.GET)
    public Iterable<Aparkost> ret(HttpServletRequest request)
            throws ServletException {
        return repo.findAll();
    }

}
