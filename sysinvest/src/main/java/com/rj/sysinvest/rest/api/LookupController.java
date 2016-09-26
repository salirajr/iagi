/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.rest.api;


import com.rj.sysinvest.model.util.Lookup;
import com.rj.sysinvest.model.util.LookupRepository;
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
@RequestMapping(ApiController.PREFIX+"/lookup")
public class LookupController {

    @Resource
    private LookupRepository repo;

    @RequestMapping(value = "/ret/findByGroupName", method = RequestMethod.GET)
    public Iterable<Lookup> ret(@RequestParam String value, HttpServletRequest request)
            throws ServletException {
        return repo.findByGroupName(value);
    }

}
