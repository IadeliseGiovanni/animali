package it.adozioni.animali.Model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "adottante", schema = "public") // ricordati di collegare il DB
public class Adottante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String cognome;

    private String email;

    private String telefono;

    private Boolean isSchedato;

    @OneToMany(mappedBy = "adottante")
    private List<Animale> animaliAdottati;
}