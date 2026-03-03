package fr.prefecture.sidsic.dashboard_sidsic.service;

import org.springframework.stereotype.Service;

import fr.prefecture.sidsic.dashboard_sidsic.entity.Groupe;
import fr.prefecture.sidsic.dashboard_sidsic.repository.GroupeRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.MembreGroupeRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.MembreRepository;

@Service
public class GroupeService {
    private final GroupeRepository groupeRepository;
    private final MembreRepository membreRepository;
    private final MembreGroupeRepository membreGroupeRepository;

    public GroupeService(GroupeRepository groupeRepository, MembreRepository membreRepository, MembreGroupeRepository membreGroupeRepository) {
        this.groupeRepository = groupeRepository;
        this.membreRepository = membreRepository;
        this.membreGroupeRepository = membreGroupeRepository;
    }
    public Groupe getGroupeById(Long id){
        return groupeRepository.findById(id).orElseThrow(() -> new RuntimeException("Groupe not found"));
    }
    

}
