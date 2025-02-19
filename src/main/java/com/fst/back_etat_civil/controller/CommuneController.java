package com.fst.back_etat_civil.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fst.back_etat_civil.dto.CercleDto;
import com.fst.back_etat_civil.dto.CommuneDto;
import com.fst.back_etat_civil.model.Cercle;
import com.fst.back_etat_civil.model.Citoyen;
import com.fst.back_etat_civil.model.Commune;
import com.fst.back_etat_civil.model.Vqf;
import com.fst.back_etat_civil.repository.CercleRepository;
import com.fst.back_etat_civil.repository.CommuneRepository;
import com.fst.back_etat_civil.service.CercleService;
import com.fst.back_etat_civil.service.CommuneService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/commune")
public class CommuneController {

    @Autowired
    private CommuneService communeService;
    @Autowired
    CercleRepository cercleRepository;
    @Autowired
    private CercleService cercleService;
    @Autowired
    private CommuneRepository communeRepository;

    @GetMapping("")
    public ResponseEntity<List<CommuneDto>> getAllCommunes() {
        List<CommuneDto> communes = communeService.getAllCommunes();
        return new ResponseEntity<>(communes, HttpStatus.OK);
    }
    
    @GetMapping("/getCommunesByCercle/{id}")
    public ResponseEntity<List<CommuneDto>> getCommunesByCercle(@PathVariable long id) {
        List<CommuneDto> communes = communeService.getCommunesByCercle(id);
        return new ResponseEntity<>(communes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommuneDto> getCommuneById(@PathVariable long id) {
        CommuneDto commune = communeService.getCommuneById(id);
        return new ResponseEntity<>(commune, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommuneDto> createCommune(@RequestBody CommuneDto commune) {
    	if (communeRepository.existsByCode(commune.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce code de commune existe déjà");
        }
    	else {
        try {
            Optional<Cercle> cercle=cercleRepository.findById(commune.getCercle());
            Commune commune1= new Commune();
            //MAPPING
            commune1.setAutre(commune.getAutre());
            commune1.setNom(commune.getNom());
            commune1.setCode(commune.getCode());

            commune1.setCercle(cercle.get());
            //END MAPPING
            if(!cercle.isPresent())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"CERCLE NOT FOUND");

            Commune _commune = communeRepository
                    .save(commune1);
            commune.setId(_commune.getId());

            return new ResponseEntity<>(commune, HttpStatus.CREATED);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
       }
    }
    
    @PostMapping("/payes")
    public ResponseEntity<CommuneDto> createPayes(@RequestBody CommuneDto commune) {
    	if (communeRepository.existsByNom(commune.getNom())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce nom de payes existe déjà");
        }
    	else {
        try {
            Optional<Cercle> cercle=cercleRepository.findById(commune.getCercle());
            Commune commune1= new Commune();
            //MAPPING
            commune1.setAutre(commune.getAutre());
            commune1.setNom(commune.getNom());
            commune1.setCode(GeneCode(cercle.get()));

            commune1.setCercle(cercle.get());
            //END MAPPING
            if(!cercle.isPresent())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"CERCLE NOT FOUND");

            Commune _commune = communeRepository
                    .save(commune1);
            commune.setId(_commune.getId());

            return new ResponseEntity<>(commune, HttpStatus.CREATED);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommuneDto> updateCommune(@PathVariable long id, @RequestBody CommuneDto communeDto) {
    	if (communeRepository.existsByCode(communeDto.getCode()) 
        		&& !communeRepository.findById(id).get().getCode().equals(communeDto.getCode())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce code de commune existe déjà");
            }
        	else {
        CommuneDto updatedCommune = communeService.updateCommune(id, communeDto);
        return new ResponseEntity<>(updatedCommune, HttpStatus.OK);
            }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommune(@PathVariable long id) {
        communeService.deleteCommune(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<Commune>> search(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<Commune> communes = communeService.searchCommunes(keyword, page, size);
        return ResponseEntity.ok(communes);
    }
    
    @GetMapping("/searchP")
    public ResponseEntity<Page<Commune>> searchP(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<Commune> communes = communeService.searchPayes(keyword, page, size);
        return ResponseEntity.ok(communes);
    }
    
    public String GeneCode(Cercle cercle) {
    	List<Commune>  communes=communeRepository.findByCercle(cercle);
    	long seq=1;
    	for (Commune c : communes) seq++;
			
		return cercle.getCode()+String.valueOf(String.format("%02d",seq));
    }
}
