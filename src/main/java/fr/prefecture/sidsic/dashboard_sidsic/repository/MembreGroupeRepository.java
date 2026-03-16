package fr.prefecture.sidsic.dashboard_sidsic.repository;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Groupe;
import fr.prefecture.sidsic.dashboard_sidsic.entity.Membre;
import fr.prefecture.sidsic.dashboard_sidsic.entity.MembreGroupe;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembreGroupeRepository extends JpaRepository<MembreGroupe, Long> {
    List<MembreGroupe> findByGroupeId(Long idGroupe);
    List<MembreGroupe> findByMembreId(Long idMembre);
    long countByGroupeId(Long idGroupe);
    void deleteByMembreAndGroupe(Membre membre, Groupe groupe);
    Optional<MembreGroupe> findByMembreAndGroupe(Membre membre, Groupe groupe);
}