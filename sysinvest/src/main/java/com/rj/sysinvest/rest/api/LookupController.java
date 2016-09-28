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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(ApiController.PREFIX + "/lookup")
public class LookupController {

    @Resource
    private LookupRepository repo;

    @RequestMapping(value = "/ret/findByGroupName", method = RequestMethod.GET)
    public Iterable<Lookup> findByGroupName(@RequestParam String value, HttpServletRequest request)
            throws ServletException {
        return repo.findByGroupName(value);
    }

    @RequestMapping(value = "/ret/listGroupName", method = RequestMethod.GET)
    public Iterable<String> listGroupName(HttpServletRequest request)
            throws ServletException {
        return repo.listGroupName();
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Lookup save(@RequestBody Lookup payload, HttpServletRequest request)
            throws ServletException {
        repo.save(payload);
        return payload;
    }
    
    @RequestMapping(value = "/rem", method = RequestMethod.POST)
    public ResponseEntity rem(@RequestBody Long id, HttpServletRequest request)
            throws ServletException {
        repo.delete(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}
