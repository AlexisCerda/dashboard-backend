package fr.prefecture.sidsic.dashboard_sidsic.entity;

import java.time.LocalDate;

import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatPret;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Pret {
    public Pret(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    @Version
    private int version;

    @ManyToOne
    @JoinColumn(name="IDmateriel")
    private Materiel materiel;

    @ManyToOne
    @JoinColumn(name="IDgroupe")
    private Groupe groupe;

    private String NomPersonne;
    private String PrenomPersonne;
    private int quantite;

    @Enumerated(EnumType.STRING)
    private EtatPret etat;

    private LocalDate dateDebut;
    private LocalDate dateFin;



}
