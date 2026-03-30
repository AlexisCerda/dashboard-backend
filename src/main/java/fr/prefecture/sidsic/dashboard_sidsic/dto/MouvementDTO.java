package fr.prefecture.sidsic.dashboard_sidsic.dto;

import java.time.LocalDate;

import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatMouvement;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MouvementDTO {
    private Long id;
    private String nom;
    private String prenom;
    private LocalDate dateArrivee;
    private LocalDate dateDepart;
    private EtatMouvement etat;

    private String service;
    private String statut;
    private String urlTicketGlpi;
}
