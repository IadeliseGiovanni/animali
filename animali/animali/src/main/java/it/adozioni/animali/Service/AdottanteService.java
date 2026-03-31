package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.AdottanteDto;
import it.adozioni.animali.Mapper.AdottanteMapper;
import it.adozioni.animali.Mapper.Converter;
import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Repository.AdottanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;



@Service

@Autowired
public class AdottanteService extends AbstractService<Adottante, AdottanteDto> {

    private final AdottanteRepository adottanteRepository;
    private final AdottanteMapper adottanteMapper;

    protected AdottanteService(JpaRepository<Adottante, Integer> repository,
                               Converter<Adottante, AdottanteDto> converter,
                               AdottanteMapper adottanteMapper,
                               AdottanteRepository adottanteRepository) {

        super(repository, converter);
        this.adottanteRepository = adottanteRepository;
        this.adottanteMapper = adottanteMapper;
    }

    public List<AdottanteDto> findByCognome(String cognome) {
        return adottanteMapper.toDTOList(adottanteRepository.findByCognome(cognome));
    }

}