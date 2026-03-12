package fr.prefecture.sidsic.dashboard_sidsic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import fr.prefecture.sidsic.dashboard_sidsic.dto.MembreDTO;
import fr.prefecture.sidsic.dashboard_sidsic.dto.TacheDTO;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Groupe;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Membre;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Tache;
import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatTache;
import fr.prefecture.sidsic.dashboard_sidsic.repository.GroupeRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.MembreGroupeRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.MembreRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.TacheRepository;
import jakarta.transaction.Transactional;

@Service
public class MembreService {
    private final MembreRepository  membreRepository;
    private final TacheRepository tacheRepository;
    private final GroupeRepository groupeRepository;
    private final MembreGroupeRepository membreGroupeRepository;
    //private final BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public MembreService(MembreRepository membreRepository, TacheRepository tacheRepository,
            GroupeRepository groupeRepository, MembreGroupeRepository membreGroupeRepository) {
        this.membreRepository = membreRepository;
        this.tacheRepository = tacheRepository;
        this.groupeRepository = groupeRepository;
        this.membreGroupeRepository = membreGroupeRepository;
        //this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<MembreDTO> recupererToutLesMembres() {
        List<Membre> lMembre = membreRepository.findAll();
        List<MembreDTO> lMembreDTO = new ArrayList<>();
        for (Membre membre : lMembre) {
            lMembreDTO.add(this.GetMembreDTO(membre));
        }
        return lMembreDTO;
    }

    public Optional<Membre> getMembreById(Long id){
        Optional<Membre> membre = membreRepository.findById(id);
        return membre;
    }
    public Optional<Membre> getMembreByEmail(String mail){
        Optional<Membre> membre = membreRepository.findByEmail(mail);
        return membre;
    }

    public MembreDTO creerUnNouveauMembre(MembreDTO nouveauMembre, String mdp)throws RuntimeException {
        if (this.getMembreByEmail(nouveauMembre.getEmail()).isPresent()) {
            throw new RuntimeException("L'Email est deja pris !");
        }
        if (!nouveauMembre.getEmail().contains("@")) {
            throw new RuntimeException("Veuillez entrer une adresse mail valide");
        }
        Membre m = new Membre();
        String motDePasseEnClair = this.Encrypted(mdp);
        m.setPassword(motDePasseEnClair);
        m.setNom(nouveauMembre.getNom().toUpperCase());
        m.setPrenom(nouveauMembre.getPrenom().toUpperCase());
        m.setEmail(nouveauMembre.getEmail());
        Membre saved = membreRepository.save(m);
        return GetMembreDTO(saved);
    }

    public Membre verifierConnexion(String email, String motDePasse) {
        Membre leMembre = membreRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        if (!leMembre.getPassword().equals(motDePasse)) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        return leMembre;
    }
    public String Encrypted(String Pwd){
        //String motDePasseCrypte = passwordEncoder.encode(motDePasseEnClair);
        return Pwd;
    }

    public MembreDTO SaveBD(Membre m){
        Membre saved = membreRepository.save(m);
        return GetMembreDTO(saved);
    }

    public MembreDTO GetMembreDTO(Membre m){
        MembreDTO membreReturn = new MembreDTO();
        membreReturn.setPrenom(m.getPrenom());
        membreReturn.setNom(m.getNom());
        membreReturn.setEmail(m.getEmail());
        membreReturn.setId(m.getId());
        return membreReturn;
    }
    public MembreDTO GetMembreDTO(Optional<Membre> membre)throws RuntimeException{
        if (!membre.isPresent()) {
            throw new RuntimeException("Le membre n'existe pas");
        }
        Membre m = membre.get();
        return GetMembreDTO(m);
    }
    public Membre GetMembre(Optional<Membre> membre)throws RuntimeException{
        if (!membre.isPresent()) {
            throw new RuntimeException("Le membre n'existe pas");
        }
        return membre.get();
    }
    @Transactional
    public void DelMembre(Membre m){
        List<Long> idGroupes = m.getGroupes().stream()
                .map(mg -> mg.getGroupe().getId())
                .collect(java.util.stream.Collectors.toList());
        membreRepository.delete(m);
        for (Long idGroupe : idGroupes) {
            long nbMembresRestants = membreGroupeRepository.countByGroupeId(idGroupe);
            if (nbMembresRestants == 0) {
                groupeRepository.findById(idGroupe).ifPresent(groupeRepository::delete);
            }
        }
    }

    public List<MembreDTO> getAllMembresByTache(Long idTache){
        Tache tache = tacheRepository.findById(idTache)
            .orElseThrow(() -> new RuntimeException("Tache not found"));
        List<Membre> membres = tache.getMembres();
        List<MembreDTO> membresDTO = new ArrayList<>();
        for (Membre membre : membres) {
            membresDTO.add(GetMembreDTO(membre));
        }
        return membresDTO;
    }

    @Transactional
    public void updatePwdByIdMembre(Long IdMembre, String Pwd){
        Membre membre = membreRepository.findById(IdMembre).orElseThrow(() -> new RuntimeException("Membre not found"));
        membre.setPassword(Encrypted(Pwd));
        membreRepository.save(membre);
    }


    //##### PARTIE TACHE ######

    @Transactional
    public List<MembreDTO> addMembreToTache(Long idMembre, Long idTache) {
        Membre membre = membreRepository.findById(idMembre)
            .orElseThrow(() -> new RuntimeException("Membre not found"));
        Tache tache = tacheRepository.findById(idTache)
            .orElseThrow(() -> new RuntimeException("Tache not found"));
        if (!membre.getTaches().contains(tache)) {
            membre.getTaches().add(tache);
            membreRepository.save(membre);
        }
        
        Long idGroupe = tache.getGroupe().getId();
        String frequenceRadio = "/topic/groupe/" + idGroupe;
        messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_TACHES");
        
        return getAllMembresByTache(idTache);
    }

    @Transactional
    public List<MembreDTO> deleteMembreFromTache(Long idMembre, Long idTache) {
        Membre membre = membreRepository.findById(idMembre)
            .orElseThrow(() -> new RuntimeException("Membre not found"));
        Tache tache = tacheRepository.findById(idTache)
            .orElseThrow(() -> new RuntimeException("Tache not found"));
        if (membre.getTaches().contains(tache)) {
            membre.getTaches().remove(tache);
            membreRepository.save(membre);
        }
        
        Long idGroupe = tache.getGroupe().getId();
        String frequenceRadio = "/topic/groupe/" + idGroupe;
        messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_TACHES");
        
        return getAllMembresByTache(idTache);
    }

    public TacheDTO updateTacheDTO(TacheDTO tacheDTO) {
        Tache tache = tacheRepository.findById(tacheDTO.getId())
                .orElseThrow(() -> new RuntimeException("Tache not found"));

        tache.setNom(tacheDTO.getNom());
        tache.setDescription(tacheDTO.getDescription());
        tache.setDateDebut(tacheDTO.getDateDebut());
        tache.setDateLimite(tacheDTO.getDateLimite());
        
        tacheRepository.save(tache);
        
        Long idGroupe = tache.getGroupe().getId();
        String frequenceRadio = "/topic/groupe/" + idGroupe;
        messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_TACHES");

        return this.convertTacheToDTO(tache);
    }
    @Transactional
    public void deleteTache(Long id) {
        Tache tache = tacheRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tache not found"));
        Long idGroupe = tache.getGroupe().getId();
        
        for (Membre membre : new ArrayList<>(tache.getMembres())) {
            membre.getTaches().remove(tache);
        }
        tache.getMembres().clear();
        tacheRepository.save(tache);
        tacheRepository.delete(tache);
        
        String frequenceRadio = "/topic/groupe/" + idGroupe;
        messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_TACHES");
    }

    public TacheDTO addTache(TacheDTO tacheDTO, Groupe groupe) {
        // Vérification d'unicité : même nom de tâche dans le même groupe
        if (tacheDTO.getNom() != null && tacheDTO.getNom().trim().length() > 0 && tacheDTO.getId() != null) {
            Tache tacheExistante = tacheRepository.findById(tacheDTO.getId()).orElse(null);
            if (tacheExistante != null && tacheExistante.getNom().equalsIgnoreCase(tacheDTO.getNom())) {
                throw new RuntimeException("Cette tâche existe déjà dans ce groupe");
            }
        }
        Tache tache = new Tache();
        tache.setNom(tacheDTO.getNom());
        tache.setGroupe(groupe);
        tache.setDescription(tacheDTO.getDescription());
        tache.setDateDebut(tacheDTO.getDateDebut());
        tache.setDateLimite(tacheDTO.getDateLimite());
        tache.setEtat(EtatTache.A_FAIRE);
        tacheRepository.save(tache);
        
        Long idGroupe = groupe.getId();
        String frequenceRadio = "/topic/groupe/" + idGroupe;
        messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_TACHES");

        return tacheDTO;
    }

    public Tache getTacheById(Long id) {
        return tacheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tache not found"));
    }

    public Tache updateTache(Tache tache) {
        Tache existingTache = tacheRepository.findById(tache.getId())
                .orElseThrow(() -> new RuntimeException("Tache not found"));
        existingTache.setNom(tache.getNom());
        existingTache.setDescription(tache.getDescription());
        existingTache.setDateDebut(tache.getDateDebut());
        existingTache.setDateLimite(tache.getDateLimite());
        Tache savedTache = tacheRepository.save(existingTache);
        
        Long idGroupe = savedTache.getGroupe().getId();
        String frequenceRadio = "/topic/groupe/" + idGroupe;
        messagingTemplate.convertAndSend(frequenceRadio, "REFRESH_TACHES");
        
        return savedTache;
    }
    public List<TacheDTO> getTacheDTO(Membre membre) {
        List<Tache> taches = membre.getTaches();
        List<TacheDTO> tachesDTO = new ArrayList<>();
        for (Tache tache : taches) {
            TacheDTO tacheDTO = new TacheDTO();
            tacheDTO.setId(tache.getId());
            tacheDTO.setNom(tache.getNom());
            tacheDTO.setDescription(tache.getDescription());
            tacheDTO.setDateDebut(tache.getDateDebut());
            tacheDTO.setDateLimite(tache.getDateLimite());
            tachesDTO.add(tacheDTO);
        }
        return tachesDTO;
    }
    public TacheDTO convertTacheToDTO(Tache tache) {
        TacheDTO dto = new TacheDTO();
        dto.setId(tache.getId());
        dto.setNom(tache.getNom());
        dto.setDescription(tache.getDescription());
        dto.setDateDebut(tache.getDateDebut());
        dto.setDateLimite(tache.getDateLimite());
        return dto;
    }
}
