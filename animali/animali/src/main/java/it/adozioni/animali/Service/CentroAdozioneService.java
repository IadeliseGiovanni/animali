package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.CentroAdozioneDto;
import it.adozioni.animali.Mapper.CentroAdozioneMapper;
import it.adozioni.animali.Model.CentroAdozione;
import it.adozioni.animali.Repository.CentroAdozioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CentroAdozioneService {

    @Autowired
    private CentroAdozioneRepository repository;

    @Autowired
    private CentroAdozioneMapper mapper; // Iniettiamo il mapper che hai appena creato

    // Metodo aggiornato: molto più corto e pulito!
    public List<CentroAdozioneDto> listaTuttiICentri() {
        // 1. Prendo la lista delle Entity (Model) dal database
        List<CentroAdozione> modelli = repository.findAll();

        // 2. Uso il metodo ereditato dall'AbstractConverter per convertire tutta la lista in un colpo solo
        return mapper.toDtoList(modelli);
    }
}