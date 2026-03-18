package fr.prefecture.sidsic.dashboard_sidsic.dto;

import java.time.LocalDate;

import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatTache;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TacheDTO {
    private Long id;
    private String nom;
    private String Description;
    private LocalDate dateDebut;
    private LocalDate dateLimite;
    private EtatTache etat;
}
