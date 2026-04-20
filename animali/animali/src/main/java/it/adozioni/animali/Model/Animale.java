package it.adozioni.animali.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
//
    @Column(name = "genere")
    private String genere;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    // Nome allineato al DB: 'adottato'
    private boolean adottato;

    private String microchip;


    @Column(name = "video_url")
    private String videoUrl;

    // Se non usi Lombok, aggiungi anche Getter e Setter per videoUrl
    @OneToMany(mappedBy = "animale")
    private List<VisitaMedica> visiteMediche;

    @ManyToOne
    @JoinColumn(name="adottante_id", nullable = true) // Permette ID nullo
    @JsonIgnore
    private Adottante adottante;

    @ManyToOne
    @JoinColumn(name = "centri_adozione_id", nullable = true) // Fondamentale: cambiato in TRUE
    @JsonIgnore
    private CentroAdozione centroAdozione;

    @Column(name = "foto_url")
    private String fotoUrl;
}