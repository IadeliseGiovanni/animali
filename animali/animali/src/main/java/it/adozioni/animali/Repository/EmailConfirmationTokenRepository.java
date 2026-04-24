package it.adozioni.animali.Repository;

import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Model.EmailConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, Long> {

    // Trova il token per validare il click dell'utente sul link
    Optional<EmailConfirmationToken> findByToken(String token);

    // Utile per pulire vecchie richieste se l'utente ne fa una nuova
    void deleteByAdottante(Adottante adottante);

    Optional<EmailConfirmationToken> findByAdottante(Adottante adottante);
}