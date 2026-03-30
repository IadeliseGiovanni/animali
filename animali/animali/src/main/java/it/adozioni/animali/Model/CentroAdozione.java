package it.adozioni.animali.Model;

import it.adozioni.animali.Model.Animale;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "centri_adozione", schema = "public")
@Getter
@Setter
@NoArgsConstructor // Fondamentale per JPA
public class CentroAdozione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_centro", nullable = false, length = 100)
    private String nomeCentro;

    private String indirizzo;
    private String citta;

    @Column(name = "capacita_massima")
    private Integer capacitaMassima;

    @Column(name = "is_no_profit")
    private Boolean isNoProfit;

    // --- RELAZIONE 1 A N CON GLI ANIMALI ---
    // Il "mappedBy" deve corrispondere al nome del campo "centro" nella classe Animale
    @OneToMany(mappedBy = "centro", cascade = CascadeType.ALL)
    private List<Animale> animaliOspitati;
}