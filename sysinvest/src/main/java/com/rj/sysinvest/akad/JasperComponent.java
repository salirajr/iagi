package com.rj.sysinvest.akad;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
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

    protected JasperReport loadReport(String jrxmlFilePathString) throws JRException, IOException {
        Path pathJasper = Paths.get(jrxmlFilePathString + ".jasper");
        if (Files.exists(pathJasper)) {
            LOGGER.debug("load {}", jrxmlFilePathString, ".jasper");
            return (JasperReport) JRLoader.loadObject(pathJasper.toFile());
        }
        LOGGER.debug("load {}", jrxmlFilePathString);
        Path pathJrxml = Paths.get(jrxmlFilePathString);
        // load the jrxml design
        JasperDesign design = JRXmlLoader.load(pathJrxml.toFile());

        // compile to bytes and save it to file and load from the bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JasperCompileManager.compileReportToStream(design, baos);
        // save it to file and load from the bytes
        Files.write(pathJasper, baos.toByteArray());
        // load the bytes
        PipedInputStream in = new PipedInputStream();
        baos.writeTo(new PipedOutputStream(in));
        return (JasperReport) JRLoader.loadObject(in);
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
