package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.AnimaleDto;
import it.adozioni.animali.Mapper.AnimaleMapper;
import it.adozioni.animali.Model.Animale;
import it.adozioni.animali.Repository.AnimaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimaleService extends AbstractService<Animale, AnimaleDto> {

    private final AnimaleRepository animaleRepository;
    private final AnimaleMapper animaleMapper;

    @Autowired
    public AnimaleService(AnimaleRepository animaleRepository, AnimaleMapper animaleMapper) {
        super(animaleRepository, animaleMapper);
        this.animaleRepository = animaleRepository;
        this.animaleMapper = animaleMapper;
    }

    public Animale findByIdEntity(Integer id) {
        if (id == null) return null;
        return animaleRepository.findById(id).orElse(null);
    }

    public List<AnimaleDto> findByNome(String nome) {
        return animaleMapper.toDTOList(animaleRepository.findByNome(nome));
    }

    public List<AnimaleDto> findAllDisponibili() {
        // Recupera le entità e le trasforma in DTO tramite il mapper
        return animaleMapper.toDTOList(animaleRepository.findByAdottatoFalse());
    }

    @Override
    public List<AnimaleDto> findAll() {
        return animaleMapper.toDTOList(animaleRepository.findAll());
    }

    public List<AnimaleDto> filterAnimali(String specie, String genere, Long centroId) {
        String s = (specie != null && !specie.isEmpty()) ? specie : null;
        String g = (genere != null && !genere.isEmpty()) ? genere : null;

        List<Animale> lista = animaleRepository.findFiltered(s, g, centroId);

        // Filtriamo solo quelli non adottati e mappiamo a DTO
        return animaleMapper.toDTOList(
                lista.stream().filter(a -> !a.isAdottato()).collect(Collectors.toList())
        );
    }

    public List<AnimaleDto> getAnimaliByCentro(Long centroId) {
        return animaleMapper.toDTOList(animaleRepository.findByCentroAdozioneId(centroId));
    }
}