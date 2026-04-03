package it.adozioni.animali.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Volontario", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Volontario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String cognome;
    private String cf;      // codice fiscale
    private String turno;   // turno assegnato

    @Column(unique = true, nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "centro_id", nullable = true)
    private CentroAdozione centroAdozione;


    @Column(nullable = false)
    private String password;

    private String ruolo;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
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
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
