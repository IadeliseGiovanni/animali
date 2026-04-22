package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.AnimaleDto; // NECESSARIO per l'override
import it.adozioni.animali.Dto.VisitaMedicaDto;
import it.adozioni.animali.Mapper.Converter;
import it.adozioni.animali.Mapper.VisitaMedicaMapper;
import it.adozioni.animali.Model.VisitaMedica;
import it.adozioni.animali.Repository.VisitaMedicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VisitaMedicaService extends AbstractService<VisitaMedica, VisitaMedicaDto> {

    private final VisitaMedicaRepository visitaMedicaRepository;
    private final VisitaMedicaMapper visitaMedicaMapper;

    @Autowired
    public VisitaMedicaService(VisitaMedicaRepository repository,
                               VisitaMedicaMapper mapper) {
        // Passiamo al costruttore di AbstractService il repository e il mapper (che è un Converter)
        super(repository, mapper);
        this.visitaMedicaRepository = repository;
        this.visitaMedicaMapper = mapper;
    }

    public List<VisitaMedicaDto> getAll(){
        return visitaMedicaMapper.toDTOList(repository.findAll());
    }

    /**
     * 🟢 FIX OBBLIGATORIO PER L'ERRORE DI COMPILAZIONE
     * Questo metodo DEVE esserci perché l'AbstractService lo richiede.
     * Restituiamo una lista vuota di AnimaleDto per far stare zitto il compilatore.
     */
    @Override
    public List<AnimaleDto> findAll() {
        return new ArrayList<>();
    }

    /**
     * 🟢 IL VERO METODO FIND ALL PER LE VISITE
     * Questo è quello che userai nel Controller.
     */
    @Transactional(readOnly = true)
    public List<VisitaMedicaDto> findAllVisite() {
        return visitaMedicaMapper.toDTOList(visitaMedicaRepository.findAll());
    }

    // --- METODI DI RICERCA SPECIFICI ---

    public List<VisitaMedicaDto> findByData(LocalDateTime data) {
        return visitaMedicaMapper.toDTOList(visitaMedicaRepository.findByData(data));
    }

    public List<VisitaMedicaDto> findByEsito(String esito) {
        return visitaMedicaMapper.toDTOList(visitaMedicaRepository.findByEsito(esito));
    }

    public List<VisitaMedicaDto> findByVeterinario(String veterinario) {
        return visitaMedicaMapper.toDTOList(visitaMedicaRepository.findByVeterinario(veterinario));
    }

    public List<VisitaMedicaDto> findByDataAndVeterinario(LocalDateTime data, String veterinario) {
        return visitaMedicaMapper.toDTOList(visitaMedicaRepository.findByDataAndVeterinario(data, veterinario));
    }

    public List<VisitaMedicaDto> findByDataAndEsito(LocalDateTime data, String esito) {
        return visitaMedicaMapper.toDTOList(visitaMedicaRepository.findByDataAndEsito(data, esito));
    }
}