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

import fr.prefecture.sidsic.dashboard_sidsic.dto.AddMembreTache;
import fr.prefecture.sidsic.dashboard_sidsic.dto.TacheDTO;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Tache;
import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatTache;
import fr.prefecture.sidsic.dashboard_sidsic.service.GroupeService;
import fr.prefecture.sidsic.dashboard_sidsic.service.MembreService;

@RestController
@RequestMapping("api/tache")
@CrossOrigin(origins ="*")
public class TacheController {

    private final MembreService membreService;
    private final GroupeService groupeService;

    public TacheController(MembreService membreService, GroupeService groupeService) {
        this.membreService = membreService;
        this.groupeService = groupeService;
    }

    @GetMapping("/getMembreid{id}")
    public ResponseEntity<?> getTacheByMembre(@PathVariable Long idmembre){
        try {
            return ResponseEntity.ok(membreService.GetMembre(membreService.getMembreById(idmembre)).getTaches());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/getGroupeid{id}")
    public ResponseEntity<?> getTacheByGroupe(@PathVariable Long idgroupe){
        try {
            return ResponseEntity.ok(groupeService.getGroupeById(idgroupe).getTaches());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/addMembre")
    public ResponseEntity<?> addMembreToTache(@RequestBody AddMembreTache addMembreTache){
        try {
            return ResponseEntity.ok(membreService.addMembreToTache(addMembreTache.getIdMembre(), addMembreTache.getIdTache()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteMembre")
    public ResponseEntity<?> deleteMembreFromTache(@RequestBody AddMembreTache addMembreTache){
        try {
            return ResponseEntity.ok(membreService.deleteMembreFromTache(addMembreTache.getIdMembre(), addMembreTache.getIdTache()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateTache(@RequestBody TacheDTO tache){
        try {
            return ResponseEntity.ok(membreService.updateTacheDTO(tache));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTache(@PathVariable Long id){
        try {
            membreService.deleteTache(id);
            return ResponseEntity.ok("Tache deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTache(@RequestBody TacheDTO tacheDTO){
        try {
            return ResponseEntity.ok(membreService.addTache(tacheDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/updateEtat/{id}")
    public ResponseEntity<?> updateEtatTache(@PathVariable Long id, @RequestBody EtatTache etat){
        try {
            Tache tache = membreService.getTacheById(id);
            tache.setEtat(etat);
            membreService.updateTache(tache);
            return ResponseEntity.ok(tache);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/getEtats")
    public ResponseEntity<?> getEtatTache(){
        return ResponseEntity.ok(EtatTache.values());
    }
}
