package it.adozioni.animali.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Volontario", schema = "Animali")
@Getter
@Setter
@NoArgsConstructor
public class Volontario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognome;
    private String cf;      // codice fiscale
    private String turno;   // turno assegnato
}
