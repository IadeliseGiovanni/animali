package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.AdottanteDto;
import it.adozioni.animali.Mapper.AdottanteMapper;
import it.adozioni.animali.Mapper.VolontarioMapper;
import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Model.Volontario;
import it.adozioni.animali.Dto.VolontarioDto;
import it.adozioni.animali.Repository.VolontarioRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VolontarioService extends AbstractService<Volontario, VolontarioDto> implements UserDetailsService {

    private final VolontarioRepository volontarioRepository;
    private final VolontarioMapper volontarioMapper;
    private final PasswordEncoder passwordEncoder;

    public VolontarioService(VolontarioRepository volontarioRepository,
                             VolontarioMapper volontarioMapper, @Lazy PasswordEncoder passwordEncoder) {

        super(volontarioRepository, volontarioMapper);
        this.volontarioRepository = volontarioRepository;
        this.volontarioMapper = volontarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return volontarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));
    }
//
    public List<VolontarioDto> findAll() {
        return volontarioMapper.toDTOList(repository.findAll());
    }

    // 🔹 1 - Cerca per nome
    public List<Volontario> cercaPerNome(String nome) {
        return volontarioRepository.findByNome(nome);
    }

    // 🔹 2 - Cerca per codice fiscale
    public Volontario cercaPerCf(String cf) {
        return volontarioRepository.findByCf(cf);
    }

    // 🔹 3 - DTO per CF
    public VolontarioDto findByCfDto(String cf) {
        return converter.toDTO(volontarioRepository.findByCf(cf));
    }

    // 🔹 4 - DTO per nome
    public List<VolontarioDto> findByNomeDto(String nome) {
        return converter.toDTOList(volontarioRepository.findByNome(nome));
    }

    // 🔹 5 - Cerca per cognome
    public List<Volontario> cercaPerCognome(String cognome) {
        return volontarioRepository.findByCognome(cognome);
    }

    // 🔹 6 - DTO cognome
    public List<VolontarioDto> findByCognomeDto(String cognome) {
        return converter.toDTOList(volontarioRepository.findByCognome(cognome));
    }

    // 🔹 7 - Cerca per turno
    public List<VolontarioDto> findByTurnoDto(String turno) {
        return converter.toDTOList(volontarioRepository.findByTurno(turno));
    }

    // 🔹 8 - Nome + Cognome
    public VolontarioDto findByNomeAndCognome(String nome, String cognome) {
        return converter.toDTO(volontarioRepository.findByNomeAndCognome(nome, cognome));
    }

    // 🔹 9 - JPQL nome + turno
    public List<VolontarioDto> findByNomeAndTurno(String nome, String turno) {
        return converter.toDTOList(volontarioRepository.findByNomeAndTurno(nome, turno));
    }

    // 🔹 10 - Ricerca LIKE
    public List<VolontarioDto> searchByKeyword(String keyword) {
        return converter.toDTOList(volontarioRepository.findByNomeContaining(keyword));
    }

    @Transactional
    public VolontarioDto registra(VolontarioDto dto) {
        // 1. Mappatura DTO -> Entity
        // Usiamo 'converter' che è il mapper ereditato da AbstractService
        Volontario entity = volontarioMapper.toEntity(dto);

        // 2. Criptazione Password (BCrypt)
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new RuntimeException("La password è obbligatoria per la registrazione del volontario!");
        }

        // Assicurati che passwordEncoder sia iniettato nel costruttore del Service
        String passwordCriptata = passwordEncoder.encode(dto.getPassword());
        entity.setPassword(passwordCriptata);

        // 3. Setup dei ruoli (Logica per i tuoi Test)
        // Se il DTO non specifica nulla, mettiamo "USER" di default.
        // Se vuoi creare un ADMIN, dovrai passare "ADMIN" nel JSON di Postman.
        if (entity.getRuolo() == null || entity.getRuolo().isEmpty()) {
            entity.setRuolo("ADMIN");
        } else {
            // Normalizziamo in maiuscolo per coerenza con getAuthorities()
            entity.setRuolo(entity.getRuolo().toUpperCase());
        }

        // 4. Salvataggio e log
        Volontario salvato = volontarioRepository.save(entity);
        System.out.println("DEBUG: Volontario registrato con successo: " + salvato.getEmail() + " con ruolo: " + salvato.getRuolo());

        // 5. Ritorno del DTO (il mapper nasconderà la password se configurato correttamente)
        return volontarioMapper.toDTO(salvato);
    }
}
