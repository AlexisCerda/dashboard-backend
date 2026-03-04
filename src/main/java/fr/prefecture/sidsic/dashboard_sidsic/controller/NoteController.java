package fr.prefecture.sidsic.dashboard_sidsic.controller;

import java.util.ArrayList;
import java.util.List;
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

import fr.prefecture.sidsic.dashboard_sidsic.dto.NoteDTO;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Membre;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Note;
import fr.prefecture.sidsic.dashboard_sidsic.repository.NoteRepository;
import fr.prefecture.sidsic.dashboard_sidsic.service.MembreService;

@RestController
@RequestMapping("api/note")
@CrossOrigin(origins ="*")
public class NoteController {
    private NoteDTO convertToDTO(Note note) {
        NoteDTO dto = new NoteDTO();
        dto.setId(note.getId());
        dto.setDescription(note.getDescription());
        return dto;
    }

    private List<NoteDTO> convertToDTOList(List<Note> notes) {
        List<NoteDTO> dtos = new ArrayList<>();
        for (Note note : notes) {
            dtos.add(convertToDTO(note));
        }
        return dtos;
    }

    private final MembreService membreService;
    private final NoteRepository noteRepository;

    public NoteController(MembreService membreService, NoteRepository noteRepository) {
        this.membreService = membreService;
        this.noteRepository = noteRepository;
    }
    @GetMapping("/getbymembre/{id}")
    public ResponseEntity<?> getNotesByMembre(@PathVariable("id") Long idmembre){
        Optional<Membre> membreOpt = membreService.getMembreById(idmembre);
        if (membreOpt.isPresent()) {
            List<Note> notes = membreOpt.get().getNotes();
            return ResponseEntity.ok(convertToDTOList(notes));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le membre n'existe pas");
        }
    }
    @PostMapping("/update/{idmembre}")
    public ResponseEntity<?> updateNotebyMembre(@PathVariable Long idmembre, @RequestBody NoteDTO noteDTO){
        try {
            Membre membre = membreService.GetMembre(membreService.getMembreById(idmembre));
            Note note = noteRepository.findById(noteDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Note not found"));
            if (!membre.getNotes().contains(note)) {
                throw new RuntimeException("La note n'appartient pas au membre");
            }
            note.setDescription(noteDTO.getDescription());
            return ResponseEntity.ok(this.convertToDTO(noteRepository.save(note)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping("/create/{idmembre}")
    public ResponseEntity<?> createNoteByMembre(@PathVariable Long idmembre, @RequestBody NoteDTO noteDTO){
        try {
            // Vérification d'unicité : même description pour le même membre
            Membre membre = membreService.GetMembre(membreService.getMembreById(idmembre));
            boolean existe = membre.getNotes() != null &&
                membre.getNotes().stream().anyMatch(n -> n.getDescription().equalsIgnoreCase(noteDTO.getDescription()));
            if (existe) {
                throw new RuntimeException("Cette note existe déjà pour ce membre");
            }
            Note n = new Note();
            n.setMembre(membre);
            n.setDescription(noteDTO.getDescription());
            Note saved = noteRepository.save(n);
            return ResponseEntity.ok(convertToDTO(saved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Long id){
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
