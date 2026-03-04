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

import fr.prefecture.sidsic.dashboard_sidsic.dto.AchatDTO;
import fr.prefecture.sidsic.dashboard_sidsic.service.GroupeService;


@RestController
@RequestMapping("/api/achat")
@CrossOrigin(origins = "*")
public class AchatController {
	private final GroupeService groupeService;

	public AchatController(GroupeService groupeService) {
		this.groupeService = groupeService;
	}

	@GetMapping("/getall/{idgroupe}")
	public ResponseEntity<?> getAllAchatGroupe(@PathVariable Long idgroupe) {
		try {
			return ResponseEntity.ok(groupeService.getAllAchatsByGroupe(idgroupe)); // La méthode service retourne maintenant des DTO
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	@PutMapping("/update/{idgroupe}")
	public ResponseEntity<?> updateAchat(@RequestBody AchatDTO achat, @PathVariable Long idgroupe) {
		try {
			return ResponseEntity.ok(groupeService.updateAchat(achat, idgroupe));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	@PostMapping("/create/{idgroupe}")
	public ResponseEntity<?> createAchat(@RequestBody AchatDTO achat, @PathVariable Long idgroupe) {
		try {
			return ResponseEntity.ok(groupeService.createAchat(achat, idgroupe));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	@DeleteMapping("/delete/{idachat}/{idgroupe}")
	public ResponseEntity<?> deleteAchat(@PathVariable Long idachat, @PathVariable Long idgroupe) {
		try {
			groupeService.deleteAchat(idachat, idgroupe);
			return ResponseEntity.ok("Achat deleted successfully");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	@GetMapping("/getetat/{idachat}")
	public ResponseEntity<?> getEtatAchat(@PathVariable Long idachat) {
		try {
			return ResponseEntity.ok(groupeService.getEtatsAchat(idachat));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	@GetMapping("/getetats")
	public ResponseEntity<?> getEtatsAchat() {
		try {
			return ResponseEntity.ok(groupeService.getAllEtatsAchat());
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}
}
