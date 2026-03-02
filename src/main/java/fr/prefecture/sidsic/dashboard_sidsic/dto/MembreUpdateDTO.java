package fr.prefecture.sidsic.dashboard_sidsic.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MembreUpdateDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String pwd;
}