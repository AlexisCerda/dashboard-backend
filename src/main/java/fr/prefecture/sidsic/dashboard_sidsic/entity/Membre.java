package fr.prefecture.sidsic.dashboard_sidsic.entity;

import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Membre {
    public Membre(){}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private int version;

    private String password;
    @ManyToMany
    @JoinTable(
        name = "Tache2Membre",
        joinColumns = @JoinColumn(name = "IDmembre"),
        inverseJoinColumns = @JoinColumn(name = "IDtache")
    )
    private ArrayList<Tache> taches;

    @OneToMany(mappedBy = "membre")
    private ArrayList<Note> notes;

    private String Nom;
    private String Prenom;

    @ManyToOne
    @JoinColumn(name = "CurrentGroupe")
    private Groupe Current_groupe;

    @ManyToMany(mappedBy = "membres")
    private ArrayList<Groupe> groupes;

    @OneToMany(mappedBy = "admin")
    private ArrayList<Groupe> groupes_admin;
}
