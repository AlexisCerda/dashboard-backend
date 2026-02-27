package fr.prefecture.sidsic.dashboard_sidsic.service;

import org.springframework.stereotype.Service;

import fr.prefecture.sidsic.dashboard_sidsic.repository.AchatRepository;

@Service
public class AchatService {
    private final AchatRepository  achatRepository;

    public AchatService(AchatRepository AchatRepository) {
        this.achatRepository = AchatRepository;
    }

}
