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
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class GroupeController {
    private final GroupeService groupeService;

    public GroupeController(GroupeService groupeService) {
        this.groupeService = groupeService;
    }

    @GetMapping("/membres/{id}/groupes")
    public ResponseEntity<?> GetAllByMembre(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(groupeService.getAllByMembre(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/membres/{id}/groupes/role/{role}")
    public ResponseEntity<?> getGroupesByRole(@PathVariable Long id, @PathVariable int role) {
        try {
            return ResponseEntity.ok(groupeService.getGroupesByRole(id, role));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/groupes/{idGroupe}/membres")
    public ResponseEntity<?> GetAllMembreGroupe(@PathVariable Long idGroupe) {
        try {
            return ResponseEntity.ok(groupeService.getAllMembre(idGroupe));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/groupes/{idGroupe}/membres/role/{role}")
    public ResponseEntity<?> getMembresByRole(@PathVariable Long idGroupe, @PathVariable int role) {
        try {
            return ResponseEntity.ok(groupeService.getMembresByRole(idGroupe, role));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/groupes/{idGroupe}/membres/{idMembre}/role")
    public ResponseEntity<?> getUserRole(@PathVariable Long idMembre, @PathVariable Long idGroupe) {
        try {
            return ResponseEntity.ok(groupeService.getUserRole(idMembre, idGroupe));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @DeleteMapping("/groupes/{idGroupe}/membres/{idMembre}")
    public ResponseEntity<?> DeleteMembre(@PathVariable Long idMembre, @PathVariable Long idGroupe) {
        try {
            groupeService.deleteMembre(idMembre, idGroupe);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/membres/{idMembre}/groupes")
    public ResponseEntity<?> CreateGroupe(@PathVariable Long idMembre, @RequestBody GroupeDTO groupeDTO) {
        try {
            return ResponseEntity.ok(groupeService.createGroupe(idMembre, groupeDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/groupes/{idGroupe}/membres/{idMembre}/added-by/{idMembreActuel}")
    public ResponseEntity<?> addMembreToGroupe(@PathVariable Long idMembre, @PathVariable Long idGroupe, @PathVariable Long idMembreActuel   ) {
        try {
            return ResponseEntity.ok(groupeService.addMembreToGroupe(idMembre, idGroupe, idMembreActuel));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PatchMapping("/groupes/{idGroupe}/membres/{idMembre}/role/{role}/by/{idMembreActuel}")
    public ResponseEntity<?> setMembreRole(@PathVariable Long idMembreActuel, @PathVariable Long idGroupe,
            @PathVariable Long idMembre, @PathVariable int role) {
        try {
            return ResponseEntity.ok(groupeService.setMembreRole(idMembre, idGroupe, role, idMembreActuel));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PatchMapping("/groupes/{idGroupe}/membres/{idMembre}/role/{role}/urgent")
    public ResponseEntity<?> setMembreRoleUrgent(@PathVariable Long idGroupe,
            @PathVariable Long idMembre, @PathVariable int role) {
        try {
            return ResponseEntity.ok(groupeService.setMembreRole(idMembre, idGroupe, role, null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @DeleteMapping("/groupes/{idGroupe}")
    public ResponseEntity<?> deleteGroupe(@PathVariable Long idGroupe) {
        try {
            groupeService.deleteGroupe(idGroupe);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/groupes")
    public ResponseEntity<?> getALLGroupes() {
        try {
            return ResponseEntity.ok(groupeService.getAllGroupes());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
