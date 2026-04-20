package it.adozioni.animali.Mapper;

import it.adozioni.animali.Model.Animale;
import it.adozioni.animali.Dto.AnimaleDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AnimaleMapper extends AbstractConverter<Animale, AnimaleDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public Animale toEntity(AnimaleDto dto) {
        return mapper.map(dto, Animale.class);
    }

    @Override
    public AnimaleDto toDTO(Animale entity) {
        // Mappatura automatica standard
        AnimaleDto dto = mapper.map(entity, AnimaleDto.class);

        // --- FIX MANUALE: Assicuriamoci che il video passi sempre ---
        if (entity.getVideoUrl() != null) {
            dto.setVideoUrl(entity.getVideoUrl());
        }

        return dto;
    }
}