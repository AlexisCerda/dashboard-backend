package fr.prefecture.sidsic.dashboard_sidsic.repository;

import fr.prefecture.sidsic.dashboard_sidsic.entity.Image;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findByMembreId(Long idMembre);
}