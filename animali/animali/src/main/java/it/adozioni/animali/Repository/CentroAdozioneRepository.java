package it.adozioni.animali.Repository;

import it.adozioni.animali.Model.CentroAdozione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// 🔹 CAMBIATO: Da Long a Integer per combaciare con l'AbstractService
public interface CentroAdozioneRepository extends JpaRepository<CentroAdozione, Integer> {

    // 1 - Cerca per nome del centro
    List<CentroAdozione> findByNomeCentro(String nomeCentro);

    // 2 - Cerca per città
    List<CentroAdozione> findByCitta(String citta);

    // 3 - Cerca per indirizzo esatto
    CentroAdozione findByIndirizzo(String indirizzo);

    // 4 - Cerca per stato NoProfit (true/false)
    List<CentroAdozione> findByIsNoProfit(Boolean isNoProfit);

    // 5 - Cerca per nome e città
    CentroAdozione findByNomeCentroAndCitta(String nomeCentro, String citta);

    // 6 - JPQL: nome + città
    @Query("SELECT c FROM CentroAdozione c WHERE c.nomeCentro = ?1 AND c.citta = ?2")
    List<CentroAdozione> findByNomeAndCittaQuery(String nome, String citta);

    // 7 - JPQL: tutti i NoProfit
    @Query("SELECT c FROM CentroAdozione c WHERE c.isNoProfit = true")
    List<CentroAdozione> findAllNoProfit();

    // 8 - Native: ricerca per indirizzo
    @Query(value = "SELECT * FROM centro_adozione c WHERE c.indirizzo = ?1", nativeQuery = true)
    CentroAdozione findByIndirizzoNative(String indirizzo);

    // 9 - Native: ricerca per città specifica
    @Query(value = "SELECT * FROM centro_adozione c WHERE c.citta = ?1", nativeQuery = true)
    List<CentroAdozione> findByCittaNative(String citta);

    // 10 - Contiene (LIKE): cerca centri che hanno una parola nel nome
    List<CentroAdozione> findByNomeCentroContaining(String keyword);
}