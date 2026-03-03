package fr.prefecture.sidsic.dashboard_sidsic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MembreGroupe {
    public MembreGroupe(){}

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

    private int IsAdmin;

}
