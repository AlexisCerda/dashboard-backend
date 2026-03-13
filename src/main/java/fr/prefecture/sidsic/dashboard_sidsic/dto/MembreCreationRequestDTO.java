package fr.prefecture.sidsic.dashboard_sidsic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembreCreationRequestDTO {
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
}
