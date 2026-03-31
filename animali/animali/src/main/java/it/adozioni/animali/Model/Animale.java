package it.adozioni.animali.Model;

import it.adozioni.animali.Model.VisitaMedica;
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
@Table(name="animale",schema="public")
public class Animale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String specie;
    private String razza;
    private int eta;
    private String genere;
    boolean adottato;
    private String microchip ;

    //relazione 1-n con VisitaMedica
    @OneToMany(mappedBy = "animale")
    List<VisitaMedica> visiteMediche;
    @ManyToOne
    @JoinColumn(name="adottante_id", nullable = true)
    private Adottante adottante;
    @ManyToOne
    @JoinColumn(name = "centri_adozione_id")
    private CentroAdozione centroAdozione;



}
