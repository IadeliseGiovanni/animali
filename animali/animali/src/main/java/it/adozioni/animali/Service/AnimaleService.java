package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.AnimaleDto;
import it.adozioni.animali.Mapper.AnimaleMapper;
import it.adozioni.animali.Mapper.Converter;
import it.adozioni.animali.Model.Animale;
import it.adozioni.animali.Repository.AnimaleRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public class AnimaleService extends AbstractService<Animale, AnimaleDto>{
     final private AnimaleMapper animaleMapper;
     final private AnimaleRepository animaleRepository;
    protected AnimaleService(JpaRepository<Animale, Integer> repository, Converter<Animale, AnimaleDto> converter, AnimaleMapper animaleMapper, AnimaleRepository animaleRepository) {
        super(repository, converter);

        this.animaleMapper = animaleMapper;
        this.animaleRepository = animaleRepository;
    }
}
