package fr.prefecture.sidsic.dashboard_sidsic.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.prefecture.sidsic.dashboard_sidsic.dto.GroupeDTO;
import fr.prefecture.sidsic.dashboard_sidsic.dto.MouvementDTO;
import fr.prefecture.sidsic.dashboard_sidsic.dto.PretDTO;
import fr.prefecture.sidsic.dashboard_sidsic.dto.AchatDTO;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Achat;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Groupe;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Membre;
import fr.prefecture.sidsic.dashboard_sidsic.entity.MembreGroupe;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Mouvement;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Pret;
import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatMouvement;
import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatPret;
import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatAchat;
import fr.prefecture.sidsic.dashboard_sidsic.repository.GroupeRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.MembreGroupeRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.MembreRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.MouvementRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.PretRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.AchatRepository;
import jakarta.transaction.Transactional;

@Service
public class GroupeService {
    private final GroupeRepository groupeRepository;
    private final MembreRepository membreRepository;
    private final MembreGroupeRepository membreGroupeRepository;
    private final MouvementRepository mouvementRepository;
    private final PretRepository pretRepository;
    private final AchatRepository achatRepository;

    public GroupeService(GroupeRepository groupeRepository, MembreRepository membreRepository, MembreGroupeRepository membreGroupeRepository, MouvementRepository mouvementRepository, PretRepository pretRepository, AchatRepository achatRepository) {
        this.groupeRepository = groupeRepository;
        this.membreRepository = membreRepository;
        this.membreGroupeRepository = membreGroupeRepository;
        this.mouvementRepository = mouvementRepository;
        this.pretRepository = pretRepository;
        this.achatRepository = achatRepository;
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
        groupeDTO.setId(groupe.getId());
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


    //##### PARTIE GESTION Mouvements ######

    public List<Mouvement> getAllMouvementsByGroupe(Long idGroupe) {
        Groupe groupe = groupeRepository.findById(idGroupe)
                .orElseThrow(() -> new RuntimeException("Groupe not found"));
        return groupe.getMouvements();
    }

    @Transactional
    public MouvementDTO updateMouvement(MouvementDTO mouvementDTO, Long idGroupe) {
        Groupe groupe = groupeRepository.findById(idGroupe)
                .orElseThrow(() -> new RuntimeException("Groupe not found"));
        Mouvement mouvement = mouvementRepository.findById(mouvementDTO.getId())
                .orElseThrow(() -> new RuntimeException("Mouvement not found"));
        if (!mouvement.getGroupe().getId().equals(groupe.getId())) {
            throw new RuntimeException("Ce mouvement n'appartient pas à ce groupe");
        }
        mouvement.setDateArrivee(mouvementDTO.getDateArrivee());
        mouvement.setDateDepart(mouvementDTO.getDateDepart());
        mouvement.setNom(mouvementDTO.getNom());
        mouvement.setPrenom(mouvementDTO.getPrenom());
        groupeRepository.save(groupe);
        MouvementDTO updatedMouvementDTO = new MouvementDTO();
        updatedMouvementDTO.setId(mouvement.getId());
        updatedMouvementDTO.setDateArrivee(mouvement.getDateArrivee());
        updatedMouvementDTO.setDateDepart(mouvement.getDateDepart());
        updatedMouvementDTO.setNom(mouvement.getNom());
        updatedMouvementDTO.setPrenom(mouvement.getPrenom());
        return updatedMouvementDTO;
    }

    @Transactional
    public MouvementDTO createMouvement(MouvementDTO mouvementDTO, Long idGroupe) {
        Groupe groupe = groupeRepository.findById(idGroupe)
                .orElseThrow(() -> new RuntimeException("Groupe not found"));
        Mouvement mouvement = new Mouvement();
        mouvement.setDateArrivee(mouvementDTO.getDateArrivee());
        mouvement.setDateDepart(mouvementDTO.getDateDepart());
        mouvement.setNom(mouvementDTO.getNom());
        mouvement.setPrenom(mouvementDTO.getPrenom());
        mouvement.setGroupe(groupe);
        mouvementRepository.save(mouvement);
        MouvementDTO createdMouvementDTO = new MouvementDTO();
        createdMouvementDTO.setId(mouvement.getId());
        createdMouvementDTO.setDateArrivee(mouvement.getDateArrivee());
        createdMouvementDTO.setDateDepart(mouvement.getDateDepart());
        createdMouvementDTO.setNom(mouvement.getNom());
        createdMouvementDTO.setPrenom(mouvement.getPrenom());
        return createdMouvementDTO;
    }

    public void deleteMouvement(Long idMouvement, Long idGroupe) {
        Groupe groupe = groupeRepository.findById(idGroupe)
                .orElseThrow(() -> new RuntimeException("Groupe not found"));
        Mouvement mouvement = mouvementRepository.findById(idMouvement)
                .orElseThrow(() -> new RuntimeException("Mouvement not found"));
        if (!mouvement.getGroupe().getId().equals(groupe.getId())) {
            throw new RuntimeException("Ce mouvement n'appartient pas à ce groupe");
        }
        mouvementRepository.delete(mouvement);
    }

    public EtatMouvement getEtatsMouvement(Long idMouvement) {
        Mouvement mouvement = mouvementRepository.findById(idMouvement)
                .orElseThrow(() -> new RuntimeException("Mouvement not found"));
        return mouvement.getEtat();
    }
    public List<EtatMouvement> getAllEtatsMouvement() {
        List<EtatMouvement> etats = new ArrayList<>();
        for (EtatMouvement etat : EtatMouvement.values()) {
            etats.add(etat);
        }
        return etats;
    }
// ##### PARTIE GESTION PRET ######
public List<Pret> getAllPretsByGroupe(Long idGroupe) {
    Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
    return groupe.getPrets();
}

@Transactional
public PretDTO updatePret(PretDTO pretDTO, Long idGroupe) {
    Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
    Pret pret = pretRepository.findById(pretDTO.getId())
            .orElseThrow(() -> new RuntimeException("Pret not found"));
    if (!pret.getGroupe().getId().equals(groupe.getId())) {
        throw new RuntimeException("Ce pret n'appartient pas à ce groupe");
    }
    pret.setNomMateriel(pretDTO.getNomMateriel());
    pret.setMarqueMateriel(pretDTO.getMarqueMateriel());
    pret.setNomPersonne(pretDTO.getNomPersonne());
    pret.setPrenomPersonne(pretDTO.getPrenomPersonne());
    pret.setQuantite(pretDTO.getQuantite());
    pret.setDateDebut(pretDTO.getDateDebut());
    pret.setDateFin(pretDTO.getDateFin());
    pretRepository.save(pret);
    PretDTO updatedPretDTO = new PretDTO();
    updatedPretDTO.setId(pret.getId());
    updatedPretDTO.setNomMateriel(pret.getNomMateriel());
    updatedPretDTO.setMarqueMateriel(pret.getMarqueMateriel());
    updatedPretDTO.setNomPersonne(pret.getNomPersonne());
    updatedPretDTO.setPrenomPersonne(pret.getPrenomPersonne());
    updatedPretDTO.setQuantite(pret.getQuantite());
    updatedPretDTO.setDateDebut(pret.getDateDebut());
    updatedPretDTO.setDateFin(pret.getDateFin());
    return updatedPretDTO;
}

@Transactional
public PretDTO createPret(PretDTO pretDTO, Long idGroupe) {
    Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
    Pret pret = new Pret();
    pret.setNomMateriel(pretDTO.getNomMateriel());
    pret.setMarqueMateriel(pretDTO.getMarqueMateriel());
    pret.setNomPersonne(pretDTO.getNomPersonne());
    pret.setPrenomPersonne(pretDTO.getPrenomPersonne());
    pret.setQuantite(pretDTO.getQuantite());
    pret.setDateDebut(pretDTO.getDateDebut());
    pret.setDateFin(pretDTO.getDateFin());
    pret.setGroupe(groupe);
    pretRepository.save(pret);
    PretDTO createdPretDTO = new PretDTO();
    createdPretDTO.setId(pret.getId());
    createdPretDTO.setNomMateriel(pret.getNomMateriel());
    createdPretDTO.setMarqueMateriel(pret.getMarqueMateriel());
    createdPretDTO.setNomPersonne(pret.getNomPersonne());
    createdPretDTO.setPrenomPersonne(pret.getPrenomPersonne());
    createdPretDTO.setQuantite(pret.getQuantite());
    createdPretDTO.setDateDebut(pret.getDateDebut());
    createdPretDTO.setDateFin(pret.getDateFin());
    return createdPretDTO;
}

public void deletePret(Long idPret, Long idGroupe) {
    Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
    Pret pret = pretRepository.findById(idPret)
            .orElseThrow(() -> new RuntimeException("Pret not found"));
    if (!pret.getGroupe().getId().equals(groupe.getId())) {
        throw new RuntimeException("Ce pret n'appartient pas à ce groupe");
    }
    pretRepository.delete(pret);
}

public EtatPret getEtatsPret(Long idPret) {
    Pret pret = pretRepository.findById(idPret)
            .orElseThrow(() -> new RuntimeException("Pret not found"));
    return pret.getEtat();
}
public List<EtatPret> getAllEtatsPret() {
    List<EtatPret> etats = new ArrayList<>();
    for (EtatPret etat : EtatPret.values()) {
        etats.add(etat);
    }
    return etats;
}

// ##### PARTIE GESTION ACHAT ######
public List<Achat> getAllAchatsByGroupe(Long idGroupe) {
    Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
    return groupe.getAchats();
}

@Transactional
public AchatDTO updateAchat(AchatDTO achatDTO, Long idGroupe) {
    Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
    Achat achat = achatRepository.findById(achatDTO.getId())
            .orElseThrow(() -> new RuntimeException("Achat not found"));
    if (!achat.getGroupe().getId().equals(groupe.getId())) {
        throw new RuntimeException("Cet achat n'appartient pas à ce groupe");
    }
    achat.setNomMateriel(achatDTO.getNomMateriel());
    achat.setMarqueMateriel(achatDTO.getMarqueMateriel());
    achat.setNomPersonne(achatDTO.getNomPersonne());
    achat.setPrenomPersonne(achatDTO.getPrenomPersonne());
    achat.setQuantite(achatDTO.getQuantite());
    achatRepository.save(achat);
    AchatDTO updatedAchatDTO = new AchatDTO();
    updatedAchatDTO.setId(achat.getId());
    updatedAchatDTO.setNomMateriel(achat.getNomMateriel());
    updatedAchatDTO.setMarqueMateriel(achat.getMarqueMateriel());
    updatedAchatDTO.setNomPersonne(achat.getNomPersonne());
    updatedAchatDTO.setPrenomPersonne(achat.getPrenomPersonne());
    updatedAchatDTO.setQuantite(achat.getQuantite());
    return updatedAchatDTO;
}

@Transactional
public AchatDTO createAchat(AchatDTO achatDTO, Long idGroupe) {
    Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
    Achat achat = new Achat();
    achat.setNomMateriel(achatDTO.getNomMateriel());
    achat.setMarqueMateriel(achatDTO.getMarqueMateriel());
    achat.setNomPersonne(achatDTO.getNomPersonne());
    achat.setPrenomPersonne(achatDTO.getPrenomPersonne());
    achat.setQuantite(achatDTO.getQuantite());
    achat.setGroupe(groupe);
    achatRepository.save(achat);
    AchatDTO createdAchatDTO = new AchatDTO();
    createdAchatDTO.setId(achat.getId());
    createdAchatDTO.setNomMateriel(achat.getNomMateriel());
    createdAchatDTO.setMarqueMateriel(achat.getMarqueMateriel());
    createdAchatDTO.setNomPersonne(achat.getNomPersonne());
    createdAchatDTO.setPrenomPersonne(achat.getPrenomPersonne());
    createdAchatDTO.setQuantite(achat.getQuantite());
    return createdAchatDTO;
}

public void deleteAchat(Long idAchat, Long idGroupe) {
    Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
    Achat achat = achatRepository.findById(idAchat)
            .orElseThrow(() -> new RuntimeException("Achat not found"));
    if (!achat.getGroupe().getId().equals(groupe.getId())) {
        throw new RuntimeException("Cet achat n'appartient pas à ce groupe");
    }
    achatRepository.delete(achat);
}

public EtatAchat getEtatsAchat(Long idAchat) {
    Achat achat = achatRepository.findById(idAchat)
            .orElseThrow(() -> new RuntimeException("Achat not found"));
    return achat.getEtat_courant();
}
public List<EtatAchat> getAllEtatsAchat() {
    List<EtatAchat> etats = new ArrayList<>();
    for (EtatAchat etat : EtatAchat.values()) {
        etats.add(etat);
    }
    return etats;
}
}
