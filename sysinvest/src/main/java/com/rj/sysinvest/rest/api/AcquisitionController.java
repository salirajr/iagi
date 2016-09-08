/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.rest.api;

import com.rj.sysinvest.akad.AkadReportService;
import com.rj.sysinvest.dao.AcquisitionRepository;
import com.rj.sysinvest.model.Acquisition;
import com.rj.sysinvest.service.AcquisitionService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@RequestMapping("/acquisition")
public class AcquisitionController {

    @Resource
    private AcquisitionRepository repo;

    @Resource
    private AcquisitionService service;

    @Resource
    private AkadReportService akadReportService;

    @RequestMapping(value = "/ret/byid", method = RequestMethod.GET)
    public Acquisition retById(@RequestParam Long value, HttpServletRequest request)
            throws ServletException {
        return repo.findOne(value);
    }

    @RequestMapping(value = "/addnew", method = RequestMethod.POST)
    public Long addnew(@RequestBody Acquisition payload, HttpServletRequest request)
            throws ServletException {
        service.save(payload);
        return payload.getId();
    }

    @ResponseBody
    @RequestMapping(value = "/generateakad", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> generateAkad(@RequestParam Long id, HttpServletRequest request)
            throws ServletException {
        Acquisition t = repo.findOne(id);

        byte[] result;
        try {
            result = akadReportService.generateCompleteAkadPdf(t);
        } catch (IOException | JRException ex) {
            throw new RuntimeException(ex);
        }
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + t.getInvestor().getFullName() + ".pdf")
                .contentLength(result.length)
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_PDF_VALUE))
                .body(new InputStreamResource(new ByteArrayInputStream(result)));
    }

}
