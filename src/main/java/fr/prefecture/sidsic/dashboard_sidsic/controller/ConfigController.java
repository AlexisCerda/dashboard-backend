package fr.prefecture.sidsic.dashboard_sidsic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.prefecture.sidsic.dashboard_sidsic.service.ConfigService;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

  private ConfigService configService;
  public ConfigController(ConfigService configService) {
    this.configService = configService;
  }

  @GetMapping
    public ResponseEntity<?> getConfig() {
      return ResponseEntity.status(HttpStatus.CREATED).body(configService.getConfiguration());
    }
}