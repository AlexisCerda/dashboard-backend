
package fr.prefecture.sidsic.dashboard_sidsic.entity;

import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Materiel {
    public Materiel(){}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Version
    private int version;
    private String nom;
    private String marque;

    @OneToMany(mappedBy = "materiel")
    ArrayList<Achat> achats;

    @OneToMany(mappedBy = "materiel")
    ArrayList<Pret> prets;
    
}
