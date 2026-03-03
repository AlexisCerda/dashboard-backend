package fr.prefecture.sidsic.dashboard_sidsic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/getallmembre")
    public ResponseEntity<?> GetAllMembreGroupe() {
        try {
            return ResponseEntity.ok(groupeService.getAllMembre());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/getallmembreadmin")
    public ResponseEntity<?> GetAllMembreAdmin() {
        try {
            return ResponseEntity.ok(groupeService.getAllAdmin());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
