package it.adozioni.animali.Repository;

import it.adozioni.animali.Model.Adottante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdottanteRepository extends JpaRepository<Adottante, Integer> {

    List<Adottante> findByNome(String nome);

    List<Adottante> findByCognome(String cognome);

    Adottante findByEmail(String email);

    List<Adottante> findByIsSchedato(Boolean isSchedato);

    List<Adottante> findByNomeAndCognome(String nome, String cognome);

}

//commento inutile