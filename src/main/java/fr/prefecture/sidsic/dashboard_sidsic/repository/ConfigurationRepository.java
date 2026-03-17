package fr.prefecture.sidsic.dashboard_sidsic.repository;

import fr.prefecture.sidsic.dashboard_sidsic.entity.Configuration;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
	List<Configuration> findByGroupeId(Long idGroupe);

	List<Configuration> findByMembreId(Long idMembre);

	List<Configuration> findByMembreIdAndGroupeId(Long idMembre, Long idGroupe);
}