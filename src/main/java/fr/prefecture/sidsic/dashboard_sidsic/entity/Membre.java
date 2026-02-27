package fr.prefecture.sidsic.dashboard_sidsic.entity;

import java.util.List;

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

    private String email;
    private String password;
    @ManyToMany
    @JoinTable(
        name = "Tache2Membre",
        joinColumns = @JoinColumn(name = "IDmembre"),
        inverseJoinColumns = @JoinColumn(name = "IDtache")
    )
    private List<Tache> taches;

    @OneToMany(mappedBy = "membre")
    private List<Note> notes;

    private String Nom;
    private String Prenom;

    @ManyToOne
    @JoinColumn(name = "CurrentGroupe")
    private Groupe Current_groupe;

    @ManyToMany(mappedBy = "membres")
    private List<Groupe> groupes;

    @OneToMany(mappedBy = "admin")
    private List<Groupe> groupes_admin;
}
