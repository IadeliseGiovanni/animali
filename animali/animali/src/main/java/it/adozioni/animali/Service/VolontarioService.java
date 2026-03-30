package it.adozioni.animali.Service;

import it.adozioni.animali.Mapper.VolontarioMapper;
import it.adozioni.animali.Model.Volontario;
import it.adozioni.animali.Dto.VolontarioDto;
import it.adozioni.animali.Repository.VolontarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolontarioService extends AbstractService<Volontario, VolontarioDto> {

    private final VolontarioRepository volontarioRepository;

    public VolontarioService(VolontarioRepository volontarioRepository,
                             VolontarioMapper volontarioMapper) {

        super(volontarioRepository, volontarioMapper);
        this.volontarioRepository = volontarioRepository;
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
}
