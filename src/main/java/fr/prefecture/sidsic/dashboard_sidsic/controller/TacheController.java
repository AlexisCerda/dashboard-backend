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

import fr.prefecture.sidsic.dashboard_sidsic.dto.AddMembreTache;
import fr.prefecture.sidsic.dashboard_sidsic.dto.TacheDTO;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Groupe;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Tache;
import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatTache;
import fr.prefecture.sidsic.dashboard_sidsic.service.GroupeService;
import fr.prefecture.sidsic.dashboard_sidsic.service.MembreService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins ="*")
public class TacheController {
    public static class EtatTacheDTO {
        public EtatTache etat;
    }

    private final MembreService membreService;
    private final GroupeService groupeService;

    public TacheController(MembreService membreService, GroupeService groupeService) {
        this.membreService = membreService;
        this.groupeService = groupeService;
    }

    @GetMapping("/membres/{id}/taches")
    public ResponseEntity<?> getTacheByMembre(@PathVariable("id") Long idMembre){
        try {
            return ResponseEntity.ok(membreService.getTacheDTO(membreService.GetMembre(membreService.getMembreById(idMembre))));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/groupes/{id}/taches")
    public ResponseEntity<?> getTacheByGroupe(@PathVariable("id") Long idGroupe){
        try {
            Groupe groupe = groupeService.getGroupeById(idGroupe);
            return ResponseEntity.ok(groupe.getTaches());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/taches/{idTache}/membres")
    public ResponseEntity<?> addMembreToTache(@PathVariable Long idTache, @RequestBody AddMembreTache addMembreTache){
        try {
            return ResponseEntity.ok(membreService.addMembreToTache(addMembreTache.getIdMembre(), idTache));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/taches/{idTache}/membres/{idMembre}")
    public ResponseEntity<?> deleteMembreFromTache(@PathVariable Long idTache, @PathVariable Long idMembre){
        try {
            return ResponseEntity.ok(membreService.deleteMembreFromTache(idMembre, idTache));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/taches/{id}")
    public ResponseEntity<?> updateTache(@PathVariable Long id, @RequestBody TacheDTO tache){
        try {
            return ResponseEntity.ok(membreService.updateTacheDTO(tache));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/taches/{id}")
    public ResponseEntity<?> deleteTache(@PathVariable Long id){
        try {
            membreService.deleteTache(id);
            return ResponseEntity.ok("Tache deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/groupes/{idGroupe}/taches")
    public ResponseEntity<?> addTache(@RequestBody TacheDTO tacheDTO,@PathVariable Long idGroupe){
        try {
            return ResponseEntity.ok(membreService.addTache(tacheDTO, groupeService.getGroupeById(idGroupe)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/taches/{id}/etat")
    public ResponseEntity<?> updateEtatTache(@PathVariable Long id, @RequestBody EtatTacheDTO dto){
        try {
            Tache tache = membreService.getTacheById(id);
            tache.setEtat(dto.etat);
            membreService.updateTache(tache);
            TacheDTO tacheDTO = membreService.convertTacheToDTO(tache);
            return ResponseEntity.ok(tacheDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/taches/etats")
    public ResponseEntity<?> getEtatTache(){
        return ResponseEntity.ok(EtatTache.values());
    }
}
