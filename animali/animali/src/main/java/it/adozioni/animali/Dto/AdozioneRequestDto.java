package it.adozioni.animali.Dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdozioneRequestDto {
    private String nome;
    private String cognome;
    private String residenza;
    private Integer idAnimale; // Fondamentale per collegare l'animale del DB
    private Integer idAdottante;
}

// Questo è l'oggetto che riceve i dati dell'adottante