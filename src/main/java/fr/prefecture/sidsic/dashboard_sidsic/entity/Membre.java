package fr.prefecture.sidsic.dashboard_sidsic.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Membre implements UserDetails {
    
    public Membre(){}
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    private String email;
    
    private String password;

    private LocalDate LastConnection;

    @ManyToMany
    @JoinTable(
        name = "Tache2Membre",
        joinColumns = @JoinColumn(name = "IDmembre"),
        inverseJoinColumns = @JoinColumn(name = "IDtache")
    )
    private List<Tache> taches;

    @OneToMany(mappedBy = "membre",cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Note> notes;
    @OneToMany(mappedBy = "membre",cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Image> images;

    @OneToMany(mappedBy = "membre",cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Configuration> configurations;

    public List<Note> getNotes() {
        if (notes == null) {
            return new ArrayList<>();
        }
        return notes;
    }

    private String Nom;
    private String Prenom;

    @ManyToOne
    @JoinColumn(name = "CurrentGroupe")
    private Groupe Current_groupe;

    @OneToMany(mappedBy = "membre",cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MembreGroupe> groupes;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}