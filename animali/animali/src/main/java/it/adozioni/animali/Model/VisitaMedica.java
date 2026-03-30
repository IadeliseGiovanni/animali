package it.adozioni.animali.Model;

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
@Table(name = "VisitaMedica", schema = "public")
public class VisitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime data;

    private String esito;

    private String veterinario;

    @ManyToOne
    @JoinColumn(name = "animale_id", nullable = true)
    private Animale animale;

}
