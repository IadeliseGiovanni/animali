package it.adozioni.animali.Mapper;

import it.adozioni.animali.Dto.CentroAdozioneDto;
import it.adozioni.animali.Dto.VisitaMedicaDto;
import it.adozioni.animali.Model.VisitaMedica;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class VisitaMedicaMapper extends AbstractConverter<VisitaMedica, VisitaMedicaDto> {

    final private ModelMapper mapper = new ModelMapper();

    @Override
    public VisitaMedicaDto toDTO(VisitaMedica entity) {
        return mapper.map(entity, VisitaMedicaDto.class);
    }

    @Override
    public VisitaMedica toEntity(VisitaMedicaDto dto) {
        return mapper.map(dto, VisitaMedica.class);
    }
}

