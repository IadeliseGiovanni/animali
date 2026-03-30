package it.adozioni.animali.dto;

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

    private String nomeCentro;

    private String indirizzo;

    private String citta;

    private Integer capacitaMassima;

    private Boolean isNoProfit;


}