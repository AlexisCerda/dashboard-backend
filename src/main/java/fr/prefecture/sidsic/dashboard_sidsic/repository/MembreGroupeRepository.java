package fr.prefecture.sidsic.dashboard_sidsic.repository;
import fr.prefecture.sidsic.dashboard_sidsic.entity.MembreGroupe;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembreGroupeRepository extends JpaRepository<MembreGroupe, Long> {
    List<MembreGroupe> findByIsAdmin();
}