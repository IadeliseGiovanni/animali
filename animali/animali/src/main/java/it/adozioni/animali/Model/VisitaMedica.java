package it.adozioni.animali.Model;

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
@Table(name = "VisitaMedica", schema = "Animali")
public class VisitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String nome;

    private LocalDateTime data;

    private String esito;

    private String veterinario;

    @ManyToOne
    @JoinColumn(name = "animale_id", nullable = true)
    private Animale animale;

}
