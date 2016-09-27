/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.rest.api;

import com.rj.sysinvest.dao.RankRepository;
import com.rj.sysinvest.model.Rank;
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
@RequestMapping(ApiController.PREFIX+"/rank")
public class RankController {

    @Resource
    private RankRepository repo;

    @RequestMapping(value = "/ret", method = RequestMethod.GET)
    public Iterable<Rank> ret(@RequestParam String key, HttpServletRequest request)
            throws ServletException {
        Iterable<Rank> result;
        switch (key.toLowerCase()) {
            default:
                result = repo.findAll();
        }
        return result;
    }
    
    

}
