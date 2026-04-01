package it.adozioni.animali.Repository;

import it.adozioni.animali.Model.Adottante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdottanteRepository extends JpaRepository<Adottante, Integer> {

    // Utilizzato dal Service per il caricamento dell'utente durante il login
    Optional<Adottante> findByEmail(String email);

    List<Adottante> findByCognome(String cognome);
}