package com.rj.sysinvest.akad;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
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
import org.springframework.stereotype.Component;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Component
@Data
public class JasperComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(JasperComponent.class);
    private boolean alwaysCompile = false;

    protected JasperReport loadReport(String jrxmlFilePathString) throws JRException, IOException {
        Path pathJasper = Paths.get(jrxmlFilePathString + ".jasper");
        if (!alwaysCompile && Files.exists(pathJasper)) {
            LOGGER.debug("start load {}", jrxmlFilePathString, ".jasper");
            JasperReport jr = (JasperReport) JRLoader.loadObject(pathJasper.toFile());
            LOGGER.debug("finish load {}", jrxmlFilePathString, ".jasper");
            return jr;
        }
        Path pathJrxml = Paths.get(jrxmlFilePathString);
        // load the jrxml design
        LOGGER.debug("start load {}", jrxmlFilePathString);
        JasperDesign design = JRXmlLoader.load(pathJrxml.toFile());
        LOGGER.debug("finish load {}", jrxmlFilePathString);
        // compile to bytes and save it to file and load from the bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LOGGER.debug("start compile {}", jrxmlFilePathString);
        JasperCompileManager.compileReportToStream(design, baos);
        LOGGER.debug("finish compile {}", jrxmlFilePathString);
        // save it to file and load from the bytes
        LOGGER.debug("start write to file {}", pathJasper);
        byte[] bytes = baos.toByteArray();
        Files.write(pathJasper, bytes);
        LOGGER.debug("finish write to file {}", pathJasper);
        // load the bytes 
        LOGGER.debug("start write the bytes to inputStream");
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        LOGGER.debug("finish write the bytes to inputStream");
        LOGGER.debug("start load from inputStream");
        JasperReport jr = (JasperReport) JRLoader.loadObject(in);
        LOGGER.debug("finish load from inputStream");
        return jr;
    }

    // Fill the report with the Data
    protected JasperPrint fillReport(JasperReport report, Map<String, Object> parameters, List dataList) throws JRException {
        JRDataSource jRDataSource = new JRBeanCollectionDataSource(dataList);
        return JasperFillManager.fillReport(report, parameters, jRDataSource);
    }

    public byte[] exportToPdf(JasperPrint filledAkad) throws JRException {
        // exporter for .pdf
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(filledAkad));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
        // perform the export
        exporter.exportReport();
        return baos.toByteArray();
    }

    public byte[] loadFillExportToPdf(String jrxmlFilePathString, Map<String, Object> parameters, List dataList) throws IOException, JRException {
        JasperReport jasperReport = loadReport(jrxmlFilePathString);
        JasperPrint jasperPrint = fillReport(jasperReport, parameters, dataList);
        return exportToPdf(jasperPrint);
    }

}
