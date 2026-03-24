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

import fr.prefecture.sidsic.dashboard_sidsic.dto.AchatDTO;
import fr.prefecture.sidsic.dashboard_sidsic.dto.EtatAchatDTO;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Achat;
import fr.prefecture.sidsic.dashboard_sidsic.service.GroupeService;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AchatController {
	private final GroupeService groupeService;

	public AchatController(GroupeService groupeService) {
		this.groupeService = groupeService;
	}

	@GetMapping("/groupes/{idGroupe}/achats")
	public ResponseEntity<?> getAllAchatGroupe(@PathVariable Long idGroupe) {
		try {
			return ResponseEntity.ok(groupeService.getAllAchatsByGroupe(idGroupe)); 
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	@PutMapping("/groupes/{idGroupe}/achats/{idAchat}")
	public ResponseEntity<?> updateAchat(@RequestBody AchatDTO achat, @PathVariable Long idGroupe) {
		try {
			return ResponseEntity.ok(groupeService.updateAchat(achat, idGroupe));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}
	@PatchMapping("/achats/{id}/etat")
	public ResponseEntity<?> updateEtatAchat(@PathVariable Long id, @RequestBody EtatAchatDTO dto){
		try {
			Achat achat = groupeService.getAchatById(id);
			achat.setEtat(dto.getEtat());
			return ResponseEntity.ok(groupeService.updateAchatEtat(achat));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PostMapping("/groupes/{idGroupe}/achats")
	public ResponseEntity<?> createAchat(@RequestBody AchatDTO achat, @PathVariable Long idGroupe) {
		try {
			return ResponseEntity.ok(groupeService.createAchat(achat, idGroupe));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	@DeleteMapping("/groupes/{idGroupe}/achats/{idAchat}")
	public ResponseEntity<?> deleteAchat(@PathVariable Long idAchat, @PathVariable Long idGroupe) {
		try {
			groupeService.deleteAchat(idAchat, idGroupe);
			return ResponseEntity.ok("Achat deleted successfully");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	@GetMapping("/achats/{idAchat}/etat")
	public ResponseEntity<?> getEtatAchat(@PathVariable Long idAchat) {
		try {
			return ResponseEntity.ok(groupeService.getEtatsAchat(idAchat));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	@GetMapping("/achats/etats")
	public ResponseEntity<?> getEtatsAchat() {
		try {
			return ResponseEntity.ok(groupeService.getAllEtatsAchat());
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}
}
