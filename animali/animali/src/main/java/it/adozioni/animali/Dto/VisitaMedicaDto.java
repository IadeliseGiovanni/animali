package it.adozioni.animali.Dto;

import it.adozioni.animali.Model.Animale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VisitaMedicaDto {

    private Integer id;

    private LocalDateTime data;

    private String esito;

    private String veterinario;

    private Animale animale;
}
