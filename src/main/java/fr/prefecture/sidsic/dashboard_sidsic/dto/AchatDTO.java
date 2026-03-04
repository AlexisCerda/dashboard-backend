package fr.prefecture.sidsic.dashboard_sidsic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AchatDTO {
    private Long id;
    private String nomMateriel;
    private String marqueMateriel;
    private String nomPersonne;
    private String prenomPersonne;
    private int quantite;
}
