package fr.prefecture.sidsic.dashboard_sidsic.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatTache;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tache {
    public Tache(){}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    @ManyToOne
    @JoinColumn(name="IDgroupe")
    private Groupe groupe;

    private String nom;
    private String Description;
    private LocalDate dateDebut;
    private LocalDate dateLimite;

    @Enumerated(EnumType.STRING)
    private EtatTache etat;

    @ManyToMany(mappedBy = "taches")
    private List<Membre> membres = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mouvement_id", nullable = true)
    private Mouvement mouvement;

}
