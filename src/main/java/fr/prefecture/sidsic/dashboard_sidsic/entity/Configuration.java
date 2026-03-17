package fr.prefecture.sidsic.dashboard_sidsic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    @ManyToOne
    @JoinColumn(name = "IDmembre")
    private Membre membre;

    @ManyToOne
    @JoinColumn(name = "IDgroupe")
    private Groupe groupe;

    private String Nom;

    private String Taches;
    private String Notes;
    private String Achats;
    private String Prets;
    private String Mouvements;
}
