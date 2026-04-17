package it.adozioni.animali.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.adozioni.animali.Model.CentroAdozione;
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
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
//
    private CentroAdozione centroAdozione;
}
