/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.rest.api;

import com.rj.sysinvest.dao.TowerRepository;
import com.rj.sysinvest.model.Tower;
import java.util.List;
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
@RequestMapping(ApiController.PREFIX + "/tower")
public class TowerController {

    @Resource
    private TowerRepository repo;

    @RequestMapping(value = "/ret", method = RequestMethod.GET)
    public Iterable<Tower> ret(@RequestParam String key, HttpServletRequest request)
            throws ServletException {
        Iterable<Tower> result = null;
        switch (key.toLowerCase()) {
            default:
                result = repo.findAll();
        }
        return result;
    }

    @RequestMapping(value = "/ret/bysiteid", method = RequestMethod.GET)
    public List<Tower> retBySiteId(@RequestParam Long value, HttpServletRequest request)
            throws ServletException {
        return repo.findBySiteId(value);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Tower save(@RequestBody Tower payload, HttpServletRequest request)
            throws ServletException {
        repo.save(payload);
        return payload;
    }

}
