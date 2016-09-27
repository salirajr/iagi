/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.rest.api;

import com.rj.sysinvest.dao.StaffRepository;
import com.rj.sysinvest.model.Investor;
import com.rj.sysinvest.model.Staff;
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
@RequestMapping(ApiController.PREFIX+"/staff")
public class StaffController {

    @Resource
    private StaffRepository repo;

    @RequestMapping(value = "/ret", method = RequestMethod.GET)
    public Iterable<Staff> ret(@RequestParam String key, HttpServletRequest request)
            throws ServletException {
        Iterable<Staff> result = null;
        switch (key.toLowerCase()) {
            case "covenant":
                result = repo.getCovenant();
                break;
            default:
                result = repo.findAll();
        }
        return result;
    }
    
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Staff addNew(@RequestBody Staff payload, HttpServletRequest request)
            throws ServletException {
        repo.save(payload);
        return payload;
    }

}
