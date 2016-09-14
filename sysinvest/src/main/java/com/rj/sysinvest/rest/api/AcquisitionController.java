/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.rest.api;

import com.rj.sysinvest.akad.AkadReportService;
import com.rj.sysinvest.akad.docx.AkadDocxService;
import com.rj.sysinvest.dao.AcquisitionRepository;
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.service.AcquisitionService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author salirajr
 */
@RestController
@RequestMapping(ApiController.PREFIX + "/acquisition")
public class AcquisitionController {

    @Resource
    private AcquisitionRepository repo;

    @Resource
    private AcquisitionService service;

    @Resource
    private AkadDocxService akadDocxService;

    @RequestMapping(value = "/ret/byid", method = RequestMethod.GET)
    public Acquisition retById(@RequestParam Long value, HttpServletRequest request)
            throws ServletException {
        return repo.findOne(value);
    }
    
    @RequestMapping(value = "/ret", method = RequestMethod.GET)
    public Iterable<Acquisition> ret(HttpServletRequest request)
            throws ServletException {
        return repo.findAll();
    }

    @RequestMapping(value = "/addnew", method = RequestMethod.POST)
    public Acquisition addnew(@RequestBody Acquisition payload, HttpServletRequest request)
            throws ServletException {
        service.addNew(payload);
        return payload;
    }
    
    @ResponseBody
    @RequestMapping(value = "/generateakad", method = RequestMethod.POST)
    public ResponseEntity<InputStreamResource> generateAkad(@RequestParam Long id, HttpServletRequest request)
            throws ServletException {
        Acquisition t = repo.findOne(id);

        byte[] result;
        try {
            result = akadDocxService.generateAkadDocx(t);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + t.getInvestor().getFullName() + ".docx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                .contentLength(result.length)
                .body(new InputStreamResource(new ByteArrayInputStream(result)));
    }

}
