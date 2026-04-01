package it.adozioni.animali.Mapper;

import it.adozioni.animali.Dto.CentroAdozioneDto;
import it.adozioni.animali.Model.Volontario;
import it.adozioni.animali.Dto.VolontarioDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class VolontarioMapper extends AbstractConverter<Volontario, VolontarioDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public VolontarioDto toDTO(Volontario entity) {
        return mapper.map(entity, VolontarioDto.class);
    }

    @Override
    public Volontario toEntity(VolontarioDto dto) {
        return mapper.map(dto, Volontario.class);
    }
}