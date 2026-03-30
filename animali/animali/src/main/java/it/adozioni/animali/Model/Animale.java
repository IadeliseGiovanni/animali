package it.adozioni.animali.Model;

import it.adozioni.animali.Model.VisitaMedica;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name="Animale",schema="public")
public class Animale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String specie;
    private String razza;
    private int eta;
    private String genere;
    boolean isAdottato;

    //relazione 1-n con VisitaMedica
    @OneToMany(mappedBy = "Animale")
    List<VisitaMedica> visiteMediche;
}
