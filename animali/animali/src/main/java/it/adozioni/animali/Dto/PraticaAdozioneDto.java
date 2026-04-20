package it.adozioni.animali.Dto;

import it.adozioni.animali.Model.StatoPratica;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PraticaAdozioneDto {
    private Integer id;
    private Integer adottanteId;
    private String adottanteNominativo; // Nome + Cognome uniti
    private Integer animaleId;
    private String animaleNome;
    private StatoPratica stato;
    private LocalDateTime dataApertura;
    private String noteAdmin;
}