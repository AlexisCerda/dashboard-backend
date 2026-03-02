package fr.prefecture.sidsic.dashboard_sidsic.controller;

import fr.prefecture.sidsic.dashboard_sidsic.dto.LoginRequest;
import fr.prefecture.sidsic.dashboard_sidsic.dto.MembreDTO;
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
    
    @PostMapping("/login")
    public ResponseEntity<?> seConnecter(@RequestBody LoginRequest requeteConnexion) {
        try {
            Membre membreConnecte = membreService.verifierConnexion(requeteConnexion.getEmail(),requeteConnexion.getMotDePasse());
            
            MembreDTO reponseSecurisee = new MembreDTO();
            reponseSecurisee.setId(membreConnecte.getId());
            reponseSecurisee.setNom(membreConnecte.getNom());
            reponseSecurisee.setPrenom(membreConnecte.getPrenom());
            reponseSecurisee.setEmail(membreConnecte.getEmail());
            return ResponseEntity.ok(reponseSecurisee);

        } catch (RuntimeException erreur) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erreur.getMessage());
        }
    }
}