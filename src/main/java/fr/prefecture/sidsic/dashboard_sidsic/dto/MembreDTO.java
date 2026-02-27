package fr.prefecture.sidsic.dashboard_sidsic.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MembreDTO {
    private Integer id;
    private String nom;
    // Ajoute d'autres infos si React en a besoin (ex: le rôle, l'email...)
    
}