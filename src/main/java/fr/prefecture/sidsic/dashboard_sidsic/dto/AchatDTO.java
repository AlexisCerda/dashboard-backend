package fr.prefecture.sidsic.dashboard_sidsic.dto;

import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatAchat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AchatDTO {
    private Long id;
    private String nomMateriel;
    private String marqueMateriel;
    private String reference;
    private String nomPersonne;
    private String prenomPersonne;
    private int quantite;
    private EtatAchat etat;
}
