package fr.prefecture.sidsic.dashboard_sidsic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.prefecture.sidsic.dashboard_sidsic.dto.MembreDTO;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Membre;
import fr.prefecture.sidsic.dashboard_sidsic.repository.MembreRepository;

@Service
public class MembreService {
    private final MembreRepository  membreRepository;
    //private final BCryptPasswordEncoder passwordEncoder;

    public MembreService(MembreRepository membreRepository) {
        this.membreRepository = membreRepository;
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
        return membreRepository.findById(id);
    }
    public Optional<Membre> getMembreByEmail(String mail){
        return membreRepository.findByEmail(mail);
    }

    public Membre creerUnNouveauMembre(MembreDTO nouveauMembre, String mdp)throws RuntimeException {
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
        return membreRepository.save(m);
    }

    public Membre verifierConnexion(String email, String motDePasse) {
        Membre leMembre = membreRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // Plus tard, avec BCrypt, on utilisera : passwordEncoder.matches(motDePasse, leMembre.getPassword())
        if (!leMembre.getPassword().equals(motDePasse)) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        return leMembre;
    }
    public String Encrypted(String Pwd){
        //String motDePasseCrypte = passwordEncoder.encode(motDePasseEnClair);
        return Pwd;
    }

    public Membre SaveBD(Membre m){
        return membreRepository.save(m);
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

    public void DelMembre(Membre m){
        membreRepository.delete(m);
    }
}
