package fr.prefecture.sidsic.dashboard_sidsic.service;

import org.springframework.stereotype.Service;
import fr.prefecture.sidsic.dashboard_sidsic.repository.NoteRepository;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }
}
