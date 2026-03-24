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

import fr.prefecture.sidsic.dashboard_sidsic.dto.MouvementDTO;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Mouvement;
import fr.prefecture.sidsic.dashboard_sidsic.dto.EtatMouvementDTO;
import fr.prefecture.sidsic.dashboard_sidsic.service.GroupeService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MouvementController {
    private final GroupeService groupeService;

    public MouvementController(GroupeService groupeService) {
        this.groupeService = groupeService;
    }

    @GetMapping("/groupes/{idGroupe}/mouvements")
    public ResponseEntity<?> GetAllMouvementGroupe(@PathVariable Long idGroupe) {
        try {
            return ResponseEntity.ok(groupeService.getAllMouvementsByGroupe(idGroupe)); 
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
        @PatchMapping("/mouvements/{id}/etat")
        public ResponseEntity<?> updateEtatMouvement(@PathVariable Long id, @RequestBody EtatMouvementDTO dto){
            try {
                Mouvement mouvement = groupeService.getMouvementById(id);
                mouvement.setEtat(dto.getEtat());
                return ResponseEntity.ok(groupeService.updateMouvementEtat(mouvement));
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }

    @PutMapping("/groupes/{idGroupe}/mouvements/{idMouvement}")
    public ResponseEntity<?> UpdateMouvement(@RequestBody MouvementDTO mouvement, @PathVariable Long idGroupe) {
        try {
            
            return ResponseEntity.ok(groupeService.updateMouvement(mouvement, idGroupe));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/groupes/{idGroupe}/mouvements")
    public ResponseEntity<?> CreateMouvement(@RequestBody MouvementDTO mouvement, @PathVariable Long idGroupe) {
        try {
            return ResponseEntity.ok(groupeService.createMouvement(mouvement, idGroupe));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @DeleteMapping("/groupes/{idGroupe}/mouvements/{idMouvement}")
    public ResponseEntity<?> DeleteMouvement(@PathVariable Long idMouvement, @PathVariable Long idGroupe) {
        try {
            groupeService.deleteMouvement(idMouvement, idGroupe);
            return ResponseEntity.ok("Mouvement deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/mouvements/{idMouvement}/etat")
    public ResponseEntity<?> GetEtatMouvement(@PathVariable Long idMouvement) {
        try {
            return ResponseEntity.ok(groupeService.getEtatsMouvement(idMouvement));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/mouvements/etats")
    public ResponseEntity<?> GetEtatsMouvement() {
        try {
            return ResponseEntity.ok(groupeService.getAllEtatsMouvement());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
