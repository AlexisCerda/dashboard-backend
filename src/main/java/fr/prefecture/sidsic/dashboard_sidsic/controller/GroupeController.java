package fr.prefecture.sidsic.dashboard_sidsic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.prefecture.sidsic.dashboard_sidsic.dto.GroupeDTO;
import fr.prefecture.sidsic.dashboard_sidsic.service.GroupeService;

@RestController
@RequestMapping("/api/groupe")
@CrossOrigin(origins = "*")
public class GroupeController {
    private final GroupeService groupeService;

    public GroupeController(GroupeService groupeService) {
        this.groupeService = groupeService;
    }

    @GetMapping("/getallbymembre/{id}")
    public ResponseEntity<?> GetAllByMembre(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(groupeService.getAllByMembre(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/getallbymembreadmin/{id}")
    public ResponseEntity<?> GetAllByMembreAdmin(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(groupeService.getAllByMembreAdmin(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/getallmembre/{idgroupe}")
    public ResponseEntity<?> GetAllMembreGroupe(@PathVariable Long idgroupe) {
        try {
            return ResponseEntity.ok(groupeService.getAllMembre(idgroupe));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/getallmembreadmin/{idgroupe}")
    public ResponseEntity<?> GetAllMembreAdmin(@PathVariable Long idgroupe) {
        try {
            return ResponseEntity.ok(groupeService.getAllAdmin(idgroupe));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @DeleteMapping("/deletemembre/{idmembre}/{idgroupe}")
    public ResponseEntity<?> DeleteMembre(@PathVariable Long idmembre, @PathVariable Long idgroupe) {
        try {
            groupeService.deleteMembre(idmembre, idgroupe);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/create/{idmembre}")
    public ResponseEntity<?> CreateGroupe(@PathVariable Long idmembre, @RequestBody GroupeDTO groupeDTO) {
        try {
            return ResponseEntity.ok(groupeService.createGroupe(idmembre, groupeDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/join/{idMembreActuel}/{idmembre}/{idgroupe}")
    public ResponseEntity<?> addMembreToGroupe(@PathVariable Long idmembre, @PathVariable Long idgroupe, @PathVariable Long idMembreActuel   ) {
        try {
            return ResponseEntity.ok(groupeService.addMembreToGroupe(idmembre, idgroupe, idMembreActuel));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PatchMapping("/updatemembretoadmin/{idmembreActuel}/{idgroupe}/{idmembre}")
    public ResponseEntity<?> updateMembreToAdmin(@PathVariable Long idmembreActuel, @PathVariable Long idgroupe, @PathVariable Long idmembre) {
        try {
            return ResponseEntity.ok(groupeService.updateMembreToAdmin(idmembre, idgroupe, true, idmembreActuel));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @PatchMapping("/updateadmintomembre/{idmembreActuel}/{idgroupe}/{idmembre}")
    public ResponseEntity<?> updateAdminToMembre(@PathVariable Long idmembreActuel, @PathVariable Long idgroupe, @PathVariable Long idmembre) {
        try {
            return ResponseEntity.ok(groupeService.updateMembreToAdmin(idmembre, idgroupe, false, idmembreActuel));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{idgroupe}")
    public ResponseEntity<?> deleteGroupe(@PathVariable Long idgroupe) {
        try {
            groupeService.deleteGroupe(idgroupe);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
