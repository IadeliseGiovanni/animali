package it.adozioni.animali.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="animale", schema="public")
public class Animale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String specie;
    private String razza;
    private int eta;
    private String genere;

    // Nome allineato al DB: 'adottato'
    private boolean adottato;

    private String microchip;

    @OneToMany(mappedBy = "animale")
    private List<VisitaMedica> visiteMediche;

    @ManyToOne
    @JoinColumn(name="adottante_id", nullable = true) // Permette ID nullo
    private Adottante adottante;

    @ManyToOne
    @JoinColumn(name = "centri_adozione_id", nullable = true) // Fondamentale: cambiato in TRUE
    private CentroAdozione centroAdozione;
}