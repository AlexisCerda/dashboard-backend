package fr.prefecture.sidsic.dashboard_sidsic.entity;

import java.time.LocalDate;

import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatMouvement;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Mouvement {
    public Mouvement(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private int version;


    @Enumerated(EnumType.STRING)
    private EtatMouvement etat;

    @ManyToOne
    @JoinColumn(name = "IDgroupe")
    private Groupe groupe;

    private String nom;
    private String prenom;
    private LocalDate dateArrivee;
    private LocalDate dateDepart;

    @Column(length = 50)
    private String service; // ex: BFLI, BCL

    @Column(length = 50)
    private String statut; // ex: Contractuelle, Stagiaire

    @Column(length = 500)
    private String urlTicketGlpi; // Le lien vers le ticket

}

