package fr.prefecture.sidsic.dashboard_sidsic.entity;
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
    private int ID;
    @Version
    private int version;

    private String nom;
    private String ville;
    
    @ManyToOne
    @JoinColumn(name = "IDadmin")
    private Membre admin;

}
