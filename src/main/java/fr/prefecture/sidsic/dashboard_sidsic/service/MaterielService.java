package fr.prefecture.sidsic.dashboard_sidsic.service;

import org.springframework.stereotype.Service;

import fr.prefecture.sidsic.dashboard_sidsic.repository.MaterielRepository;

@Service
public class MaterielService {
    private final MaterielRepository  materielRepository;

    public MaterielService(MaterielRepository MaterielRepository) {
        this.materielRepository = MaterielRepository;
    }

}
