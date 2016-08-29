package com.rj.sysinvest.akad;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Data;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Service
@Data
public class AkadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AkadService.class);

    private JasperReport akadTemplate;
    @Value("${akadTemplateFilename}")
    private String akadTemplateFilename;

    @PostConstruct
    protected void postConstruct() {
        try {
            setAkadTemplate(loadAkadTemplate());
        } catch (JRException | IOException ex) {
            LOGGER.error("Failed to load akad template", ex);
        }
    }

    protected JasperReport loadAkadTemplate() throws JRException, IOException {
        Path pathJasper = Paths.get(getAkadTemplateFilename() + ".jasper");
        if (Files.exists(pathJasper)) {
            return (JasperReport) JRLoader.loadObject(pathJasper.toFile());
        }
        Path pathJrxml = Paths.get(getAkadTemplateFilename() + ".jrxml");
        if (!Files.exists(pathJrxml)) {
            throw new FileNotFoundException(pathJrxml.toString());
        }
        JasperDesign design = JRXmlLoader.load(pathJrxml.toFile());

//        // compile to file and load from file
//        JasperCompileManager.compileReportToFile(design, pathJasper.toString());
//        return (JasperReport) JRLoader.loadObject(pathJasper.toFile());
//
        // compile to bytes and save it to file and load from the bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JasperCompileManager.compileReportToStream(design, baos);
        Files.write(pathJasper, baos.toByteArray());

        PipedInputStream in = new PipedInputStream();
        baos.writeTo(new PipedOutputStream(in));
        return (JasperReport) JRLoader.loadObject(in);
    }

    // Fill the report
    protected JasperPrint fillAkad(List<AkadDataBean> dataList) throws JRException {
        JRDataSource jRDataSource = new JRBeanCollectionDataSource(dataList);
        Map parameters = new HashMap();
        return JasperFillManager.fillReport(getAkadTemplate(), parameters, jRDataSource);
    }

    public byte[] generateAkadPDF(List<AkadDataBean> dataList) throws Exception {
        JasperPrint filledAkad = fillAkad(dataList);
        // exporter for .pdf
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(filledAkad));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
        // perform the export
        exporter.exportReport();
        return baos.toByteArray();
    }

}
