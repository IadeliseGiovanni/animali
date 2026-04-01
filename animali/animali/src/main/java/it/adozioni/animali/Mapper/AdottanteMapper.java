package it.adozioni.animali.Mapper;

import it.adozioni.animali.Dto.AdottanteDto;
import it.adozioni.animali.Model.Adottante;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AdottanteMapper extends AbstractConverter<Adottante, AdottanteDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public AdottanteDto toDTO(Adottante entity) {
        if (entity == null) return null;
        AdottanteDto dto = mapper.map(entity, AdottanteDto.class);
        // Opzionale: per sicurezza non restituiamo mai la password nel DTO verso il client
        dto.setPassword(null);
        return dto;
    }

    @Override
    public Adottante toEntity(AdottanteDto dto) {
        if (dto == null) return null;
        return mapper.map(dto, Adottante.class);
    }
}