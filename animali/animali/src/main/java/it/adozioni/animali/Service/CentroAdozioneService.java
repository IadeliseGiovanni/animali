package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.CentroAdozioneDto;
import it.adozioni.animali.Mapper.CentroAdozioneMapper;
import it.adozioni.animali.Model.CentroAdozione;
import it.adozioni.animali.Repository.CentroAdozioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CentroAdozioneService implements ServiceDTO<CentroAdozioneDto> {

    @Autowired
    private CentroAdozioneRepository repository;

    @Autowired
    private CentroAdozioneMapper mapper;

    // --- METODI CRUD RICHIESTI DALL'INTERFACCIA ---

    @Override
    @Transactional
    public CentroAdozioneDto insert(CentroAdozioneDto dto) {
        if (dto == null) return null;
        CentroAdozione entity = mapper.toEntity(dto);
        CentroAdozione salvato = repository.save(entity);
        return mapper.toDTO(salvato);
    }

    @Override
    @Transactional(readOnly = true)
    public CentroAdozioneDto read(Integer id) {
        return repository.findById(id).map(mapper::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public CentroAdozioneDto update(CentroAdozioneDto dto) {
        return insert(dto);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        elimina(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<CentroAdozioneDto> getAll() {
        return listaTuttiICentri();
    }

    // --- ALTRI METODI ESISTENTI ---

    @Transactional(readOnly = true)
    public List<CentroAdozioneDto> listaTuttiICentri() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CentroAdozioneDto> findByCitta(String citta) {
        return repository.findByCitta(citta).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CentroAdozioneDto> findByIsNoProfit(boolean noProfit) {
        return repository.findByIsNoProfit(noProfit).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CentroAdozioneDto findByNomeCentro(String nome) {
        List<CentroAdozione> list = repository.findByNomeCentro(nome);
        return list.isEmpty() ? null : mapper.toDTO(list.get(0));
    }

    /**
     * IMPLEMENTAZIONE ELIMINA:
     * Richiamata sia dal metodo delete() dell'interfaccia che dal Controller.
     */
    @Transactional
    public void elimina(Integer id) {
        if (id != null) {
            repository.deleteById(id);
        }
    }

    public CentroAdozioneDto salvaNuovo(CentroAdozioneDto dto) {
        // 1. Converti DTO in Entity
        CentroAdozione entity = mapper.toEntity(dto);

        // 2. Salva sul DB tramite Repository
        CentroAdozione salvato = repository.save(entity);

        // 3. Riconverti in DTO e restituisci
        return mapper.toDTO(salvato);
    }
}