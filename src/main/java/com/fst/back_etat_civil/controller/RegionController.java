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

import com.fst.back_etat_civil.dto.RegionDto;
import com.fst.back_etat_civil.model.Region;
import com.fst.back_etat_civil.model.Vqf;
import com.fst.back_etat_civil.repository.RegionRepository;
import com.fst.back_etat_civil.service.RegionService;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/region")
public class RegionController {
    @Autowired
    private RegionService regionService;
    @Autowired
    private RegionRepository regionRepository;


    

	@GetMapping
    public ResponseEntity<List<RegionDto>> getAllRegions() {
        List<RegionDto> regions = regionService.getAllRegions();
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDto> getRegionById(@PathVariable Long id) {
        RegionDto region = regionService.getRegionById(id);
        return new ResponseEntity<>(region, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RegionDto> createRegion(@RequestBody RegionDto regionDto) {
    	if (regionRepository.existsByCode(regionDto.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce code de region existe déjà");
        }
    	if (regionRepository.existsByNom(regionDto.getNom())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce nom de region existe déjà");
        }else {
        RegionDto createdRegion = regionService.createRegion(regionDto);
        return new ResponseEntity<>(createdRegion, HttpStatus.CREATED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionDto> updateRegion(@PathVariable Long id, @RequestBody RegionDto regionDto) {
    	if (regionRepository.existsByCode(regionDto.getCode()) 
    		&& !regionRepository.findById(id).get().getCode().equals(regionDto.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce code de region existe déjà");
        }
    	if (regionRepository.existsByNom(regionDto.getNom())
    			&& !regionRepository.findById(id).get().getNom().equals(regionDto.getNom())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce nom de region existe déjà");
        }else {
        RegionDto updatedRegion = regionService.updateRegion(id, regionDto);
        return new ResponseEntity<>(updatedRegion, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<Region>> search(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<Region> regions = regionService.searchRegions(keyword, page, size);
        return ResponseEntity.ok(regions);
    }
}
