package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.AdottanteDto;
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

import java.util.List;

@Service
public class AdottanteService extends AbstractService<Adottante, AdottanteDto> implements UserDetailsService {

    private final AdottanteRepository adottanteRepository;
    private final AdottanteMapper adottanteMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    protected AdottanteService(AdottanteRepository adottanteRepository,
                               AdottanteMapper adottanteMapper,
                               @Lazy PasswordEncoder passwordEncoder) {
        super(adottanteRepository, adottanteMapper);
        this.adottanteRepository = adottanteRepository;
        this.adottanteMapper = adottanteMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // Metodo richiesto da Spring Security per l'autenticazione JWT
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return adottanteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));
    }

    public AdottanteDto registra(AdottanteDto dto) {
        // 1. Mappa il DTO in Entity (la password è ancora quella in chiaro di Postman)
        Adottante entity = adottanteMapper.toEntity(dto);

        // 2. Cripta la password prima del salvataggio
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // 3. Imposta i campi di default
        if (entity.getRuolo() == null || entity.getRuolo().isEmpty()) {
            entity.setRuolo("USER");
        }
        entity.setIsSchedato(false);

        // 4. Salva nel database e restituisce il DTO
        Adottante salvato = adottanteRepository.save(entity);
        return adottanteMapper.toDTO(salvato);
    }

    public Adottante findByIdEntity(Integer id) {
        return adottanteRepository.findById(id).orElse(null);
    }

    public List<AdottanteDto> findByCognome(String cognome) {
        List<Adottante> listaEntity = adottanteRepository.findByCognome(cognome);
        return adottanteMapper.toDTOList(listaEntity);
    }
}