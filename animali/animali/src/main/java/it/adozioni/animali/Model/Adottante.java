package it.adozioni.animali.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "adottante", schema = "public") // ricordati di collegare il DB
public class Adottante implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private String cognome;

    private String email;

    private String telefono;

    private Boolean isSchedato;

    // --- NUOVI CAMPI PER SECURITY ---
    private String password;

    private String ruolo;

    @OneToMany(mappedBy = "adottante")
    private List<Animale> animaliAdottati;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Trasforma la stringa 'ruolo' in un'autorità riconosciuta da Spring
        return List.of(new SimpleGrantedAuthority(ruolo));
    }

    // --- METODI INTERFACCIA USERDETAILS ---

    @Override
    public String getUsername() {
        // Spring Security usa questo metodo per identificare l'utente (nel tuo caso l'email)
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // L'account non scade mai
    }

    @Override
    public boolean isAccountNonLocked() {
        // Se 'isSchedato' significa che l'utente è bloccato, puoi mettere: !isSchedato
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Le credenziali non scadono mai
    }

    @Override
    public boolean isEnabled() {
        return true; // L'account è attivo
    }
}