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
public class CentroAdozioneService {

    @Autowired
    private CentroAdozioneRepository repository;
    @Autowired
    private CentroAdozioneMapper mapper;

    // --- METODI PER "USER" ---
    public List<CentroAdozioneDto> listaTuttiICentri() {
        return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<CentroAdozioneDto> findByCitta(String citta) {
        return repository.findByCitta(citta).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<CentroAdozioneDto> findByIsNoProfit(boolean noProfit) {
        return repository.findByIsNoProfit(noProfit).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public CentroAdozioneDto findByNomeCentro(String nome) {
        List<CentroAdozione> list = repository.findByNomeCentro(nome);
        return list.isEmpty() ? null : mapper.toDTO(list.get(0));
    }

    // --- METODI PER "ADMIN" (Quelli che probabilmente ti danno rosso) ---
    public CentroAdozioneDto salvaNuovo(CentroAdozioneDto dto) {
        CentroAdozione entity = mapper.toEntity(dto);
        CentroAdozione salvato = repository.save(entity);
        return mapper.toDTO(salvato);
    }

    public CentroAdozioneDto aggiorna(Integer id, CentroAdozioneDto dto) {
        if (!repository.existsById(id)) return null;
        CentroAdozione entity = mapper.toEntity(dto);
        entity.setId(id); // Assicura che stiamo aggiornando lo stesso ID
        return mapper.toDTO(repository.save(entity));
    }

    public void elimina(Integer id) {
        repository.deleteById(id);
    }
}