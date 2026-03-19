package fr.prefecture.sidsic.dashboard_sidsic.dto;

import java.time.LocalDate;

import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatPret;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PretDTO {
    private Long id;
    private String nomMateriel;
    private String marqueMateriel;
    private String nomPersonne;
    private String prenomPersonne;
    private int quantite;
    private EtatPret etat;
    private LocalDate dateDebut;
    private LocalDate dateFin;
}
