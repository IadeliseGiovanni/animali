package it.adozioni.animali.Model;

import it.adozioni.animali.Model.Adottante;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = Adottante.class, fetch = FetchType.EAGER)
    private Adottante user;

    private LocalDateTime expiryDate;
}