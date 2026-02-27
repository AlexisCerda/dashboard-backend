package fr.prefecture.sidsic.dashboard_sidsic.repository;

import fr.prefecture.sidsic.dashboard_sidsic.entity.Groupe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Integer> {}