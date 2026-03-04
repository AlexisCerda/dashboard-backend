package fr.prefecture.sidsic.dashboard_sidsic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.prefecture.sidsic.dashboard_sidsic.dto.PretDTO;
import fr.prefecture.sidsic.dashboard_sidsic.service.GroupeService;

@RestController
@RequestMapping("/api/pret")
@CrossOrigin(origins = "*")
public class PretController {
    private final GroupeService groupeService;

    public PretController(GroupeService groupeService) {
        this.groupeService = groupeService;
    }

    @GetMapping("/getall/{idgroupe}")
    public ResponseEntity<?> getAllPretGroupe(@PathVariable Long idgroupe) {
        try {
            return ResponseEntity.ok(groupeService.getAllPretsByGroupe(idgroupe));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PutMapping("/update/{idgroupe}")
    public ResponseEntity<?> updatePret(@RequestBody PretDTO pret, @PathVariable Long idgroupe) {
        try {
            return ResponseEntity.ok(groupeService.updatePret(pret, idgroupe));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/create/{idgroupe}")
    public ResponseEntity<?> createPret(@RequestBody PretDTO pret, @PathVariable Long idgroupe) {
        try {
            return ResponseEntity.ok(groupeService.createPret(pret, idgroupe));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{idpret}/{idgroupe}")
    public ResponseEntity<?> deletePret(@PathVariable Long idpret, @PathVariable Long idgroupe) {
        try {
            groupeService.deletePret(idpret, idgroupe);
            return ResponseEntity.ok("Pret deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/getetat/{idpret}")
    public ResponseEntity<?> getEtatPret(@PathVariable Long idpret) {
        try {
            return ResponseEntity.ok(groupeService.getEtatsPret(idpret));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/getetats")
    public ResponseEntity<?> getEtatsPret() {
        try {
            return ResponseEntity.ok(groupeService.getAllEtatsPret());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
