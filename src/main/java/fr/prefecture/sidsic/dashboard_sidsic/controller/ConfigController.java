package fr.prefecture.sidsic.dashboard_sidsic.controller;

import fr.prefecture.sidsic.dashboard_sidsic.dto.AppConfig; 
import fr.prefecture.sidsic.dashboard_sidsic.service.ConfigService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

  private final ConfigService configService;

  public ConfigController(ConfigService configService) {
    this.configService = configService;
  }

  @GetMapping
  public ResponseEntity<?> getConfig() {
    return ResponseEntity.ok(configService.getConfiguration());
  }

  @PutMapping
  public ResponseEntity<?> updateConfig(@RequestBody AppConfig nouvelleConfig) {
    try {
        configService.updateConfiguration(nouvelleConfig);
        
        return ResponseEntity.ok("Configuration mise à jour avec succès !");
        
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Erreur lors de la mise à jour : " + e.getMessage());
    }
  }
}