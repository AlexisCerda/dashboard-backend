package fr.prefecture.sidsic.dashboard_sidsic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigurationDTO {
    private Long id;
    private Long idMembre;
    private Long idGroupe;
    private String nom;
    private String taches;
    private String notes;
    private String achats;
    private String prets;
    private String mouvements;
}
