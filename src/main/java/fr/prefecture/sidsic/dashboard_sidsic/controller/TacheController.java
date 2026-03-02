import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.prefecture.sidsic.dashboard_sidsic.service.MembreService;

@RestController
@RequestMapping("api/tache")
@CrossOrigin(origins ="*")
public class TacheController {

    private final MembreService membreService;

    public TacheController(MembreService membreService) {
        this.membreService = membreService;
    }
    @GetMapping("/getid{id}")
    public ResponseEntity<?> getTacheByMembre(@PathVariable Long idmembre){
        try {
            return ResponseEntity.ok(membreService.GetMembre(membreService.getMembreById(idmembre)).getTaches());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
