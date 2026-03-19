package fr.prefecture.sidsic.dashboard_sidsic.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAlias;

import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatTache;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TacheDTO {
    @JsonAlias("idTache")
    private Long id;
    private String nom;
    @JsonAlias("Description")
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateLimite;
    private EtatTache etat;
}
