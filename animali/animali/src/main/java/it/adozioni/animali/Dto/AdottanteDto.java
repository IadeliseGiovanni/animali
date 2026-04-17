package it.adozioni.animali.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.adozioni.animali.Model.Animale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdottanteDto {

    private Integer id;
    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    private Boolean isSchedato;
    private List<Animale> animaliAdottati;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // permette di ricevere password durante la registrazione
    private String password;
//
}