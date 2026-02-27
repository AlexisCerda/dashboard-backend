package fr.prefecture.sidsic.dashboard_sidsic.entity;
import java.util.List;

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
    private List<Membre> membres;

    @OneToMany(mappedBy = "Current_groupe")
    private List<Membre> membres_current;

    @OneToMany(mappedBy = "groupe")
    private List<Achat> achats;

    @OneToMany(mappedBy = "groupe")
    private List<Pret> prets;

    @OneToMany(mappedBy = "groupe")
    private List<Mouvement> mouvements;

    @OneToMany(mappedBy = "groupe")
    private List<Tache> taches;
}
