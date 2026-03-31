package it.adozioni.animali.Mapper;

import it.adozioni.animali.Dto.CentroAdozioneDto;
import it.adozioni.animali.Model.CentroAdozione;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CentroAdozioneMapper extends AbstractConverter<CentroAdozione, CentroAdozioneDto> {

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public CentroAdozione toEntity(CentroAdozioneDto dto) {
        if (dto == null) return null;
        return modelMapper.map(dto, CentroAdozione.class);
    }

    @Override
    public CentroAdozioneDto toDTO(CentroAdozione entity) {
        if (entity == null) return null;
        return modelMapper.map(entity, CentroAdozioneDto.class);
    }

    // 🔹 IMPORTANTE: Non restituire null, ma usa il modelMapper!
    // Restituiamo CentroAdozioneDto invece di Object per aiutare il Service
    public CentroAdozioneDto toDto(CentroAdozione entity) {
        return toDTO(entity);
    }
}