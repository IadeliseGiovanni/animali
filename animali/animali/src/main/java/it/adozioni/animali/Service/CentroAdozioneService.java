package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.CentroAdozioneDto;
import it.adozioni.animali.Model.CentroAdozione;
import it.adozioni.animali.Repository.CentroAdozioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CentroAdozioneService {

    @Autowired
    private CentroAdozioneRepository repository;

    // Metodo per trasformare i dati dal Database (Model) al Formato Web (Dto)
    public List<CentroAdozioneDto> listaTuttiICentri() {
        List<CentroAdozione> modelli = repository.findAll();
        List<CentroAdozioneDto> dtos = new ArrayList<>();

        for (CentroAdozione m : modelli) {
            CentroAdozioneDto d = new CentroAdozioneDto();
            d.setNomeCentro(m.getNomeCentro());
            d.setIndirizzo(m.getIndirizzo());
            d.setCitta(m.getCitta());
            d.setCapacitaMassima(m.getCapacitaMassima());
            d.setIsNoProfit(m.getIsNoProfit());
            dtos.add(d);
        }
        return dtos;
    }
}