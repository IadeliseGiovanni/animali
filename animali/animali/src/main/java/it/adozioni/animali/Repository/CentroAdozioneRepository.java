package it.adozioni.animali.Repository;

import it.adozioni.animali.model.CentroAdozione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroAdozioneRepository extends JpaRepository<CentroAdozione, Long> {
    // Qui non devi scrivere nulla, fa tutto Spring Boot!
}