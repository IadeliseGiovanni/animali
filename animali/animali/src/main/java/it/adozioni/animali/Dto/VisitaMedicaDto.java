package it.adozioni.animali.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime data;

    private String esito;

    private String veterinario;

    private Animale animale;

    private String note;
}//
