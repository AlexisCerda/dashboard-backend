package fr.prefecture.sidsic.dashboard_sidsic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    @Version
    private int version;

    @ManyToOne
    @JoinColumn(name = "IDmembre")
    private Membre membre;

    private String description;
}
