/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.rest.api;

import com.rj.sysinvest.dao.AcquisitionRepository;
import com.rj.sysinvest.model.Acquisition;
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
@RequestMapping("/acquisition")
public class AcquisitionController {

    @Resource
    private AcquisitionRepository repo;

    @RequestMapping(value = "/ret/byid", method = RequestMethod.GET)
    public Acquisition ret(@RequestParam Long value, HttpServletRequest request)
            throws ServletException {
        return repo.findOne(value);
    }

}
