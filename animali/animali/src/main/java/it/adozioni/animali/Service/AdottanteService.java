package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.AdottanteDto;
import it.adozioni.animali.Dto.AnimaleDto;
import it.adozioni.animali.Mapper.AdottanteMapper;
import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Model.EmailConfirmationToken;
import it.adozioni.animali.Repository.AdottanteRepository;
import it.adozioni.animali.Repository.EmailConfirmationTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdottanteService extends AbstractService<Adottante, AdottanteDto> implements UserDetailsService {

    private final AdottanteRepository adottanteRepository;
    private final AdottanteMapper adottanteMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailConfirmationTokenRepository tokenRepository;

    @Autowired
    public AdottanteService(AdottanteRepository adottanteRepository,
                            AdottanteMapper adottanteMapper,
                            @Lazy PasswordEncoder passwordEncoder,
                            EmailService emailService, EmailConfirmationTokenRepository tokenRepository) {
        super(adottanteRepository, adottanteMapper);
        this.adottanteRepository = adottanteRepository;
        this.adottanteMapper = adottanteMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.tokenRepository = tokenRepository;
    }

    /**
     * METODO REGISTRAZIONE: Risolve il rosso in AuthController
     */
    @Transactional
    public AdottanteDto registra(AdottanteDto dto) {
        if (adottanteRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email già registrata!");
        }

        Adottante entity = adottanteMapper.toEntity(dto);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setRuolo("USER");
        entity.setEnabled(false); // L'utente deve prima confermare la mail
        entity.setIsSchedato(false);

        String token = UUID.randomUUID().toString();
        entity.setVerificationToken(token);

        Adottante salvato = adottanteRepository.save(entity);

        // Invio mail di verifica
        this.inviaEmailVerifica(salvato.getEmail(), token);

        return adottanteMapper.toDTO(salvato);
    }

    /**
     * VERIFICA TOKEN: Risolve il rosso in AuthController riga /verify
     */
    @Transactional
    public boolean verifyToken(String token) {
        return adottanteRepository.findByVerificationToken(token)
                .map(utente -> {
                    utente.setEnabled(true);
                    utente.setVerificationToken(null);
                    adottanteRepository.save(utente);
                    return true;
                }).orElse(false);
    }

    /**
     * FIND BY EMAIL ENTITY: Risolve il rosso in AuthController riga 107
     */
    public Adottante findByEmailEntity(String email) {
        return adottanteRepository.findByEmail(email).orElse(null);
    }

    /**
     * INVIO EMAIL: Metodo di supporto per il reinvio
     */
    public void inviaEmailVerifica(String email, String token) {
        emailService.sendVerificationEmail(email, token);
    }

    /**
     * FIND BY ID ENTITY: Risolve il rosso nel Controller del PDF
     */
    public Adottante findByIdEntity(Integer id) {
        if (id == null) return null;
        return adottanteRepository.findById(id).orElse(null);
    }

    @Override
    public List<AnimaleDto> findAll() {
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<AdottanteDto> findAllAdottantiTrue() {
        return adottanteMapper.toDTOList(adottanteRepository.findAll());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return adottanteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + email));
    }

    @Transactional
    public void aggiornaIdoneita(int id, boolean stato) {
        adottanteRepository.findById(id).ifPresent(a -> {
            // 1. Aggiorna il flag booleano
            a.setIsSchedato(stato);

            // 2. Sincronizza lo stato testuale (Enum)
            if (stato) {
                // Se l'admin lo rende idoneo
                a.setStatoIdoneita(Adottante.StatoIdoneita.IDONEO);
            } else {
                // Se l'admin toglie l'idoneità, resettiamo lo stato così può richiederla
                a.setStatoIdoneita(Adottante.StatoIdoneita.NON_RICHIESTA);
            }

            adottanteRepository.save(a);
        });
    }

    @Transactional
    public void aggiornaRuolo(int id, String nuovoRuolo) {
        adottanteRepository.findById(id).ifPresent(a -> {
            a.setRuolo(nuovoRuolo);
            adottanteRepository.save(a);
        });
    }

    public List<AdottanteDto> findByCognome(String cognome) {
        return adottanteMapper.toDTOList(adottanteRepository.findByCognome(cognome));
    }

    @Transactional(readOnly = true)
    public AdottanteDto getMioProfilo(String email) {
        Adottante entity = adottanteRepository.findByEmailWithAnimals(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        return adottanteMapper.toDTO(entity);
    }

    @Transactional
    public AdottanteDto patch(Integer id, AdottanteDto dto) {
        // 1. Recupero l'entità esistente
        Adottante adottante = adottanteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adottante non trovato con ID: " + id));

        // 2. Aggiorno solo i campi anagrafici (NON l'email)
        if (dto.getNome() != null) adottante.setNome(dto.getNome());
        if (dto.getCognome() != null) adottante.setCognome(dto.getCognome());
        if (dto.getDataDiNascita() != null) adottante.setDataDiNascita(dto.getDataDiNascita());
        if (dto.getIndirizzo() != null) adottante.setIndirizzo(dto.getIndirizzo());
        if (dto.getTelefono() != null) adottante.setTelefono(dto.getTelefono());
        if (dto.getCodiceFiscale() != null) adottante.setCodiceFiscale(dto.getCodiceFiscale());

        // 3. Salvo e ritorno il DTO aggiornato
        Adottante salvato = adottanteRepository.save(adottante);
        return adottanteMapper.toDTO(salvato);
    }

    @Transactional
    public void avviaPraticaIdoneita(Integer adottanteId) {
        Adottante adottante = adottanteRepository.findById(adottanteId)
                .orElseThrow(() -> new RuntimeException("Adottante non trovato"));

        // Controlliamo se è null OPPURE se è diverso da NON_RICHIESTA
        if (adottante.getStatoIdoneita() != null &&
                adottante.getStatoIdoneita() != Adottante.StatoIdoneita.NON_RICHIESTA) {
            throw new RuntimeException("Esiste già una pratica in corso o sei già idoneo.");
        }

        // Se è null o NON_RICHIESTA, procediamo
        adottante.setStatoIdoneita(Adottante.StatoIdoneita.IN_ATTESA);
        adottanteRepository.save(adottante);

        emailService.inviaConfermaRichiestaIdoneita(adottante.getEmail(), adottante.getNome());
    }

    @Transactional
    public String richiediCambioEmail(Integer adottanteId, String nuovaEmail) {
        Adottante adottante = adottanteRepository.findById(adottanteId)
                .orElseThrow(() -> new RuntimeException("Adottante non trovato"));

        // 1. Cerca il token esistente
        tokenRepository.findByAdottante(adottante).ifPresent(token -> {
            tokenRepository.delete(token);
            // 2. FORZA la cancellazione immediata sul database
            tokenRepository.flush();
        });

        // 3. Ora procedi con la creazione del nuovo token
        String stringToken = UUID.randomUUID().toString();
        EmailConfirmationToken newToken = new EmailConfirmationToken();
        newToken.setToken(stringToken);
        newToken.setNuovaEmail(nuovaEmail);
        newToken.setAdottante(adottante);
        newToken.setDataScadenza(LocalDateTime.now().plusHours(24));

        tokenRepository.save(newToken);

        String linkConferma = "http://localhost:8080/api/Adottante/conferma-email?token=" + stringToken;
        emailService.inviaMailConferma(nuovaEmail, linkConferma);

        return "Email inviata con successo!";
    }

    @Transactional
    public void confermaCambioEmail(String token) {
        // 1. Recupera il token con i dati dell'adottante caricati
        EmailConfirmationToken confermaToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token non valido o già utilizzato."));

        // 2. Prendi l'adottante associato
        Adottante adottante = confermaToken.getAdottante();
        String vecchiaEmail = adottante.getEmail();
        String nuovaEmail = confermaToken.getNuovaEmail();

        // 3. AGGIORNA L'EMAIL
        adottante.setEmail(nuovaEmail);

        // 4. SALVA E FORZA IL FLUSH IMMEDIATO
        // Questo è il passaggio che mancava per rendere il cambio effettivo subito
        adottanteRepository.saveAndFlush(adottante);

        // 5. CANCELLA IL TOKEN
        tokenRepository.delete(confermaToken);
        tokenRepository.flush(); // Puliamo anche qui

        // 6. INVIA LA NOTIFICA (alla vecchia email per sicurezza)
        try {
            emailService.inviaNotificaCambioEffettuato(vecchiaEmail, nuovaEmail);
        } catch (Exception e) {
            // Logga l'errore ma non bloccare la transazione se la mail fallisce
            System.err.println("Notifica di sicurezza fallita: " + e.getMessage());
        }
    }

    @Transactional
    public void cambiaPassword(Integer adottanteId, String vecchiaPassword, String nuovaPassword) {
        Adottante adottante = adottanteRepository.findById(adottanteId)
                .orElseThrow(() -> new RuntimeException("Adottante non trovato"));

        // 1. Verifica che la password attuale sia corretta
        if (!passwordEncoder.matches(vecchiaPassword, adottante.getPassword())) {
            throw new RuntimeException("La password attuale non è corretta");
        }

        // 2. Cripta e imposta la nuova password
        adottante.setPassword(passwordEncoder.encode(nuovaPassword));

        // 3. Salva e forza il commit
        adottanteRepository.saveAndFlush(adottante);

        // 4. Invia email di conferma (notifica di sicurezza)
        emailService.inviaConfermaCambioPassword(adottante.getEmail());
    }

    // AdottanteService.java

    @Transactional // Fondamentale per scrivere fisicamente sul DB
    public void salvaResetToken(Integer id, String token) {
        Adottante adottante = adottanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        adottante.setResetToken(token);
        // Imposta la scadenza a 30 minuti da ora
        adottante.setResetTokenExpiration(LocalDateTime.now().plusMinutes(30));

        // Forza il salvataggio immediato
        adottanteRepository.saveAndFlush(adottante);
    }

    @Transactional
    public boolean aggiornaPasswordConToken(String token, String nuovaPassword) {
        // Pulisci il token da eventuali spazi bianchi invisibili
        String cleanToken = token.trim();
        System.out.println("DEBUG: Ricerca nel DB per token: [" + cleanToken + "]");

        Optional<Adottante> opzionale = adottanteRepository.findByResetToken(cleanToken);

        if (opzionale.isPresent()) {
            Adottante adottante = opzionale.get();
            System.out.println("DEBUG: Utente trovato: " + adottante.getEmail());

            if (adottante.getResetTokenExpiration().isAfter(LocalDateTime.now())) {
                adottante.setPassword(passwordEncoder.encode(nuovaPassword));

                // Fondamentale: resetta i campi
                adottante.setResetToken(null);
                adottante.setResetTokenExpiration(null);

                adottanteRepository.saveAndFlush(adottante);
                return true;
            }
            System.out.println("DEBUG: Token scaduto il: " + adottante.getResetTokenExpiration());
        } else {
            System.out.println("DEBUG: Token non trovato nel database.");
        }
        return false;
    }
}