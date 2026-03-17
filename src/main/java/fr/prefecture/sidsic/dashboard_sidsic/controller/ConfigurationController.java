package fr.prefecture.sidsic.dashboard_sidsic.controller;

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
import org.springframework.web.bind.annotation.RestController;

import fr.prefecture.sidsic.dashboard_sidsic.dto.ConfigurationDTO;
import fr.prefecture.sidsic.dashboard_sidsic.service.ConfigurationService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ConfigurationController {
    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @GetMapping("/groupes/{idGroupe}/membres/{idMembre}/configurations")
    public ResponseEntity<?> getAllConfigurationsByGroupeAndMembre(@PathVariable Long idGroupe, @PathVariable Long idMembre) {
        try {
            return ResponseEntity.ok(configurationService.getAllConfigurationsByGroupeAndMembre(idGroupe, idMembre));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/configurations/{idConfiguration}")
    public ResponseEntity<?> getConfigurationById(@PathVariable Long idConfiguration) {
        return ResponseEntity.ok(configurationService.getConfigurationById(idConfiguration));
    }

    @PostMapping("/groupes/{idGroupe}/membres/{idMembre}/configurations")
    public ResponseEntity<?> createConfiguration(@PathVariable Long idGroupe, @PathVariable Long idMembre, @RequestBody String nom) {
        try {
            return ResponseEntity.ok(configurationService.createConfiguration(idMembre, idGroupe, nom));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/configurations")
    public ResponseEntity<?> updateConfiguration( @RequestBody ConfigurationDTO configurationDTO) {
        try {
            return ResponseEntity.ok(configurationService.updateConfiguration(configurationDTO.getId(), configurationDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/configurations/{idConfiguration}")
    public ResponseEntity<?> deleteConfiguration(@PathVariable Long idConfiguration) {
        try {
            configurationService.deleteConfiguration(idConfiguration);
            return ResponseEntity.ok("Configuration deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
