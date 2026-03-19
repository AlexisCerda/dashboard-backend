package fr.prefecture.sidsic.dashboard_sidsic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import fr.prefecture.sidsic.dashboard_sidsic.dto.GroupeDTO;
import fr.prefecture.sidsic.dashboard_sidsic.dto.MembreDTO;
import fr.prefecture.sidsic.dashboard_sidsic.dto.MouvementDTO;
import fr.prefecture.sidsic.dashboard_sidsic.dto.PretDTO;
import fr.prefecture.sidsic.dashboard_sidsic.dto.TacheDTO;
import fr.prefecture.sidsic.dashboard_sidsic.dto.AchatDTO;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Achat;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Groupe;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Membre;
import fr.prefecture.sidsic.dashboard_sidsic.entity.MembreGroupe;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Mouvement;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Pret;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Tache;
import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatMouvement;
import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatPret;
import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatAchat;
import fr.prefecture.sidsic.dashboard_sidsic.repository.GroupeRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.MembreGroupeRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.MembreRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.MouvementRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.PretRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.AchatRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.TacheRepository;
import jakarta.transaction.Transactional;

@Service
public class GroupeService {

   private final GroupeRepository groupeRepository;
   private final MembreRepository membreRepository;
   private final MembreGroupeRepository membreGroupeRepository;
   private final MouvementRepository mouvementRepository;
   private final PretRepository pretRepository;
   private final AchatRepository achatRepository;
   private final TacheRepository tacheRepository;
   private final MembreService membreService;

   @Autowired
   private SimpMessagingTemplate messagingTemplate;

   public GroupeService(GroupeRepository groupeRepository, MembreRepository membreRepository,
         MembreGroupeRepository membreGroupeRepository, MouvementRepository mouvementRepository,
         PretRepository pretRepository, AchatRepository achatRepository, TacheRepository tacheRepository, MembreService membreService) {
      this.groupeRepository = groupeRepository;
      this.membreRepository = membreRepository;
      this.membreGroupeRepository = membreGroupeRepository;
      this.mouvementRepository = mouvementRepository;
      this.pretRepository = pretRepository;
      this.achatRepository = achatRepository;
      this.tacheRepository = tacheRepository;
      this.membreService = membreService;
   }

   private GroupeDTO convertToDTO(Groupe groupe) {
      GroupeDTO dto = new GroupeDTO();
      dto.setId(groupe.getId());
      dto.setNom(groupe.getNom());
      dto.setVille(groupe.getVille());
      // Ajoutez ici la conversion des listes si besoin
      return dto;
   }

   private MouvementDTO convertMouvementToDTO(Mouvement mouvement) {
      MouvementDTO dto = new MouvementDTO();
      dto.setId(mouvement.getId());
      dto.setNom(mouvement.getNom());
      dto.setPrenom(mouvement.getPrenom());
      dto.setDateArrivee(mouvement.getDateArrivee());
      dto.setDateDepart(mouvement.getDateDepart());
      dto.setEtat(mouvement.getEtat());
      return dto;
   }

   private PretDTO convertPretToDTO(Pret pret) {
      PretDTO dto = new PretDTO();
      dto.setId(pret.getId());
      dto.setNomMateriel(pret.getNomMateriel());
      dto.setMarqueMateriel(pret.getMarqueMateriel());
      dto.setNomPersonne(pret.getNomPersonne());
      dto.setPrenomPersonne(pret.getPrenomPersonne());
      dto.setQuantite(pret.getQuantite());
      dto.setEtat(pret.getEtat());
      dto.setDateDebut(pret.getDateDebut());
      dto.setDateFin(pret.getDateFin());
      return dto;
   }

   private AchatDTO convertAchatToDTO(Achat achat) {
      AchatDTO dto = new AchatDTO();
      dto.setId(achat.getId());
      dto.setNomMateriel(achat.getNomMateriel());
      dto.setMarqueMateriel(achat.getMarqueMateriel());
      dto.setReference(achat.getReference());
      dto.setNomPersonne(achat.getNomPersonne());
      dto.setPrenomPersonne(achat.getPrenomPersonne());
      dto.setQuantite(achat.getQuantite());
      dto.setEtat(achat.getEtat());
      return dto;
   }

   private TacheDTO convertTacheToDTO(Tache tache) {
      TacheDTO dto = new TacheDTO();
      dto.setId(tache.getId());
      dto.setNom(tache.getNom());
      dto.setDescription(tache.getDescription());
      dto.setDateDebut(tache.getDateDebut());
      dto.setDateLimite(tache.getDateLimite());
      dto.setEtat(tache.getEtat());
      return dto;
   }

   public Groupe getGroupeById(Long id) {
      Groupe groupe = groupeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
      return groupe;
   }

   @Transactional
   public GroupeDTO updateCurrentGroupe(Long idMembre, Long idGroupe) {
      Membre membre = membreRepository.findById(idMembre)
            .orElseThrow(() -> new RuntimeException("Membre not found"));
      Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
      membre.setCurrent_groupe(groupe);
      membreRepository.save(membre);
      return convertToDTO(groupe);
   }

   public List<MembreDTO> getMembresByRole(Long idGroupe, int role) {
      List<MembreGroupe> membresGroupes = membreGroupeRepository.findByGroupeId(idGroupe);
      List<MembreDTO> membres = new ArrayList<>();
      for (MembreGroupe membreGroupe : membresGroupes) {
         if (membreGroupe.getRole() == role) {
            membres.add(membreService.GetMembreDTO(membreGroupe.getMembre()));
         }
      }
      return membres;
   }

   public List<MembreDTO> getAllMembre(Long idGroupe) {
      List<MembreGroupe> membresGroupes = membreGroupeRepository.findByGroupeId(idGroupe);
      List<MembreDTO> membres = new ArrayList<>();
      for (MembreGroupe membreGroupe : membresGroupes) {
         membres.add(membreService.GetMembreDTO(membreGroupe.getMembre()));
      }
      return membres;
   }

   public int getUserRole(Long idMembre, Long idGroupe) {
      Membre membre = membreRepository.findById(idMembre)
            .orElseThrow(() -> new RuntimeException("Membre not found"));
      Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
      MembreGroupe membreGroupe = membreGroupeRepository.findByMembreAndGroupe(membre, groupe)
            .orElseThrow(() -> new RuntimeException("MembreGroupe not found"));
      return membreGroupe.getRole();
   }

   public List<GroupeDTO> getAllByMembre(Long idMembre) {
      Membre membre = membreRepository.findById(idMembre)
            .orElseThrow(() -> new RuntimeException("Membre not found"));
      List<GroupeDTO> groupes = new ArrayList<>();
      for (MembreGroupe membreGroupe : membre.getGroupes()) {
         groupes.add(this.convertToDTO(membreGroupe.getGroupe()));
      }
      return groupes;
   }

   public List<GroupeDTO> getGroupesByRole(Long idMembre, int role) {
      Membre membre = membreRepository.findById(idMembre)
            .orElseThrow(() -> new RuntimeException("Membre not found"));
      List<GroupeDTO> groupes = new ArrayList<>();
      for (MembreGroupe membreGroupe : membre.getGroupes()) {
         if (membreGroupe.getRole() == role) {
            groupes.add(this.convertToDTO(membreGroupe.getGroupe()));
         }
      }
      return groupes;
   }

   @Transactional
   public void deleteMembre(Long idMembre, Long idGroupe) {
      Membre membre = membreRepository.findById(idMembre)
            .orElseThrow(() -> new RuntimeException("Membre not found"));
      Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
      membreGroupeRepository.deleteByMembreAndGroupe(membre, groupe);

      if (membre.getCurrent_groupe() != null && membre.getCurrent_groupe().getId().equals(idGroupe)) {
         List<MembreGroupe> groupesRestants = membreGroupeRepository.findByMembreId(idMembre);
         if (groupesRestants.isEmpty()) {
            membre.setCurrent_groupe(null);
         } else {
            membre.setCurrent_groupe(groupesRestants.get(0).getGroupe());
         }
         membreRepository.save(membre);
      }

      long nbMembresRestants = membreGroupeRepository.findByGroupeId(idGroupe).size();
      if (nbMembresRestants == 0) {
         if (groupe.getMembres_current() != null && !groupe.getMembres_current().isEmpty()) {
            for (Membre membreCourant : groupe.getMembres_current()) {
               membreCourant.setCurrent_groupe(null);
            }
            membreRepository.saveAll(groupe.getMembres_current());
         }
         groupeRepository.delete(groupe);
         return;
      }
      String frequenceRadio = "/topic/groupe/" + idGroupe;
      messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_MEMBRES");
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
      membreGroupe.setRole(MembreGroupe.ROLE_ADMIN);
      membreGroupeRepository.save(membreGroupe);
      return this.convertToDTO(groupe);
   }

   public List<MembreDTO> addMembreToGroupe(Long idMembre, Long idGroupe, Long idMembreActuel) {
      Membre membreActuel = membreRepository.findById(idMembreActuel)
            .orElseThrow(() -> new RuntimeException("Membre not found"));
      if (this.getUserRole(membreActuel.getId(), idGroupe) != MembreGroupe.ROLE_ADMIN) {
         throw new RuntimeException("Vous n'êtes pas admin de ce groupe");
      }
      Membre membre = membreRepository.findById(idMembre)
            .orElseThrow(() -> new RuntimeException("Membre not found"));
      Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
      // Vérification d'unicité
      boolean existe = membreGroupeRepository.findAll().stream()
            .anyMatch(mg -> mg.getMembre().getId().equals(membre.getId())
                  && mg.getGroupe().getId().equals(groupe.getId()));
      if (existe) {
         throw new RuntimeException("Ce membre est déjà dans ce groupe");
      }
      MembreGroupe membreGroupe = new MembreGroupe();
      membreGroupe.setMembre(membre);
      membreGroupe.setGroupe(groupe);
      membreGroupe.setRole(MembreGroupe.ROLE_INVITE);
      membreGroupeRepository.save(membreGroupe);

      String frequenceRadio = "/topic/groupe/" + idGroupe;
      messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_MEMBRES");

      ArrayList<MembreDTO> membres = new ArrayList<>();
      for (MembreDTO membreadd : this.getAllMembre(idGroupe)) {
         membres.add(membreadd);
      }
      return membres;
   }

   public List<MembreDTO> setMembreRole(Long idMembre, Long idGroupe, int role, Long idMembreActuel) {
      if (role < MembreGroupe.ROLE_INVITE || role > MembreGroupe.ROLE_MEMBRE) {
         throw new RuntimeException(
               "Rôle invalide. Valeurs autorisées : 0 (invité), 1 (admin), 2 (membre)");
      }
      if (!(idMembreActuel == null)) {
         Membre membreActuel = membreRepository.findById(idMembreActuel)
               .orElseThrow(() -> new RuntimeException("Membre not found"));
         if (this.getUserRole(membreActuel.getId(), idGroupe) != MembreGroupe.ROLE_ADMIN) {
            throw new RuntimeException("Vous n'êtes pas admin de ce groupe");
         }
      }
      Membre membre = membreRepository.findById(idMembre)
            .orElseThrow(() -> new RuntimeException("Membre not found"));
      Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
      MembreGroupe membreGroupe = membreGroupeRepository.findByMembreAndGroupe(membre, groupe)
            .orElseThrow(() -> new RuntimeException("MembreGroupe not found"));
      membreGroupe.setRole(role);
      membreGroupeRepository.save(membreGroupe);

      String frequenceRadio = "/topic/groupe/" + idGroupe;
      messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_MEMBRES");

      List<MembreDTO> membres = new ArrayList<>();
      for (MembreDTO membreadd : this.getAllMembre(idGroupe)) {
         membres.add(membreadd);
      }
      return membres;
   }

   @Transactional
   public void deleteGroupe(Long idGroupe) {
      Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));

      if (groupe.getMembres_current() != null && !groupe.getMembres_current().isEmpty()) {
         for (Membre membreCourant : groupe.getMembres_current()) {
            membreCourant.setCurrent_groupe(null);
         }
         membreRepository.saveAll(groupe.getMembres_current());
      }

      tacheRepository.deleteTaskMemberLinksByGroupeId(idGroupe);

      groupeRepository.delete(groupe);
   }

   public GroupeDTO getCurrentGroupe(Long idMembre) {
      Membre membre = membreRepository.findById(idMembre)
            .orElseThrow(() -> new RuntimeException("Membre not found"));
      Groupe currentGroupe = membre.getCurrent_groupe();
      if (currentGroupe == null) {
         return null;
      }

      boolean estToujoursDansLeGroupeCourant = membreGroupeRepository
            .findByMembreAndGroupe(membre, currentGroupe).isPresent();
      if (!estToujoursDansLeGroupeCourant) {
         List<MembreGroupe> groupesRestants = membreGroupeRepository.findByMembreId(idMembre);
         if (groupesRestants.isEmpty()) {
            membre.setCurrent_groupe(null);
            membreRepository.save(membre);
            return null;
         }

         Groupe nouveauGroupeCourant = groupesRestants.get(0).getGroupe();
         membre.setCurrent_groupe(nouveauGroupeCourant);
         membreRepository.save(membre);
         return convertToDTO(nouveauGroupeCourant);
      }

      return convertToDTO(currentGroupe);
   }

   // ##### PARTIE GESTION Mouvements ######

   public List<MouvementDTO> getAllMouvementsByGroupe(Long idGroupe) {
      Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
      return groupe.getMouvements().stream().map(this::convertMouvementToDTO).collect(Collectors.toList());
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

      String frequenceRadio = "/topic/groupe/" + idGroupe;
      messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_MOUVEMENTS");

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
      // Vérification d'unicité : même nom, prénom et dates dans le même groupe
      boolean existe = groupe.getMouvements().stream()
            .anyMatch(m -> m.getNom().equalsIgnoreCase(mouvementDTO.getNom()) &&
                  m.getPrenom().equalsIgnoreCase(mouvementDTO.getPrenom()) &&
                  ((m.getDateArrivee() != null && m
                        .getDateArrivee().equals(mouvementDTO.getDateArrivee()))
                        ||
                        (m.getDateArrivee() == null && mouvementDTO
                              .getDateArrivee() == null))
                  &&
                  ((m.getDateDepart() != null && m.getDateDepart()
                        .equals(mouvementDTO.getDateDepart())) ||
                        (m.getDateDepart() == null && mouvementDTO
                              .getDateDepart() == null)));
      if (existe) {
         throw new RuntimeException("Ce mouvement existe déjà dans ce groupe");
      }
      Mouvement mouvement = new Mouvement();
      mouvement.setDateArrivee(mouvementDTO.getDateArrivee());
      mouvement.setDateDepart(mouvementDTO.getDateDepart());
      mouvement.setNom(mouvementDTO.getNom());
      mouvement.setPrenom(mouvementDTO.getPrenom());
      mouvement.setGroupe(groupe);
      mouvement.setEtat(mouvementDTO.getEtat());
      mouvementRepository.save(mouvement);

      String frequenceRadio = "/topic/groupe/" + idGroupe;
      messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_MOUVEMENTS");

      MouvementDTO createdMouvementDTO = new MouvementDTO();
      createdMouvementDTO.setId(mouvement.getId());
      createdMouvementDTO.setDateArrivee(mouvement.getDateArrivee());
      createdMouvementDTO.setDateDepart(mouvement.getDateDepart());
      createdMouvementDTO.setNom(mouvement.getNom());
      createdMouvementDTO.setPrenom(mouvement.getPrenom());
      createdMouvementDTO.setEtat(mouvement.getEtat());
      return createdMouvementDTO;
   }

   @Transactional
   public void deleteMouvement(Long idMouvement, Long idGroupe) {
      Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
      Mouvement mouvement = mouvementRepository.findById(idMouvement)
            .orElseThrow(() -> new RuntimeException("Mouvement not found"));
      if (!mouvement.getGroupe().getId().equals(groupe.getId())) {
         throw new RuntimeException("Ce mouvement n'appartient pas à ce groupe");
      }
      mouvementRepository.delete(mouvement);

      String frequenceRadio = "/topic/groupe/" + idGroupe;
      messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_MOUVEMENTS");
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
   public List<PretDTO> getAllPretsByGroupe(Long idGroupe) {
      Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
      return groupe.getPrets().stream().map(this::convertPretToDTO).collect(Collectors.toList());
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
      if (pretDTO.getEtat() != null) {
         pret.setEtat(pretDTO.getEtat());
      }
      pret.setDateDebut(pretDTO.getDateDebut());
      pret.setDateFin(pretDTO.getDateFin());
      pretRepository.save(pret);

      String frequenceRadio = "/topic/groupe/" + idGroupe;
      messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_PRETS");

      PretDTO updatedPretDTO = new PretDTO();
      updatedPretDTO.setId(pret.getId());
      updatedPretDTO.setNomMateriel(pret.getNomMateriel());
      updatedPretDTO.setMarqueMateriel(pret.getMarqueMateriel());
      updatedPretDTO.setNomPersonne(pret.getNomPersonne());
      updatedPretDTO.setPrenomPersonne(pret.getPrenomPersonne());
      updatedPretDTO.setQuantite(pret.getQuantite());
      updatedPretDTO.setEtat(pret.getEtat());
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
      pret.setEtat(pretDTO.getEtat() != null ? pretDTO.getEtat() : EtatPret.EN_COURS);
      pret.setDateDebut(pretDTO.getDateDebut());
      pret.setDateFin(pretDTO.getDateFin());
      pret.setGroupe(groupe);
      pretRepository.save(pret);

      String frequenceRadio = "/topic/groupe/" + idGroupe;
      messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_PRETS");

      PretDTO createdPretDTO = new PretDTO();
      createdPretDTO.setId(pret.getId());
      createdPretDTO.setNomMateriel(pret.getNomMateriel());
      createdPretDTO.setMarqueMateriel(pret.getMarqueMateriel());
      createdPretDTO.setNomPersonne(pret.getNomPersonne());
      createdPretDTO.setPrenomPersonne(pret.getPrenomPersonne());
      createdPretDTO.setQuantite(pret.getQuantite());
      createdPretDTO.setEtat(pret.getEtat());
      createdPretDTO.setDateDebut(pret.getDateDebut());
      createdPretDTO.setDateFin(pret.getDateFin());
      return createdPretDTO;
   }

   @Transactional
   public void deletePret(Long idPret, Long idGroupe) {
      Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
      Pret pret = pretRepository.findById(idPret)
            .orElseThrow(() -> new RuntimeException("Pret not found"));
      if (!pret.getGroupe().getId().equals(groupe.getId())) {
         throw new RuntimeException("Ce pret n'appartient pas à ce groupe");
      }
      pretRepository.delete(pret);

      String frequenceRadio = "/topic/groupe/" + idGroupe;
      messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_PRETS");
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
   public List<AchatDTO> getAllAchatsByGroupe(Long idGroupe) {
      Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
      return groupe.getAchats().stream().map(this::convertAchatToDTO).collect(Collectors.toList());
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
      achat.setReference(achatDTO.getReference());
      achat.setNomPersonne(achatDTO.getNomPersonne());
      achat.setPrenomPersonne(achatDTO.getPrenomPersonne());
      achat.setQuantite(achatDTO.getQuantite());
      if (achatDTO.getEtat() != null) {
         achat.setEtat(achatDTO.getEtat());
      }
      achatRepository.save(achat);

      String frequenceRadio = "/topic/groupe/" + idGroupe;
      messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_ACHATS");

      AchatDTO updatedAchatDTO = new AchatDTO();
      updatedAchatDTO.setId(achat.getId());
      updatedAchatDTO.setNomMateriel(achat.getNomMateriel());
      updatedAchatDTO.setMarqueMateriel(achat.getMarqueMateriel());
      updatedAchatDTO.setReference(achat.getReference());
      updatedAchatDTO.setNomPersonne(achat.getNomPersonne());
      updatedAchatDTO.setPrenomPersonne(achat.getPrenomPersonne());
      updatedAchatDTO.setQuantite(achat.getQuantite());
      updatedAchatDTO.setEtat(achat.getEtat());
      return updatedAchatDTO;
   }

   @Transactional
   public AchatDTO createAchat(AchatDTO achatDTO, Long idGroupe) {
      Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
      // Vérification d'unicité : même nom matériel, marque et personne dans le même
      // groupe
      boolean existe = groupe.getAchats().stream()
            .anyMatch(a -> a.getNomMateriel().equalsIgnoreCase(achatDTO.getNomMateriel()) &&
                  a.getMarqueMateriel().equalsIgnoreCase(achatDTO.getMarqueMateriel()) &&
                  a.getNomPersonne().equalsIgnoreCase(achatDTO.getNomPersonne()) &&
                  a.getPrenomPersonne().equalsIgnoreCase(achatDTO.getPrenomPersonne()));
      if (existe) {
         throw new RuntimeException("Cet achat existe déjà dans ce groupe");
      }
      Achat achat = new Achat();
      achat.setNomMateriel(achatDTO.getNomMateriel());
      achat.setMarqueMateriel(achatDTO.getMarqueMateriel());
      achat.setReference(achatDTO.getReference());
      achat.setNomPersonne(achatDTO.getNomPersonne());
      achat.setPrenomPersonne(achatDTO.getPrenomPersonne());
      achat.setQuantite(achatDTO.getQuantite());
      achat.setGroupe(groupe);
      achat.setEtat(achatDTO.getEtat() != null ? achatDTO.getEtat() : EtatAchat.à_ACHETER);
      achatRepository.save(achat);

      String frequenceRadio = "/topic/groupe/" + idGroupe;
      messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_ACHATS");

      AchatDTO createdAchatDTO = new AchatDTO();
      createdAchatDTO.setId(achat.getId());
      createdAchatDTO.setNomMateriel(achat.getNomMateriel());
      createdAchatDTO.setMarqueMateriel(achat.getMarqueMateriel());
      createdAchatDTO.setReference(achat.getReference());
      createdAchatDTO.setNomPersonne(achat.getNomPersonne());
      createdAchatDTO.setPrenomPersonne(achat.getPrenomPersonne());
      createdAchatDTO.setQuantite(achat.getQuantite());
      createdAchatDTO.setEtat(achat.getEtat());
      return createdAchatDTO;
   }

   @Transactional
   public void deleteAchat(Long idAchat, Long idGroupe) {
      Groupe groupe = groupeRepository.findById(idGroupe)
            .orElseThrow(() -> new RuntimeException("Groupe not found"));
      Achat achat = achatRepository.findById(idAchat)
            .orElseThrow(() -> new RuntimeException("Achat not found"));
      if (!achat.getGroupe().getId().equals(groupe.getId())) {
         throw new RuntimeException("Cet achat n'appartient pas à ce groupe");
      }
      achatRepository.delete(achat);

      String frequenceRadio = "/topic/groupe/" + idGroupe;
      messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_ACHATS");
   }

   public EtatAchat getEtatsAchat(Long idAchat) {
      Achat achat = achatRepository.findById(idAchat)
            .orElseThrow(() -> new RuntimeException("Achat not found"));
      return achat.getEtat();
   }

   public List<EtatAchat> getAllEtatsAchat() {
      List<EtatAchat> etats = new ArrayList<>();
      for (EtatAchat etat : EtatAchat.values()) {
         etats.add(etat);
      }
      return etats;
   }

   public Mouvement getMouvementById(Long id) {
      Mouvement mouvement = mouvementRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mouvement not found"));
      return mouvement;
   }

   public Achat getAchatById(Long id) {
      Achat achat = achatRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Achat not found"));
      return achat;
   }

   public Pret getPretById(Long id) {
      Pret pret = pretRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pret not found"));
      return pret;
   }

   @Transactional
   public MouvementDTO updateMouvementEtat(Mouvement dto) {
      Mouvement mouvement = mouvementRepository.findById(dto.getId())
            .orElseThrow(() -> new RuntimeException("Mouvement not found"));
      mouvement.setEtat(dto.getEtat());
      mouvementRepository.save(mouvement);

      String frequenceRadio = "/topic/groupe/" + mouvement.getGroupe().getId();
      messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_MOUVEMENTS");

      return convertMouvementToDTO(mouvement);
   }

   @Transactional
   public AchatDTO updateAchatEtat(Achat dto) {
      Achat achat = achatRepository.findById(dto.getId())
            .orElseThrow(() -> new RuntimeException("Achat not found"));
      achat.setEtat(dto.getEtat());
      achatRepository.save(achat);

      String frequenceRadio = "/topic/groupe/" + achat.getGroupe().getId();
      messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_ACHATS");

      return convertAchatToDTO(achat);
   }

   @Transactional
   public PretDTO updatePretEtat(Pret dto) {
      Pret pret = pretRepository.findById(dto.getId())
            .orElseThrow(() -> new RuntimeException("Pret not found"));
      pret.setEtat(dto.getEtat());
      pretRepository.save(pret);

      String frequenceRadio = "/topic/groupe/" + pret.getGroupe().getId();
      messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_PRETS");

      return convertPretToDTO(pret);
   }

   public List<GroupeDTO> getAllGroupes() {
      List<GroupeDTO> LDTO = new ArrayList<>();
      List<Groupe> L = groupeRepository.findAll();
      for (Groupe groupe : L) {
         GroupeDTO g = new GroupeDTO();
         g.setId(groupe.getId());
         g.setNom(groupe.getNom());
         LDTO.add(g);
      }
      return LDTO;
   }
}
