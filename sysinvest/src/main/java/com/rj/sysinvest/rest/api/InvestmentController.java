/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.rest.api;

import com.rj.sysinvest.dao.InvestmentRepository;
import com.rj.sysinvest.dao.InvestorRepository;
import com.rj.sysinvest.model.Investment;
import com.rj.sysinvest.model.Investor;
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
@RequestMapping("/investment")
public class InvestmentController {

    @Resource
    private InvestmentRepository repo;

    @RequestMapping(value = "/ret/onsale/bytowerid", method = RequestMethod.GET)
    public List<Investment> retByAccountId(@RequestParam Long value, HttpServletRequest request)
            throws ServletException {
        List<Investment> result = repo.findOnSaleByTowerId(value);
        return result;
    }

}
