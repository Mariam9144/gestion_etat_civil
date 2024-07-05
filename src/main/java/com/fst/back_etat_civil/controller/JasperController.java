package com.fst.back_etat_civil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.fst.back_etat_civil.dto.CitoyenDto;
import com.fst.back_etat_civil.dto.*;
import com.fst.back_etat_civil.service.*;
import com.fst.back_etat_civil.service.*;

import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/jasper")
public class JasperController {

    @Autowired
    private CitoyenService citoyenService;
    @Autowired
    private VqfService vqfService;
    @Autowired
    private CommuneService communeService;
    @Autowired
    private CercleService cercleService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private ProfessionService professionService;
    @Autowired
    private ImageController imageController;
    

    @Autowired
    private JasperService recuService;

    @GetMapping("/recu/{id}")
    public ResponseEntity<byte[]> generateRecu(@PathVariable Long id) throws IOException {
        // Récupérer les détails du citoyen
        CitoyenDto citoyen = citoyenService.getCitoyenById(id);
        VqfDto adresse = vqfService.getVqfById(citoyen.getAdresse());
        VqfDto lieu = vqfService.getVqfById(citoyen.getLieuNaissance());
        CommuneDto communeA = communeService.getCommuneById(adresse.getCommune());
        CommuneDto commune = communeService.getCommuneById(lieu.getCommune());
        CercleDto cercleA = cercleService.getCercleById(communeA.getCercle());
        CercleDto cercle = cercleService.getCercleById(commune.getCercle());
        RegionDto regionA = regionService.getRegionById(cercleA.getRegion());
        RegionDto region = regionService.getRegionById(cercle.getRegion());
        
        

        // Créer des paramètres pour le rapport JasperReports
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", citoyen.getId());
        parameters.put("nom", citoyen.getNom());
        parameters.put("prenom", citoyen.getPrenom());
        parameters.put("niciv", citoyen.getNiciv());
        parameters.put("telephone", citoyen.getTelephone());
        parameters.put("civilite", citoyen.getCivilite());
        parameters.put("prenomPere", citoyen.getPrenomPere());
        parameters.put("nomMere", citoyen.getNomMere());
        parameters.put("prenomMere", citoyen.getPrenomMere());
        parameters.put("dateNaissance", citoyen.getDateNaissance());
        
        parameters.put("region", region.getNom());
        parameters.put("cercle", cercle.getNom());
        parameters.put("commune", commune.getNom());
        parameters.put("adresse", adresse.getNom());
        
        parameters.put("regionA", regionA.getNom());
        parameters.put("cercleA", cercleA.getNom());
        parameters.put("communeA", communeA.getNom());
        parameters.put("lieuNaissance", lieu.getNom());
        parameters.put("toDay", new Date());
        
        
        // Ajoutez d'autres paramètres si nécessaire

        // Générer le reçu
        try {
            return recuService.generateRecu(parameters);
        } catch (JRException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la génération du reçu", e);
        }
    }
    
    @GetMapping("/carte/{id}")
    public ResponseEntity<byte[]> generateCarte(@PathVariable Long id) throws IOException {
        // Récupérer les détails du citoyen
        CitoyenDto citoyen = citoyenService.getCitoyenById(id);
        VqfDto adresse = vqfService.getVqfById(citoyen.getAdresse());
        VqfDto lieu = vqfService.getVqfById(citoyen.getLieuNaissance());
        CommuneDto communeA = communeService.getCommuneById(adresse.getCommune());
        CommuneDto commune = communeService.getCommuneById(lieu.getCommune());
        CercleDto cercleA = cercleService.getCercleById(communeA.getCercle());
        CercleDto cercle = cercleService.getCercleById(commune.getCercle());
        RegionDto regionA = regionService.getRegionById(cercleA.getRegion());
        RegionDto region = regionService.getRegionById(cercle.getRegion());
        ProfessionDto profession = professionService.getProfessionById(citoyen.getProfession());

        // Créer des paramètres pour le rapport JasperReports
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", citoyen.getId());
        parameters.put("nom", citoyen.getNom());
        parameters.put("prenom", citoyen.getPrenom());
        parameters.put("niciv", citoyen.getNiciv());
        parameters.put("telephone", citoyen.getTelephone());
        parameters.put("civilite", citoyen.getCivilite());
        parameters.put("prenomPere", citoyen.getPrenomPere());
        parameters.put("nomMere", citoyen.getNomMere());
        parameters.put("prenomMere", citoyen.getPrenomMere());
        parameters.put("dateNaissance", citoyen.getDateNaissance());
        
        parameters.put("region", region.getNom());
        parameters.put("cercle", (region.getNom().equals("Diaspora"))?commune.getNom():cercle.getNom());
        parameters.put("commune", commune.getNom());
        parameters.put("adresse", adresse.getNom());
        
        parameters.put("regionA", (regionA.getNom().equals("Diaspora"))?communeA.getNom():regionA.getNom());
        parameters.put("cercleA", (regionA.getNom().equals("Diaspora"))?communeA.getNom():cercleA.getNom());
        parameters.put("communeA", communeA.getNom());
        parameters.put("lieuNaissance", lieu.getNom());
        parameters.put("profession", profession.getLibelle());
        parameters.put("imageUrl", "http://localhost:8080/api/citoyen/affichePortrait/"+citoyen.getPortrait());
        parameters.put("sexe", (citoyen.getGenre().equals("Femme"))?"F":"M");
        parameters.put("toDay", new Date());
        // Ajoutez d'autres paramètres si nécessaire

        // Générer le reçu
        try {
            return recuService.generateCarte(parameters);
        } catch (JRException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la génération de la carte", e);
        }
    }
    
    @GetMapping("/fiche/{id}")
    public ResponseEntity<byte[]> generateFicheIndividuelle(@PathVariable Long id) throws IOException {
        // Récupérer les détails du citoyen
        CitoyenDto citoyen = citoyenService.getCitoyenById(id);
        VqfDto adresse = vqfService.getVqfById(citoyen.getAdresse());
        VqfDto lieu = vqfService.getVqfById(citoyen.getLieuNaissance());
        CommuneDto communeA = communeService.getCommuneById(adresse.getCommune());
        CommuneDto commune = communeService.getCommuneById(lieu.getCommune());
        CercleDto cercleA = cercleService.getCercleById(communeA.getCercle());
        CercleDto cercle = cercleService.getCercleById(commune.getCercle());
        RegionDto regionA = regionService.getRegionById(cercleA.getRegion());
        RegionDto region = regionService.getRegionById(cercle.getRegion());
        ProfessionDto profession = professionService.getProfessionById(citoyen.getProfession());
        ProfessionDto professionPere = professionService.getProfessionById(citoyen.getProfessionPere());
        ProfessionDto professionMere = professionService.getProfessionById(citoyen.getProfessionMere());

        // Créer des paramètres pour le rapport JasperReports
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", citoyen.getId());
        parameters.put("nom", citoyen.getNom());
        parameters.put("prenom", citoyen.getPrenom());
        parameters.put("niciv", citoyen.getNiciv());
        parameters.put("telephone", citoyen.getTelephone());
        parameters.put("civilite", citoyen.getCivilite());
        parameters.put("prenomPere", citoyen.getPrenomPere());
        parameters.put("nomMere", citoyen.getNomMere());
        parameters.put("prenomMere", citoyen.getPrenomMere());
        parameters.put("dateNaissance", citoyen.getDateNaissance());
        
        parameters.put("region", !(region.getNom().equals("Diaspora"))? "Region : "+region.getNom() : "");
        parameters.put("cercle", (region.getNom().equals("Diaspora"))? "Continent : "+cercle.getNom() : "Cercle : "+cercle.getNom());
        parameters.put("commune", (region.getNom().equals("Diaspora"))? "Payes : "+commune.getNom() : "Commune : "+commune.getNom());
        parameters.put("adresse", (regionA.getNom().equals("Diaspora"))? "Ville : "+adresse.getNom() :"VQF : "+adresse.getNom());
        parameters.put("rue", citoyen.getRue());
        parameters.put("porte", citoyen.getPorte());
        parameters.put("autre", citoyen.getAutre());
        
        parameters.put("regionA", !(regionA.getNom().equals("Diaspora"))? "Region : "+regionA.getNom() : "");
        parameters.put("cercleA", (regionA.getNom().equals("Diaspora"))? "Continent : "+cercleA.getNom() : "Cercle : "+cercleA.getNom());
        parameters.put("communeA", (regionA.getNom().equals("Diaspora"))? "Payes : "+communeA.getNom() : "Commune : "+communeA.getNom());
        parameters.put("lieuNaissance", (region.getNom().equals("Diaspora"))? "Ville : "+lieu.getNom() : "VQF : "+lieu.getNom());
        parameters.put("profession", profession.getLibelle());
        parameters.put("professionPere", professionPere.getLibelle());
        parameters.put("professionMere", professionMere.getLibelle());
        parameters.put("imageUrl", "http://localhost:8080/api/citoyen/affichePortrait/"+citoyen.getPortrait());
        parameters.put("sexe", citoyen.getGenre());
        parameters.put("toDay", new Date());
        // Ajoutez d'autres paramètres si nécessaire
        
       
        // Générer le reçu
        try {
            return recuService.generateFicheIndividuelle(parameters);
        } catch (JRException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la génération de la fiche", e);
        }
    }
    
    @GetMapping("/nationalite/{id}")
    public ResponseEntity<byte[]> generateNationalite(@PathVariable Long id) throws IOException {
        // Récupérer les détails du citoyen
        CitoyenDto citoyen = citoyenService.getCitoyenById(id);
        VqfDto adresse = vqfService.getVqfById(citoyen.getAdresse());
        VqfDto lieu = vqfService.getVqfById(citoyen.getLieuNaissance());
        CommuneDto communeA = communeService.getCommuneById(adresse.getCommune());
        CommuneDto commune = communeService.getCommuneById(lieu.getCommune());
        CercleDto cercleA = cercleService.getCercleById(communeA.getCercle());
        CercleDto cercle = cercleService.getCercleById(commune.getCercle());
        RegionDto regionA = regionService.getRegionById(cercleA.getRegion());
        RegionDto region = regionService.getRegionById(cercle.getRegion());
        ProfessionDto profession = professionService.getProfessionById(citoyen.getProfession());
        

        // Créer des paramètres pour le rapport JasperReports
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", citoyen.getId());
        parameters.put("nom", citoyen.getNom());
        parameters.put("prenom", citoyen.getPrenom());
        parameters.put("niciv", citoyen.getNiciv());
        parameters.put("telephone", citoyen.getTelephone());
        parameters.put("civilite", citoyen.getCivilite());
        parameters.put("prenomPere", citoyen.getPrenomPere());
        parameters.put("nomMere", citoyen.getNomMere());
        parameters.put("prenomMere", citoyen.getPrenomMere());
        parameters.put("dateNaissance", citoyen.getDateNaissance());
        parameters.put("profession", profession.getLibelle());
        
        parameters.put("region", region.getNom());
        parameters.put("cercle", cercle.getNom());
        parameters.put("commune", commune.getNom());
        parameters.put("adresse", adresse.getNom());
        
        parameters.put("regionA", regionA.getNom());
        parameters.put("cercleA", cercleA.getNom());
        parameters.put("communeA", communeA.getNom());
        parameters.put("lieuNaissance", lieu.getNom());
        parameters.put("toDay", new Date());
        
        
        // Ajoutez d'autres paramètres si nécessaire

        // Générer le reçu
        try {
            return recuService.generateNationalite(parameters);
        } catch (JRException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la génération du nationalite", e);
        }
    }
}

