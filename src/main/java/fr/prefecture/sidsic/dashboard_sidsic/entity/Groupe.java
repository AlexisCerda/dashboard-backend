package fr.prefecture.sidsic.dashboard_sidsic.entity;
import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Groupe {
    public Groupe(){}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;
    @Version
    private int version;

    private String nom;
    private String ville;
    
    @ManyToOne
    @JoinColumn(name = "IDadmin")
    private Membre admin;

    @ManyToMany
    @JoinTable(
        name = "Groupe2Membre",
        joinColumns = @JoinColumn(name = "IDgroupe"),
        inverseJoinColumns = @JoinColumn(name = "IDmembre")
    )
    private ArrayList<Membre> membres;

    @OneToMany(mappedBy = "Current_groupe")
    private ArrayList<Membre> membres_current;

    @OneToMany(mappedBy = "groupe")
    private ArrayList<Achat> achats;

    @OneToMany(mappedBy = "groupe")
    private ArrayList<Pret> prets;

    @OneToMany(mappedBy = "groupe")
    private ArrayList<Mouvement> mouvements;

    @OneToMany(mappedBy = "groupe")
    private ArrayList<Tache> taches;
}
