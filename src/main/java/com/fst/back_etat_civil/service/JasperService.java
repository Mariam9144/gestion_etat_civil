package com.fst.back_etat_civil.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fst.back_etat_civil.dto.CitoyenDto;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.*;

@Service
public class JasperService {
	
    //Generation de recu
	public ResponseEntity<byte[]> generateRecu(Map<String, Object> parameters) throws JRException, IOException {
        JasperReport jasperReport = JasperCompileManager.compileReport(
            new ClassPathResource("reports/recepisse.jrxml").getInputStream());

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=recepisse.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
	
	//Generation de nationalite
		public ResponseEntity<byte[]> generateNationalite(Map<String, Object> parameters) throws JRException, IOException {
	        JasperReport jasperReport = JasperCompileManager.compileReport(
	            new ClassPathResource("reports/nationalite.jrxml").getInputStream());

	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

	        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

	        HttpHeaders headers = new HttpHeaders();
	        headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");
	        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=nationalite.pdf");

	        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	    }
	
	//Generation de carte
		public ResponseEntity<byte[]> generateCarte(Map<String, Object> parameters) throws JRException, IOException {
	        JasperReport jasperReport = JasperCompileManager.compileReport(
	            new ClassPathResource("reports/cartePvc.jrxml").getInputStream());

	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

	        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

	        HttpHeaders headers = new HttpHeaders();
	        headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");
	        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=carte_bio.pdf");

	        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	    }
		
		
		//Generation de fiche individuelle
				public ResponseEntity<byte[]> generateFicheIndividuelle(Map<String, Object> parameters) throws JRException, IOException {
			        JasperReport jasperReport = JasperCompileManager.compileReport(
			            new ClassPathResource("reports/fiche_individuelle.jrxml").getInputStream());

			        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

			        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

			        HttpHeaders headers = new HttpHeaders();
			        headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");
			        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fiche_individuelle.pdf");

			        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
			    }
}

