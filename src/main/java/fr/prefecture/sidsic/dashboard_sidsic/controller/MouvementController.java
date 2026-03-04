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
@RequestMapping("/api/mouvement")
@CrossOrigin(origins = "*")
public class MouvementController {
    private final GroupeService groupeService;

    public MouvementController(GroupeService groupeService) {
        this.groupeService = groupeService;
    }

    @GetMapping("/getall/{idgroupe}")
    public ResponseEntity<?> GetAllMouvementGroupe(@PathVariable Long idgroupe) {
        try {
            return ResponseEntity.ok(groupeService.getAllMouvementsByGroupe(idgroupe)); // La méthode service retourne maintenant des DTO
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
        @PatchMapping("/updateEtat/{id}")
        public ResponseEntity<?> updateEtatMouvement(@PathVariable Long id, @RequestBody EtatMouvementDTO dto){
            try {
                Mouvement mouvement = groupeService.getMouvementById(id);
                mouvement.setEtat(dto.getEtat());
                return ResponseEntity.ok(groupeService.updateMouvementEtat(mouvement));
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }

    @PutMapping("/update/{idgroupe}")
    public ResponseEntity<?> UpdateMouvement(@RequestBody MouvementDTO mouvement, @PathVariable Long idgroupe) {
        try {
            
            return ResponseEntity.ok(groupeService.updateMouvement(mouvement, idgroupe));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/create/{idgroupe}")
    public ResponseEntity<?> CreateMouvement(@RequestBody MouvementDTO mouvement, @PathVariable Long idgroupe) {
        try {
            return ResponseEntity.ok(groupeService.createMouvement(mouvement, idgroupe));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{idmouvement}/{idgroupe}")
    public ResponseEntity<?> DeleteMouvement(@PathVariable Long idmouvement, @PathVariable Long idgroupe) {
        try {
            groupeService.deleteMouvement(idmouvement, idgroupe);
            return ResponseEntity.ok("Mouvement deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/getetat/{idmouvement}")
    public ResponseEntity<?> GetEtatMouvement(@PathVariable Long idmouvement) {
        try {
            return ResponseEntity.ok(groupeService.getEtatsMouvement(idmouvement));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/getetats")
    public ResponseEntity<?> GetEtatsMouvement() {
        try {
            return ResponseEntity.ok(groupeService.getAllEtatsMouvement());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
