package it.adozioni.animali.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdozioneRequestDto {
    private String nome;
    private String cognome;
    private String residenza;
    private Integer idAnimale; // Fondamentale per collegare l'animale del DB
}

// Questo è l'oggetto che riceve i dati dell'adottante