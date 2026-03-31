package it.adozioni.animali.Dto;

import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Model.CentroAdozione;
import it.adozioni.animali.Model.VisitaMedica;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimaleDto {
    private Long id;
    private String nome;
    private String specie;
    private String razza;
    private int eta;
    boolean isAdottato;
    private List<VisitaMedica> visiteMediche;
    private Adottante adottante;
    private CentroAdozione centroAdozione;


}
