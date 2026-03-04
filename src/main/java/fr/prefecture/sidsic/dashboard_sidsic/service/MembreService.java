package fr.prefecture.sidsic.dashboard_sidsic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.prefecture.sidsic.dashboard_sidsic.dto.MembreDTO;
import fr.prefecture.sidsic.dashboard_sidsic.dto.TacheDTO;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Groupe;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Membre;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Tache;
import fr.prefecture.sidsic.dashboard_sidsic.repository.MembreRepository;
import fr.prefecture.sidsic.dashboard_sidsic.repository.TacheRepository;
import jakarta.transaction.Transactional;

@Service
public class MembreService {
    private final MembreRepository  membreRepository;
    private final TacheRepository tacheRepository;
    //private final BCryptPasswordEncoder passwordEncoder;

    public MembreService(MembreRepository membreRepository, TacheRepository tacheRepository) {
        this.membreRepository = membreRepository;
        this.tacheRepository = tacheRepository;
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
        membreRepository.delete(m);
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
        return getAllMembresByTache(idTache);
    }

    public Tache updateTacheDTO(TacheDTO tacheDTO) {
        Tache tache = tacheRepository.findById(tacheDTO.getId())
                .orElseThrow(() -> new RuntimeException("Tache not found"));

        tache.setNom(tacheDTO.getNom());
        tache.setDescription(tacheDTO.getDescription());
        tache.setDateDebut(tacheDTO.getDateDebut());
        tache.setDateLimite(tacheDTO.getDateLimite());

        return tacheRepository.save(tache);
    }
    @Transactional
    public void deleteTache(Long id) {
        Tache tache = tacheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tache not found"));
        tacheRepository.delete(tache);
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
        tacheRepository.save(tache);

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
        return tacheRepository.save(existingTache);
    }
}
