package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.AdottanteDto;
import it.adozioni.animali.Dto.VolontarioDto;
import it.adozioni.animali.Mapper.AdottanteMapper;
import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Repository.AdottanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hibernate.cfg.JdbcSettings.USER;

@Service
public class AdottanteService extends AbstractService<Adottante, AdottanteDto> implements UserDetailsService {

    private final AdottanteRepository adottanteRepository;
    private final AdottanteMapper adottanteMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public AdottanteService(AdottanteRepository adottanteRepository,
                            AdottanteMapper adottanteMapper,
                            @Lazy PasswordEncoder passwordEncoder, EmailService emailService) {
        super(adottanteRepository, adottanteMapper);
        this.adottanteRepository = adottanteRepository;
        this.adottanteMapper = adottanteMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public List<AdottanteDto> findAll() {
        return adottanteMapper.toDTOList(repository.findAll());
    }

    /**
     * Metodo fondamentale per Spring Security: recupera l'utente dal DB tramite email.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Cerchiamo l'utente nel DB
        Adottante utente = adottanteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));

        return utente;
    }
//
    /**
     * Registra un nuovo utente criptando la password.
     */
    @Transactional
    public AdottanteDto registra(AdottanteDto dto) {
        // 1. Mappatura DTO -> Entity
        Adottante entity = adottanteMapper.toEntity(dto);

        // 2. Controllo Email Duplicata
        if (adottanteRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Errore: Questa email è già registrata su PetFlow!");
        }

        // 3. Criptazione Password
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new RuntimeException("La password è obbligatoria per la registrazione!");
        }
        String passwordCriptata = passwordEncoder.encode(dto.getPassword());
        entity.setPassword(passwordCriptata);

        // 4. Setup campi di default e SICUREZZA
        if (entity.getRuolo() == null || entity.getRuolo().isEmpty()) {
            entity.setRuolo("USER");
        }
        entity.setIsSchedato(false);

        // --- NUOVA LOGICA DI VERIFICA EMAIL ---
        String tokenVerifica = java.util.UUID.randomUUID().toString();
        entity.setVerificationToken(tokenVerifica);
        entity.setEnabled(false); // L'utente parte come disabilitato
        // ---------------------------------------

        // 5. Salvataggio nel DB
        Adottante salvato = adottanteRepository.save(entity);

        this.inviaEmailVerifica(salvato.getEmail(), tokenVerifica);
        return adottanteMapper.toDTO(salvato);
    }

    // Metodo per completare la verifica (quello che ti mancava)
    @Transactional
    public boolean verifyToken(String token) {
        return adottanteRepository.findByVerificationToken(token)
                .map(utente -> {
                    utente.setEnabled(true);
                    utente.setVerificationToken(null); // Puliamo il token dopo l'uso
                    adottanteRepository.save(utente);
                    return true;
                }).orElse(false);
    }

    /**
     * Metodo usato dal Controller per recuperare l'oggetto Adottante reale
     */
    public Adottante findByIdEntity(Integer id) {
        if (id == null) return null;
        return adottanteRepository.findById(id).orElse(null);
    }

    public List<AdottanteDto> findByCognome(String cognome) {
        List<Adottante> listaEntity = adottanteRepository.findByCognome(cognome);
        return adottanteMapper.toDTOList(listaEntity);
    }

    @Transactional(readOnly = true)
    public AdottanteDto getMioProfilo(String email) {
        // 1. Recupero l'entity dal DB
        Adottante entity = adottanteRepository.findByEmailWithAnimals(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // 2. Mappatura Entity -> DTO
        // Se il tuo mapper già gestisce la lista 'animaliAdottati', usalo pure
        return adottanteMapper.toDTO(entity);
    }

    @Transactional
    public void aggiornaIdoneita(int id, boolean stato) {
        Adottante adottante = adottanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adottante non trovato"));

        adottante.setIsSchedato(stato);
        adottanteRepository.save(adottante);
    }

    @Transactional
    public void aggiornaRuolo(int id, String nuovoRuolo) {
        Adottante adottante = adottanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adottante non trovato"));

        // Se usi un Enum per il ruolo: Ruolo.valueOf(nuovoRuolo)
        adottante.setRuolo(nuovoRuolo);
        adottanteRepository.save(adottante);
    }

    public Adottante findByEmailEntity(String email) {
        return adottanteRepository.findByEmail(email).orElse(null);
    }

    public void inviaEmailVerifica(String email, String token) {
        try {
            emailService.sendVerificationEmail(email, token);
            System.out.println("DEBUG: Email di verifica inviata a: " + email);
        } catch (Exception e) {
            System.err.println("DEBUG ERROR: Errore durante l'invio email: " + e.getMessage());
            throw new RuntimeException("Impossibile inviare l'email di verifica. Riprova più tardi.");
        }
    }
}