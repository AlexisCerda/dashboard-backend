package fr.prefecture.sidsic.dashboard_sidsic.service;

import org.springframework.stereotype.Service;
import fr.prefecture.sidsic.dashboard_sidsic.repository.MouvementRepository;

@Service
public class MouvementService {
    private final MouvementRepository mouvementRepository;

    public MouvementService(MouvementRepository mouvementRepository) {
        this.mouvementRepository = mouvementRepository;
    }
}
