package it.adozioni.animali.Dto;

import it.adozioni.animali.Model.Animale;
import it.adozioni.animali.Model.Volontario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CentroAdozioneDto {

    private Integer id;

    private String nomeCentro;

    private String indirizzo;

    private String citta;

    private Integer capacitaMassima;

    private Boolean isNoProfit;

    private List<Animale> animaliOspitati;

    private List<Volontario> volontari;

    private Double latitudine;
    private Double longitudine;
    //
}