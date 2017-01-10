/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.rest.api;

import com.rj.sysinvest.dao.BookingRepository;
import com.rj.sysinvest.model.Aparkost;
import com.rj.sysinvest.model.Booking;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author salirajr
 */
@RestController
@RequestMapping(ApiController.PREFIX + "/booking")
public class BookingController {

    @Resource
    private BookingRepository repo;

    @RequestMapping(value = "/addnew", method = RequestMethod.POST)
    public Booking addnew(@RequestBody Booking payload, HttpServletRequest request)
            throws ServletException {
        System.out.println(payload);
        repo.save(payload);
        payload.setBookCode(genId(payload.getBroker().getId(), payload.getId()));
        return payload;
    }

    private String genId(Long staffId, Long inc) {
        DateFormat dateFormat = new SimpleDateFormat("yy-MM");
        String fId = String.format("%02d", staffId);
        Date date = new Date();
        return "BS" + fId + dateFormat.format(date)+"."+inc;
    }

}
