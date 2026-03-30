package it.adozioni.animali.Repository;

import it.adozioni.animali.Model.Animale;
import it.adozioni.animali.Model.VisitaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimaleRepository extends JpaRepository<AnimaleRepository,Long> {
    Animale findByNome(String nome);
    List<Animale>findByRazza(String razza);
    List<Animale>findByGenere(String genere);
    List<Animale>findBySpecie(String specie);
    List<Animale>findByRazzaAndSpecie(String razza, String genere);
}
