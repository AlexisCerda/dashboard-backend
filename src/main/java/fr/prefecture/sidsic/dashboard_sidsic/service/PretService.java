package fr.prefecture.sidsic.dashboard_sidsic.service;

import org.springframework.stereotype.Service;
import fr.prefecture.sidsic.dashboard_sidsic.repository.PretRepository;

@Service
public class PretService {
    private final PretRepository pretRepository;

    public PretService(PretRepository pretRepository) {
        this.pretRepository = pretRepository;
    }
}
