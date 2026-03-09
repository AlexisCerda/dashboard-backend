package fr.prefecture.sidsic.dashboard_sidsic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.prefecture.sidsic.dashboard_sidsic.dto.EtatPretDTO;
import fr.prefecture.sidsic.dashboard_sidsic.dto.PretDTO;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Pret;
import fr.prefecture.sidsic.dashboard_sidsic.service.GroupeService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PretController {
    private final GroupeService groupeService;

    public PretController(GroupeService groupeService) {
        this.groupeService = groupeService;
    }

    @GetMapping("/groupes/{idGroupe}/prets")
    public ResponseEntity<?> getAllPretGroupe(@PathVariable Long idGroupe) {
        try {
            return ResponseEntity.ok(groupeService.getAllPretsByGroupe(idGroupe)); // La méthode service retourne maintenant des DTO
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
        @PatchMapping("/prets/{id}/etat")
        public ResponseEntity<?> updateEtatPret(@PathVariable Long id, @RequestBody EtatPretDTO dto){
            try {
                Pret pret = groupeService.getPretById(id);
                pret.setEtat(dto.getEtat());
                    return ResponseEntity.ok(groupeService.updatePretEtat(pret));
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }

    @PutMapping("/groupes/{idGroupe}/prets/{idPret}")
    public ResponseEntity<?> updatePret(@RequestBody PretDTO pret, @PathVariable Long idGroupe) {
        try {
            return ResponseEntity.ok(groupeService.updatePret(pret, idGroupe));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/groupes/{idGroupe}/prets")
    public ResponseEntity<?> createPret(@RequestBody PretDTO pret, @PathVariable Long idGroupe) {
        try {
            return ResponseEntity.ok(groupeService.createPret(pret, idGroupe));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @DeleteMapping("/groupes/{idGroupe}/prets/{idPret}")
    public ResponseEntity<?> deletePret(@PathVariable Long idPret, @PathVariable Long idGroupe) {
        try {
            groupeService.deletePret(idPret, idGroupe);
            return ResponseEntity.ok("Pret deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/prets/{idPret}/etat")
    public ResponseEntity<?> getEtatPret(@PathVariable Long idPret) {
        try {
            return ResponseEntity.ok(groupeService.getEtatsPret(idPret));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/prets/etats")
    public ResponseEntity<?> getEtatsPret() {
        try {
            return ResponseEntity.ok(groupeService.getAllEtatsPret());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
