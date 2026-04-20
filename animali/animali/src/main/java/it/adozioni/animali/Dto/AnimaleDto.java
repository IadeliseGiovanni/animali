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
    private Integer id;
    private String nome;
    private String specie;
    private String razza;
    private int eta;
    private String genere;

    // Cambiato da isAdottato a adottato per coerenza totale
    private boolean adottato;

    private List<VisitaMedica> visiteMediche;
    private Adottante adottante;
    private CentroAdozione centroAdozione;
    private String microchip;

    private String descrizione;
    private String videoUrl;
    private String fotoUrl;
    //
}