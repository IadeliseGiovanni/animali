package it.adozioni.animali.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolontarioDto {

    private Integer id;
    private String nome;
    private String cognome;
    private String cf;
    private String turno;
}
