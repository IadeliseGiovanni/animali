package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.VisitaMedicaDto;
import it.adozioni.animali.Mapper.AbstractConverter;
import it.adozioni.animali.Mapper.Converter;
import it.adozioni.animali.Mapper.VisitaMedicaMapper;
import it.adozioni.animali.Model.VisitaMedica;
import it.adozioni.animali.Repository.VisitaMedicaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VisitaMedicaService extends AbstractService<VisitaMedica, VisitaMedicaDto> {

    private final VisitaMedicaRepository visitaMedicaRepository;

    private final VisitaMedicaMapper visitaMedicaMapper;

    protected VisitaMedicaService(JpaRepository<VisitaMedica, Integer> repository, Converter<VisitaMedica, VisitaMedicaDto> converter, VisitaMedicaMapper visitaMedicaMapper, VisitaMedicaRepository visitaMedicaRepository) {
        super(repository, converter);
        this.visitaMedicaMapper = visitaMedicaMapper;
        this.visitaMedicaRepository = visitaMedicaRepository;
    }

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
