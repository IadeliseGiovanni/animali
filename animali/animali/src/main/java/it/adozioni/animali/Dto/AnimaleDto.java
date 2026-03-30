package it.adozioni.animali.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimaleDto {
    private Long id;
    private String nome;
    private String specie;
    private String razza;
    private int eta;
    boolean isAdottato;


}
