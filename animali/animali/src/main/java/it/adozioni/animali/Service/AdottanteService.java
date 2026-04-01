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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdottanteService extends AbstractService<Adottante, AdottanteDto> implements UserDetailsService {

    private final AdottanteRepository adottanteRepository;
    private final AdottanteMapper adottanteMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdottanteService(AdottanteRepository adottanteRepository,
                            AdottanteMapper adottanteMapper,
                            @Lazy PasswordEncoder passwordEncoder) {
        super(adottanteRepository, adottanteMapper);
        this.adottanteRepository = adottanteRepository;
        this.adottanteMapper = adottanteMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Metodo fondamentale per Spring Security: recupera l'utente dal DB tramite email.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return adottanteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));
    }

    /**
     * Registra un nuovo utente criptando la password.
     */
    @Transactional
    public AdottanteDto registra(AdottanteDto dto) {
        // 1. Mappatura DTO -> Entity
        Adottante entity = adottanteMapper.toEntity(dto);

        // 2. Criptazione Password (BCrypt)
        // Se la password non c'è nel DTO (magari dimenticata su Postman), lanciamo errore
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new RuntimeException("La password è obbligatoria per la registrazione!");
        }

        String passwordCriptata = passwordEncoder.encode(dto.getPassword());
        entity.setPassword(passwordCriptata);

        // 3. Setup campi di default (Admin o User)
        if (entity.getRuolo() == null || entity.getRuolo().isEmpty()) {
            entity.setRuolo("USER");
        }

        // Un nuovo utente non è mai schedato (bloccato) di default
        entity.setIsSchedato(false);

        // 4. Salvataggio e ritorno del DTO (senza password nel ritorno per sicurezza)
        Adottante salvato = adottanteRepository.save(entity);
        System.out.println("DEBUG: Utente registrato con successo: " + salvato.getEmail());

        return adottanteMapper.toDTO(salvato);
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