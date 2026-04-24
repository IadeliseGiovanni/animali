package it.adozioni.animali.Model;

import it.adozioni.animali.Model.Adottante;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class EmailConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token; // UUID unico

    @Column(nullable = false)
    private String nuovaEmail;

    @OneToOne
    @JoinColumn(name = "adottante_id", referencedColumnName = "id")
    private Adottante adottante;

    private LocalDateTime dataScadenza;

    public EmailConfirmationToken() {}

    public EmailConfirmationToken(Adottante adottante, String nuovaEmail) {
        this.adottante = adottante;
        this.nuovaEmail = nuovaEmail;
        this.token = UUID.randomUUID().toString();
        this.dataScadenza = LocalDateTime.now().plusHours(24); // Scade dopo 24h
    }
}