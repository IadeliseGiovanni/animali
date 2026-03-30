package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.CentroAdozioneDto;
import it.adozioni.animali.Mapper.CentroAdozioneMapper;
import it.adozioni.animali.Model.CentroAdozione;
import it.adozioni.animali.Repository.CentroAdozioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CentroAdozioneService extends AbstractService<CentroAdozione, CentroAdozioneDto> {

    @Autowired
    public CentroAdozioneService(CentroAdozioneRepository repository, CentroAdozioneMapper mapper) {
        super(repository, mapper);
    }

    // Metodo extra se vuoi chiamarlo specificamente dal Controller
    public Iterable<CentroAdozioneDto> listaTuttiICentri() {
        return getAll();
    }

    public Object findByCitta(String citta) {
    }

    public Object findByIsNoProfit(boolean noProfit) {
    }

    public Object findByNomeCentro(String nome) {
    }

    public List<CentroAdozione> findAll() {
    }

    public CentroAdozione findById(Integer id) {
    }
}