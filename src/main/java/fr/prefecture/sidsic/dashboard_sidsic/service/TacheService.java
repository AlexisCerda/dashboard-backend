package fr.prefecture.sidsic.dashboard_sidsic.service;

import org.springframework.stereotype.Service;
import fr.prefecture.sidsic.dashboard_sidsic.repository.TacheRepository;

@Service
public class TacheService {
    private final TacheRepository tacheRepository;

    public TacheService(TacheRepository tacheRepository) {
        this.tacheRepository = tacheRepository;
    }
}
