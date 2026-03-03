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
    private Long ID;
    @Version
    private int version;

    private int quantite;
    private String NomPersonne;
    private String PrenomPersonne;

    @Enumerated(EnumType.STRING)
    private EtatAchat etat_courant;

    private String NomMateriel;
    private String MarqueMateriel;
    private String Reference;

    @ManyToOne
    @JoinColumn(name="IDgroupe")
    private Groupe groupe;

}
