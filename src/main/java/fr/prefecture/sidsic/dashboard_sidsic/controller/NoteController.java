package fr.prefecture.sidsic.dashboard_sidsic.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.prefecture.sidsic.dashboard_sidsic.entity.Note;
import fr.prefecture.sidsic.dashboard_sidsic.repository.NoteRepository;
import fr.prefecture.sidsic.dashboard_sidsic.service.MembreService;

@RestController
@RequestMapping("api/note")
@CrossOrigin(origins ="*")
public class NoteController {

    private final MembreService membreService;
    private final NoteRepository noteRepository;

    public NoteController(MembreService membreService, NoteRepository noteRepository) {
        this.membreService = membreService;
        this.noteRepository = noteRepository;
    }
    @GetMapping("/getbymembre{id}")
    public ResponseEntity<?> getNotesByMembre(@PathVariable Long idmembre){
        try {
            return ResponseEntity.ok(membreService.GetMembre(membreService.getMembreById(idmembre)).getNotes());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping("/update")
    public ResponseEntity<?> updateNotebyMembre(@RequestBody Note note){
        try {
            return ResponseEntity.ok(noteRepository.save(note));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/create")
    public ResponseEntity<?> createNoteByMembre(@RequestBody Note note){
        try {
            Note n = new Note();
            n.setDescription(note.getDescription());
            n.setMembre(note.getMembre());
            return ResponseEntity.ok(noteRepository.save(n));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @DeleteMapping("/delete{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id){
        try {
            Optional<Note> n = noteRepository.findById(id);
            if (n.isPresent()) {
                Note note = n.get();
                noteRepository.delete(note);
                return ResponseEntity.status(HttpStatus.OK).body("Note correctement supprimer !");
            }
            else
                throw new RuntimeException("La note n'existe pas");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
