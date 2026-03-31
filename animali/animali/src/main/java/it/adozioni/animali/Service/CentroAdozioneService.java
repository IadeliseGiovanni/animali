package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.CentroAdozioneDto;
import it.adozioni.animali.Mapper.CentroAdozioneMapper;
import it.adozioni.animali.Model.CentroAdozione;
import it.adozioni.animali.Repository.CentroAdozioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CentroAdozioneService extends AbstractService<CentroAdozione, CentroAdozioneDto> {

    private final CentroAdozioneRepository repository;
    private final CentroAdozioneMapper mapper;

    @Autowired
    public CentroAdozioneService(CentroAdozioneRepository repository, CentroAdozioneMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    // Restituisce DTO (per il Controller)
    public List<CentroAdozioneDto> findByCitta(String citta) {
        return repository.findByCitta(citta).stream()
                .map(entity -> (CentroAdozioneDto) mapper.toDTO(entity)) // Cast aggiunto qui
                .collect(Collectors.toList());
    }

    // Restituisce DTO (per il Controller)
    public List<CentroAdozioneDto> findByIsNoProfit(boolean noProfit) {
        return repository.findByIsNoProfit(noProfit).stream()
                .map(entity -> (CentroAdozioneDto) mapper.toDTO(entity)) // Cast aggiunto qui
                .collect(Collectors.toList());
    }

    // Restituisce un singolo DTO
    public CentroAdozioneDto findByNomeCentro(String nome) {
        // Se la repository restituisce una List, prendiamo il primo elemento col cast
        List<CentroAdozione> entities = repository.findByNomeCentro(nome);
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        return (CentroAdozioneDto) mapper.toDTO(entities.get(0)); // Cast aggiunto qui
    }

    // Metodi che restituiscono Entity (utili per i Service o i Test interni)
    public List<CentroAdozione> findAll() {
        return repository.findAll();
    }

    public CentroAdozione findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Iterable<CentroAdozioneDto> listaTuttiICentri() {
        return getAll();
    }
}