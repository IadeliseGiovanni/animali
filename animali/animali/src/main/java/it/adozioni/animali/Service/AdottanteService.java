package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.AdottanteDto;
import it.adozioni.animali.Dto.AnimaleDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdottanteService extends AbstractService<Adottante, AdottanteDto> implements UserDetailsService {

    private final AdottanteRepository adottanteRepository;
    private final AdottanteMapper adottanteMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public AdottanteService(AdottanteRepository adottanteRepository,
                            AdottanteMapper adottanteMapper,
                            @Lazy PasswordEncoder passwordEncoder,
                            EmailService emailService) {
        super(adottanteRepository, adottanteMapper);
        this.adottanteRepository = adottanteRepository;
        this.adottanteMapper = adottanteMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
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
        // 1. Recupero l'entità esistente dal DB
        Adottante esistente = adottanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adottante non trovato con ID: " + id));

        // 2. Aggiorno solo i campi anagrafici se presenti nel DTO
        if (dto.getNome() != null) {
            esistente.setNome(dto.getNome());
        }
        if (dto.getCognome() != null) {
            esistente.setCognome(dto.getCognome());
        }
        if (dto.getCodiceFiscale() != null) {
            esistente.setCodiceFiscale(dto.getCodiceFiscale());
        }
        if (dto.getTelefono() != null) {
            esistente.setTelefono(dto.getTelefono());
        }
        if (dto.getIndirizzo() != null) {
            esistente.setIndirizzo(dto.getIndirizzo());
        }
        if (dto.getEmail() != null) {
            esistente.setEmail(dto.getEmail());
        }

        // 3. Salvo l'entità aggiornata (la password rimane quella originale già presente in 'esistente')
        Adottante salvato = adottanteRepository.save(esistente);

        // 4. Ritorno il DTO aggiornato
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

}