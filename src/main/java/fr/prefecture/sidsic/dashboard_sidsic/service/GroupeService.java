package fr.prefecture.sidsic.dashboard_sidsic.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.prefecture.sidsic.dashboard_sidsic.entity.Groupe;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Membre;
import fr.prefecture.sidsic.dashboard_sidsic.entity.MembreGroupe;
import fr.prefecture.sidsic.dashboard_sidsic.repository.GroupeRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.MembreGroupeRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.MembreRepository;
import jakarta.transaction.Transactional;

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

    @Transactional
    public Groupe updateCurrentGroupe(Long idMembre, Long idGroupe) {
        Membre membre = membreRepository.findById(idMembre)
                .orElseThrow(() -> new RuntimeException("Membre not found"));
        Groupe groupe = groupeRepository.findById(idGroupe)
                .orElseThrow(() -> new RuntimeException("Groupe not found"));
        membre.setCurrent_groupe(groupe);
        membreRepository.save(membre);
        return groupe;
    }

    public List<Membre> getAllAdmin() {
        List<MembreGroupe> membresGroupes = membreGroupeRepository.findByIsAdmin();
        List<Membre> membres = new ArrayList<>();
        for (MembreGroupe membreGroupe : membresGroupes) {
            if (membreGroupe.getIsAdmin() == 1) {
                membres.add(membreGroupe.getMembre());
            }
        }
        return membres;
    }

    public List<Membre> getAllMembre() {
        List<MembreGroupe> membresGroupes = membreGroupeRepository.findAll();
        List<Membre> membres = new ArrayList<>();
        for (MembreGroupe membreGroupe : membresGroupes) {
            membres.add(membreGroupe.getMembre());
        }
        return membres;
    }

    public List<Groupe> getAllByMembre(Long idMembre) {
        Membre membre = membreRepository.findById(idMembre)
                .orElseThrow(() -> new RuntimeException("Membre not found"));
        List<Groupe> groupes = new ArrayList<>();
        for (MembreGroupe membreGroupe : membre.getGroupes()) {
            groupes.add(membreGroupe.getGroupe());
        }
        return groupes;
    }

    public List<Groupe> getAllByMembreAdmin(Long idMembre) {
        Membre membre = membreRepository.findById(idMembre)
                .orElseThrow(() -> new RuntimeException("Membre not found"));
        List<Groupe> groupes = new ArrayList<>();
        for (MembreGroupe membreGroupe : membre.getGroupes()) {
            if (membreGroupe.getIsAdmin() == 1) {
                groupes.add(membreGroupe.getGroupe());
            }
        }
        return groupes;
    }

}
