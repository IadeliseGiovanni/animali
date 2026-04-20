package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.AnimaleDto; // IMPORTANTE per il metodo obbligatorio
import it.adozioni.animali.Dto.VolontarioDto;
import it.adozioni.animali.Mapper.VolontarioMapper;
import it.adozioni.animali.Model.Volontario;
import it.adozioni.animali.Repository.VolontarioRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class VolontarioService extends AbstractService<Volontario, VolontarioDto> implements UserDetailsService {

    private final VolontarioRepository volontarioRepository;
    private final VolontarioMapper volontarioMapper;
    private final PasswordEncoder passwordEncoder;

    public VolontarioService(VolontarioRepository volontarioRepository,
                             VolontarioMapper volontarioMapper,
                             @Lazy PasswordEncoder passwordEncoder) {
        super(volontarioRepository, volontarioMapper);
        this.volontarioRepository = volontarioRepository;
        this.volontarioMapper = volontarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 🟢 FIX OBBLIGATORIO PER L'ERRORE DI COMPILAZIONE
     * Questo metodo deve esistere con questa firma esatta per AbstractService.
     */
    @Override
    public List<AnimaleDto> findAll() {
        // Restituiamo una lista vuota per "accontentare" la classe madre
        return new ArrayList<>();
    }

    /**
     * 🟢 IL VERO METODO FIND ALL PER VOLONTARI
     */
    @Transactional(readOnly = true)
    public List<VolontarioDto> findAllVolontari() {
        return volontarioMapper.toDTOList(volontarioRepository.findAll());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return volontarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Volontario non trovato con email: " + email));
    }

    @Transactional
    public VolontarioDto registra(VolontarioDto dto) {
        if (volontarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email già registrata per un altro volontario!");
        }

        Volontario entity = volontarioMapper.toEntity(dto);

        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new RuntimeException("La password è obbligatoria!");
        }

        entity.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Impostiamo il ruolo: se non c'è, mettiamo ADMIN per i tuoi test
        if (entity.getRuolo() == null || entity.getRuolo().isEmpty()) {
            entity.setRuolo("ADMIN");
        } else {
            entity.setRuolo(entity.getRuolo().toUpperCase());
        }

        entity.setEnabled(true); // Un volontario solitamente è attivo subito

        Volontario salvato = volontarioRepository.save(entity);
        return volontarioMapper.toDTO(salvato);
    }

    // --- METODI DI RICERCA ---

    public Volontario findByEmailEntity(String email) {
        return volontarioRepository.findByEmail(email).orElse(null);
    }

    public List<Volontario> cercaPerNome(String nome) {
        return volontarioRepository.findByNome(nome);
    }

    public Volontario cercaPerCf(String cf) {
        return volontarioRepository.findByCf(cf);
    }

    public VolontarioDto findByCfDto(String cf) {
        return volontarioMapper.toDTO(volontarioRepository.findByCf(cf));
    }

    public List<VolontarioDto> findByNomeDto(String nome) {
        return volontarioMapper.toDTOList(volontarioRepository.findByNome(nome));
    }

    public List<VolontarioDto> findByCognomeDto(String cognome) {
        return volontarioMapper.toDTOList(volontarioRepository.findByCognome(cognome));
    }

    public List<VolontarioDto> findByTurnoDto(String turno) {
        return volontarioMapper.toDTOList(volontarioRepository.findByTurno(turno));
    }

    public VolontarioDto findByNomeAndCognome(String nome, String cognome) {
        return volontarioMapper.toDTO(volontarioRepository.findByNomeAndCognome(nome, cognome));
    }

    public List<VolontarioDto> findByNomeAndTurno(String nome, String turno) {
        return volontarioMapper.toDTOList(volontarioRepository.findByNomeAndTurno(nome, turno));
    }

    public List<VolontarioDto> searchByKeyword(String keyword) {
        return volontarioMapper.toDTOList(volontarioRepository.findByNomeContaining(keyword));
    }
}