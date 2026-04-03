package it.adozioni.animali.Model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "adottante", schema = "public")
public class Adottante implements UserDetails { //implementando dice usa questa classe per gestire il login

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String cognome;

    // AGGIUNTO: unique = true per impedire i duplicati che bloccavano il login
    // UNIQUE è importante perchè impedisce a due persone di registrare con lo stesso indirizzo
    @Column(unique = true, nullable = false)
    private String email;

    private String telefono;
    private Boolean isSchedato;

    @Column(nullable = false)
    private String password;

    private String ruolo; // Esempio nel DB: "USER"

    @OneToMany(mappedBy = "adottante")
    private List<Animale> animaliAdottati;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // trasforma stringa come USER a ROLE_USER
        if (this.ruolo == null || this.ruolo.isEmpty()) {
            return List.of();
        }
        // Converte "USER" in "ROLE_USER" per Spring Security
        String r = this.ruolo.toUpperCase();
        if (!r.startsWith("ROLE_")) r = "ROLE_" + r;
        return List.of(new SimpleGrantedAuthority(r));
    }

    @Override
    public String getUsername() { return this.email; }

    @Override
    public String getPassword() { return this.password; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return isSchedato == null || !isSchedato; } // permette gli admin di bloccare un utente se necessario

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}