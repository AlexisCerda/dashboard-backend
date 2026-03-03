package fr.prefecture.sidsic.dashboard_sidsic.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.prefecture.sidsic.dashboard_sidsic.dto.GroupeDTO;
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

    public List<Membre> getAllAdmin(Long idGroupe) {
        List<MembreGroupe> membresGroupes = membreGroupeRepository.findByGroupeId(idGroupe);
        List<Membre> membres = new ArrayList<>();
        for (MembreGroupe membreGroupe : membresGroupes) {
            if (membreGroupe.getIsAdmin() == 1) {
                membres.add(membreGroupe.getMembre());
            }
        }
        return membres;
    }

    public List<Membre> getAllMembre(Long idGroupe) {
        List<MembreGroupe> membresGroupes = membreGroupeRepository.findByGroupeId(idGroupe);
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

    public void deleteMembre(Long idMembre, Long idGroupe) {
        Membre membre = membreRepository.findById(idMembre)
                .orElseThrow(() -> new RuntimeException("Membre not found"));
        Groupe groupe = groupeRepository.findById(idGroupe)
                .orElseThrow(() -> new RuntimeException("Groupe not found"));
        membreGroupeRepository.deleteByMembreAndGroupe(membre, groupe);
    }

    @Transactional
    public GroupeDTO createGroupe(Long idMembre, GroupeDTO groupeDTO) {
        Membre membre = membreRepository.findById(idMembre)
                .orElseThrow(() -> new RuntimeException("Membre not found"));
        Groupe groupe = new Groupe();
        groupe.setNom(groupeDTO.getNom());
        groupe.setVille(groupeDTO.getVille());
        groupeRepository.save(groupe);
        MembreGroupe membreGroupe = new MembreGroupe();
        membreGroupe.setMembre(membre);
        membreGroupe.setGroupe(groupe);
        membreGroupe.setIsAdmin(1);
        membreGroupeRepository.save(membreGroupe);
        return this.convertToDTO(groupe);
    }

    public GroupeDTO convertToDTO(Groupe groupe) {
        GroupeDTO groupeDTO = new GroupeDTO();
        groupeDTO.setId(groupe.getID());
        groupeDTO.setNom(groupe.getNom());
        groupeDTO.setVille(groupe.getVille());
        return groupeDTO;
    }

    public List<Membre> addMembreToGroupe(Long idMembre, Long idGroupe, Long idMembreActuel) {
        Membre membreActuel = membreRepository.findById(idMembreActuel)
                .orElseThrow(() -> new RuntimeException("Membre not found"));
        if (this.getAllAdmin(idGroupe).stream().noneMatch(m -> m.getId().equals(membreActuel.getId()))) {
            throw new RuntimeException("Vous n'êtes pas admin de ce groupe");
        }
        Membre membre = membreRepository.findById(idMembre)
                .orElseThrow(() -> new RuntimeException("Membre not found"));
        Groupe groupe = groupeRepository.findById(idGroupe)
                .orElseThrow(() -> new RuntimeException("Groupe not found"));
        MembreGroupe membreGroupe = new MembreGroupe();
        membreGroupe.setMembre(membre);
        membreGroupe.setGroupe(groupe);
        membreGroupe.setIsAdmin(0);
        membreGroupeRepository.save(membreGroupe);
        return this.getAllMembre(idGroupe);
    }

    public List<Membre> updateMembreToAdmin(Long idMembre, Long idGroupe, Boolean isAdmin, Long idMembreActuel) {
        Membre membreActuel = membreRepository.findById(idMembreActuel)
                .orElseThrow(() -> new RuntimeException("Membre not found"));
        if (this.getAllAdmin(idGroupe).stream().noneMatch(m -> m.getId().equals(membreActuel.getId()))) {
            throw new RuntimeException("Vous n'êtes pas admin de ce groupe");
        }
        Membre membre = membreRepository.findById(idMembre)
                .orElseThrow(() -> new RuntimeException("Membre not found"));
        Groupe groupe = groupeRepository.findById(idGroupe)
                .orElseThrow(() -> new RuntimeException("Groupe not found"));
        MembreGroupe membreGroupe = membreGroupeRepository.findByMembreAndGroupe(membre, groupe)
                .orElseThrow(() -> new RuntimeException("MembreGroupe not found"));
        membreGroupe.setIsAdmin(isAdmin ? 1 : 0);
        membreGroupeRepository.save(membreGroupe);
        return this.getAllAdmin(idGroupe);
    }
    public void deleteGroupe(Long idGroupe) {
        Groupe groupe = groupeRepository.findById(idGroupe)
                .orElseThrow(() -> new RuntimeException("Groupe not found"));
        groupeRepository.delete(groupe);
    }
}
