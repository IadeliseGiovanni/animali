package it.adozioni.animali.Repository;

import it.adozioni.animali.Model.VisitaMedica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitaMedicaRepository extends JpaRepository<VisitaMedica, Long> {

    List<VisitaMedica> findByData(LocalDateTime data);

    List<VisitaMedica> findByEsito(String esito);

    List<VisitaMedica> findByVeterinario(String veterinario);

    List<VisitaMedica> findByDataAndVeterinario(LocalDateTime data, String veterinario);

    List<VisitaMedica> findByDataAndEsito(LocalDateTime data, String esito);

}
