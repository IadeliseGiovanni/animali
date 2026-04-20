package it.adozioni.animali.Repository;

import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Model.Volontario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolontarioRepository extends JpaRepository<Volontario, Integer> {

    // 🔹 1 - Cerca per nome
    List<Volontario> findByNome(String nome);

    // 🔹 2 - Cerca per cognome
    List<Volontario> findByCognome(String cognome);

    // 🔹 3 - Cerca per codice fiscale
    Volontario findByCf(String cf);

    // 🔹 4 - Cerca per turno
    List<Volontario> findByTurno(String turno);

    // 🔹 5 - Cerca per nome e cognome
    Volontario findByNomeAndCognome(String nome, String cognome);

    // 🔹 6 - JPQL: nome + turno
    @Query("SELECT v FROM Volontario v WHERE v.nome = ?1 AND v.turno = ?2")
    List<Volontario> findByNomeAndTurno(String nome, String turno);

    // 🔹 7 - JPQL: cognome
    @Query("SELECT v FROM Volontario v WHERE v.cognome = ?1")
    List<Volontario> findByCognomeQuery(String cognome);

    // 🔹 8 - Native: codice fiscale
    @Query(value = "SELECT * FROM Volontario v WHERE v.cf = ?1", nativeQuery = true)
    Volontario findByCfNative(String cf);

    // 🔹 9 - Native: turno
    @Query(value = "SELECT * FROM Volontario v WHERE v.turno = ?1", nativeQuery = true)
    List<Volontario> findByTurnoNative(String turno);

    // 🔹 10 - Contiene (LIKE)
    List<Volontario> findByNomeContaining(String keyword);

    Optional<Volontario> findByEmail(String email);

    boolean existsByEmail(String email);
}
//