package it.adozioni.animali.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pratica_adozione", schema = "public")
public class PraticaAdozione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "adottante_id", nullable = false)
    private Adottante adottante;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "animale_id", nullable = false)
    private Animale animale;

    @Enumerated(EnumType.STRING)
    private StatoPratica stato = StatoPratica.PENDING;

    private LocalDateTime dataApertura = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String noteAdmin; // Per commenti dell'admin durante la valutazione
}