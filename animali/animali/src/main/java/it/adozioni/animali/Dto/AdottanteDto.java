package it.adozioni.animali.Dto;

import it.adozioni.animali.Model.Animale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdottanteDto {

    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    private Boolean isSchedato;
    private List<Animale> animaliAdottati;

}