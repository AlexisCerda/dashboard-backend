package fr.prefecture.sidsic.dashboard_sidsic.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

    public List<Membre> recupererToutLesMembres() {
        return membreRepository.findAll();
    }

    public Membre ajouterMembre(Membre nouveauMembre) {
        nouveauMembre.setNom(nouveauMembre.getNom().toUpperCase());
        nouveauMembre.setPrenom(nouveauMembre.getPrenom().toUpperCase());
        return membreRepository.save(nouveauMembre);
    }

    public Membre creerUnNouveauMembre(Membre nouveauMembre, String motDePasseEnClair) {
        //String motDePasseCrypte = passwordEncoder.encode(motDePasseEnClair);
        //nouveauMembre.setPassword(motDePasseCrypte);
        nouveauMembre.setPassword(motDePasseEnClair);
        return membreRepository.save(nouveauMembre);
    }
}
