package fr.prefecture.sidsic.dashboard_sidsic.repository;

import fr.prefecture.sidsic.dashboard_sidsic.entity.Tache;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TacheRepository extends JpaRepository<Tache, Long> {
	@Modifying
	@Query(value = "DELETE FROM tache2membre WHERE idtache IN (SELECT id FROM tache WHERE IDgroupe = :idGroupe)", nativeQuery = true)
	void deleteTaskMemberLinksByGroupeId(@Param("idGroupe") Long idGroupe);
}