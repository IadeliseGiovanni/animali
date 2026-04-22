package it.adozioni.animali.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.adozioni.animali.Model.Animale;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "visita_medica", schema = "public")
public class VisitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime data;

    private String esito;

    private String veterinario;

    private String note;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "animale_id", nullable = true)
    @JsonIgnore
    private Animale animale;//

}
