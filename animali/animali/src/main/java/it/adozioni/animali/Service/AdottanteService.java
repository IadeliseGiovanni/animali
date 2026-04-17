package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.AdottanteDto;
import it.adozioni.animali.Dto.VolontarioDto;
import it.adozioni.animali.Mapper.AdottanteMapper;
import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Repository.AdottanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        return adottanteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));
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

        // 6. INVIO REALE DELL'EMAIL
        // Usiamo un try-catch opzionale se non vuoi che l'intera registrazione
        // fallisca nel caso il server email sia momentaneamente offline
        try {
            emailService.sendVerificationEmail(salvato.getEmail(), tokenVerifica);
            System.out.println("DEBUG: Email di verifica inviata a: " + salvato.getEmail());
        } catch (Exception e) {
            System.err.println("DEBUG ERROR: Impossibile inviare l'email: " + e.getMessage());
            // Opzionale: puoi decidere di lanciare un'eccezione o continuare
        }

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
}