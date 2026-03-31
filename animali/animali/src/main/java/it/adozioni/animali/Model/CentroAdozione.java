package it.adozioni.animali.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "centri_adozione", schema = "public")
public class CentroAdozione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nomeCentro;

    private String indirizzo;

    private String citta;

    private Integer capacitaMassima;

    private Boolean isNoProfit;

    @OneToMany(mappedBy = "centro", cascade = CascadeType.ALL)
    private List<Animale> animaliOspitati;

    @OneToMany(mappedBy = "centro", cascade = CascadeType.ALL)
    private List<Volontario> volontari;
}