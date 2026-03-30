package it.adozioni.animali.Dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class VolontarioDto {

    private Long id;
    private String nome;
    private String cognome;
    private String cf;
    private String turno;
}
