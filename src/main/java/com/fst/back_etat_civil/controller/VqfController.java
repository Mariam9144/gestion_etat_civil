package com.fst.back_etat_civil.controller;

import java.util.List;

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

import com.fst.back_etat_civil.dto.VqfDto;
import com.fst.back_etat_civil.model.Vqf;
import com.fst.back_etat_civil.repository.VqfRepository;
import com.fst.back_etat_civil.service.VqfService;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/vqf")
public class VqfController {
    @Autowired
    private VqfService vqfService;
    
    @Autowired
    private VqfRepository vqfRepository;

    @GetMapping
    public ResponseEntity<List<VqfDto>> getAllVqfs() {
        List<VqfDto> vqfs = vqfService.getAllVqfs();
        return new ResponseEntity<>(vqfs, HttpStatus.OK);
    }
    
    @GetMapping("/getVqfsByCommune/{id}")
    public ResponseEntity<List<VqfDto>> getVqfsByCommune(@PathVariable long id) {
        List<VqfDto> vqfs = vqfService.getVqfsByCom(id);
        return new ResponseEntity<>(vqfs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VqfDto> getVqfById(@PathVariable Long id) {
        VqfDto vqf = vqfService.getVqfById(id);
        return new ResponseEntity<>(vqf, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VqfDto> createVqf(@RequestBody VqfDto vqfDto) {
    	if (vqfRepository.existsByCode(vqfDto.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce code de vqf existe déjà");
        }
    	else {
        VqfDto createdVqf = vqfService.createVqf(vqfDto);
        return new ResponseEntity<>(createdVqf, HttpStatus.CREATED);
        }
    }
    
    @PostMapping("/ville")
    public ResponseEntity<VqfDto> createVille(@RequestBody VqfDto vqfDto) {
    	if (vqfRepository.existsByNom(vqfDto.getNom())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce nom de Ville existe déjà");
        }
    	else {
        VqfDto createdVqf = vqfService.createVille(vqfDto);
        return new ResponseEntity<>(createdVqf, HttpStatus.CREATED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VqfDto> updateVqf(@PathVariable Long id, @RequestBody VqfDto vqfDto) {
    	if (vqfRepository.existsByCode(vqfDto.getCode()) 
        		&& !vqfRepository.findById(id).get().getCode().equals(vqfDto.getCode())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce code de vqf existe déjà");
            }
        	else {
        VqfDto updatedVqf = vqfService.updateVqf(id, vqfDto);
        return new ResponseEntity<>(updatedVqf, HttpStatus.OK);
            }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVqf(@PathVariable Long id) {
        vqfService.deleteVqf(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<Vqf>> search(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<Vqf> vqfs = vqfService.searchVqfs(keyword, page, size);
        return ResponseEntity.ok(vqfs);
    }
    
    @GetMapping("/searchVille")
    public ResponseEntity<Page<Vqf>> searchV(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<Vqf> vqfs = vqfService.searchVilles(keyword, page, size);
        return ResponseEntity.ok(vqfs);
    }
}
