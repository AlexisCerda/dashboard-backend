package fr.prefecture.sidsic.dashboard_sidsic.dto;

import java.time.LocalDate;

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
}
