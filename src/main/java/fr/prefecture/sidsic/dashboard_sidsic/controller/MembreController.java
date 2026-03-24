package fr.prefecture.sidsic.dashboard_sidsic.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.prefecture.sidsic.dashboard_sidsic.dto.MembreDTO;
import fr.prefecture.sidsic.dashboard_sidsic.dto.MembreCreationRequestDTO;
import fr.prefecture.sidsic.dashboard_sidsic.dto.MembreUpdateDTO;
import fr.prefecture.sidsic.dashboard_sidsic.dto.UpdatePasswordRequestDTO;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Membre;
import fr.prefecture.sidsic.dashboard_sidsic.security.JwtService;
import fr.prefecture.sidsic.dashboard_sidsic.service.GroupeService;
import fr.prefecture.sidsic.dashboard_sidsic.service.MembreService;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/membres")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class MembreController {
    private final MembreService membreService;
    private final GroupeService groupeService;
    private final JwtService jwtService;

    public MembreController(MembreService membreService, GroupeService groupeService, JwtService jwtService) {
        this.membreService = membreService;
        this.groupeService = groupeService;
        this.jwtService = jwtService;
    }

    
    @GetMapping
    public ResponseEntity<?> GetAll() {
        try {
            return ResponseEntity.ok(membreService.recupererToutLesMembres());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> GetById(@PathVariable Long id ){
        try {
            return ResponseEntity.ok(membreService.GetMembreDTO(membreService.getMembreById(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/")
    public ResponseEntity<?> updateMembreById(@RequestBody MembreUpdateDTO membre ){
        try {
            if (membre.getId() == null) {
                MembreDTO nouveauMembre = new MembreDTO();
                nouveauMembre.setNom(membre.getNom());
                nouveauMembre.setPrenom(membre.getPrenom());
                nouveauMembre.setEmail(membre.getEmail());
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(membreService.creerUnNouveauMembre(nouveauMembre, membre.getPwd()));
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentification requise");
            }

            Optional<Membre> m = membreService.getMembreById(membre.getId());
            if (m.isPresent()) {
                if (!membre.getEmail().contains("@")) {
                    throw new RuntimeException("Veuillez entrer une adresse mail valide");
                }
                if (membreService.getMembreByEmail(membre.getEmail()).isPresent() && !(membre.getEmail().equals(m.get().getEmail()))) {
                    throw new RuntimeException("L'Email est deja pris !");
                }
                Membre membreExist = m.get();
                boolean emailChange = !membre.getEmail().equals(membreExist.getEmail());
                membreExist.setPrenom(membre.getPrenom());
                membreExist.setNom(membre.getNom());
                membreExist.setEmail(membre.getEmail());
                membreExist.setPassword(membreExist.getPassword());
                membreService.SaveBD(membreExist);

                MembreDTO membreDTO = membreService.GetMembreDTO(membreExist);
                if (emailChange) {
                    String nouveauToken = jwtService.generateToken(membreExist);
                    Map<String, Object> reponse = new HashMap<>();
                    reponse.put("token", nouveauToken);
                    reponse.put("utilisateur", membreDTO);
                    return ResponseEntity.ok(reponse);
                }
                return ResponseEntity.ok(membreDTO);
            }else{
                throw new RuntimeException("Le membre n'existe pas");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMemberById(@PathVariable Long id){
        try {
            Membre m = membreService.GetMembre(membreService.getMembreById(id));
            membreService.DelMembre(m);
            return ResponseEntity.status(HttpStatus.OK).body("Membre supprimer");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createMembre(@RequestBody MembreCreationRequestDTO request) {
        try {
            MembreDTO membre = new MembreDTO();
            membre.setNom(request.getNom());
            membre.setPrenom(request.getPrenom());
            membre.setEmail(request.getEmail());
            return ResponseEntity.ok((membreService.creerUnNouveauMembre(membre, request.getMotDePasse())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());

        }
    }

    @PatchMapping("/{idMembre}/groupe-actuel/{idGroupe}")
    public ResponseEntity<?> updateCurrentGroupe(@PathVariable("idMembre") Long idMembre, @PathVariable("idGroupe") Long idGroupe) {
        try {
            return ResponseEntity.ok(groupeService.updateCurrentGroupe(idMembre, idGroupe));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{idMembre}/groupe-actuel")
    public ResponseEntity<?> getCurrentGroupe(@PathVariable Long idMembre) {
        try {
            return ResponseEntity.ok(groupeService.getCurrentGroupe(idMembre));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PatchMapping("/{idMembre}/pwd")
    public ResponseEntity<?> updatePwdbyIdMembre(@PathVariable Long idMembre, @RequestBody UpdatePasswordRequestDTO request ) {
        try {
            membreService.updatePwdByIdMembre(idMembre, request.getMotDePasse());
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @GetMapping("/{id}/last-co")
    public ResponseEntity<?> GetLastConnexionById(@PathVariable Long id ){
        try {
            return ResponseEntity.ok(membreService.GetLastCo(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
