package fr.prefecture.sidsic.dashboard_sidsic.controller;

import fr.prefecture.sidsic.dashboard_sidsic.dto.LoginRequest;
import fr.prefecture.sidsic.dashboard_sidsic.dto.MembreCreationRequestDTO;
import fr.prefecture.sidsic.dashboard_sidsic.dto.MembreDTO;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Membre;
import fr.prefecture.sidsic.dashboard_sidsic.repository.MembreRepository;
import fr.prefecture.sidsic.dashboard_sidsic.security.JwtService;
import fr.prefecture.sidsic.dashboard_sidsic.service.MembreService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final MembreRepository membreRepository;
    private final MembreService membreService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, MembreRepository membreRepository,
            MembreService membreService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.membreRepository = membreRepository;
        this.membreService = membreService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> inscrire(@RequestBody MembreCreationRequestDTO request) {
        try {
            MembreDTO membre = new MembreDTO();
            membre.setNom(request.getNom());
            membre.setPrenom(request.getPrenom());
            membre.setEmail(request.getEmail());

            MembreDTO membreCree = membreService.creerUnNouveauMembre(membre, request.getMotDePasse());
            return ResponseEntity.status(HttpStatus.CREATED).body(membreCree);
        } catch (RuntimeException erreur) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erreur.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> seConnecter(@RequestBody LoginRequest requeteConnexion) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requeteConnexion.getEmail(),
                            requeteConnexion.getMotDePasse()
                    )
            );
            
            Membre membreConnecte = membreRepository.findByEmail(requeteConnexion.getEmail())
                    .orElseThrow(() -> new RuntimeException("Membre introuvable"));
            membreConnecte.setLastConnection(LocalDate.now());
            membreRepository.save(membreConnecte);
            String jwtToken = jwtService.generateToken(membreConnecte);
            
            MembreDTO infosUtilisateur = new MembreDTO();
            infosUtilisateur.setId(membreConnecte.getId());
            infosUtilisateur.setNom(membreConnecte.getNom());
            infosUtilisateur.setPrenom(membreConnecte.getPrenom());
            infosUtilisateur.setEmail(membreConnecte.getEmail());

            Map<String, Object> reponseBody = new HashMap<>();
            reponseBody.put("token", jwtToken);
            reponseBody.put("utilisateur", infosUtilisateur);

            return ResponseEntity.ok(reponseBody);

        } catch (Exception erreur) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou mot de passe incorrect");
        }
    }
}