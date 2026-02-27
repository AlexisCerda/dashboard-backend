package fr.prefecture.sidsic.dashboard_sidsic.controller;

import fr.prefecture.sidsic.dashboard_sidsic.dto.LoginRequest;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Membre;
import fr.prefecture.sidsic.dashboard_sidsic.service.MembreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final MembreService membreService;

    public AuthController(MembreService membreService) {
        this.membreService = membreService;
    }
    // -----------------------------------------------------
    // L'URL exacte sera : POST http://localhost:8080/api/auth/login
    // -----------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<?> seConnecter(@RequestBody LoginRequest requeteConnexion) {
        try {
            // 1. On demande au Service de faire le sale boulot (vérifier en base de données)
            Membre membreConnecte = membreService.verifierConnexion(requeteConnexion.getEmail(),requeteConnexion.getMotDePasse());
            
            // On renvoie un code HTTP 200 (OK) avec les infos de l'agent.
            return ResponseEntity.ok(membreConnecte);

        } catch (RuntimeException erreur) {
            // On renvoie un code HTTP 401 (Unauthorized) avec le message d'erreur.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erreur.getMessage());
        }
    }
}