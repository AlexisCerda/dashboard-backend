package fr.prefecture.sidsic.dashboard_sidsic.repository;

import fr.prefecture.sidsic.dashboard_sidsic.entity.Achat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchatRepository extends JpaRepository<Achat, Integer> {}