package fr.prefecture.sidsic.dashboard_sidsic.entity;

import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatAchat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Achat {
    public Achat(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;
    @Version
    private int version;

    private int quantite;
    private String NomPersonne;
    private String PrenomPersonne;

    @Enumerated(EnumType.STRING)
    private EtatAchat etat_courant;

    @ManyToOne
    @JoinColumn(name="IDmateriel")
    private Materiel materiel;

    @ManyToOne
    @JoinColumn(name="IDgroupe")
    private Groupe groupe;

}
