package fr.prefecture.sidsic.dashboard_sidsic.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import fr.prefecture.sidsic.dashboard_sidsic.dto.MembreUpdateDTO;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Membre;
import fr.prefecture.sidsic.dashboard_sidsic.service.GroupeService;
import fr.prefecture.sidsic.dashboard_sidsic.service.MembreService;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/membres")
@CrossOrigin(origins ="*")
public class MembreController {
    private final MembreService membreService;
    private final GroupeService groupeService;

    public MembreController(MembreService membreService, GroupeService groupeService) {
        this.membreService = membreService;
        this.groupeService = groupeService;
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMembreById(@RequestBody MembreUpdateDTO membre ){
        try {
            Optional<Membre> m = membreService.getMembreById(membre.getId());
            if (m.isPresent()) {
                if (!membre.getEmail().contains("@")) {
                    throw new RuntimeException("Veuillez entrer une adresse mail valide");
                }
                Membre membreExist = m.get();
                membreExist.setPrenom(membre.getPrenom());
                membreExist.setNom(membre.getNom());
                membreExist.setEmail(membre.getEmail());
                membreExist.setPassword(membreService.Encrypted(membre.getPwd()));
                membreService.SaveBD(membreExist);
                return ResponseEntity.ok(membreService.GetMembreDTO(membreExist));
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

    @PostMapping("/{mdp}")
    public ResponseEntity<?> createMembre(@RequestBody MembreDTO membre, @PathVariable String mdp) {
        try {
            return ResponseEntity.ok((membreService.creerUnNouveauMembre(membre,mdp)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());

        }
    }

    @PatchMapping("/{idMembre}/groupe-actuel/{idGroupe}")
    public ResponseEntity<?> updateCurrentGroupe(@PathVariable Long idMembre, @PathVariable Long idGroupe) {
        try {
            return ResponseEntity.ok(groupeService.updateCurrentGroupe(idMembre, idGroupe));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
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
}
