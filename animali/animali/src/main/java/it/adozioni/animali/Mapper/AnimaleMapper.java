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
        // 1. Mappatura automatica standard per i campi semplici (id, nome, specie...)
        AnimaleDto dto = mapper.map(entity, AnimaleDto.class);

        // 2. --- FIX MANUALE VIDEO ---
        // Assicuriamoci che l'URL del video passi sempre correttamente dal Model al DTO
        if (entity.getVideoUrl() != null) {
            dto.setVideoUrl(entity.getVideoUrl());
        }

        // 3. --- FIX MANUALE FOTO ---
        // Questo risolve il problema delle immagini dei gatti (Shadow, Nuvola, ecc.)
        // Se nel DB c'è un link nella colonna foto_url, lo forziamo nel DTO
        if (entity.getFotoUrl() != null) {
            dto.setFotoUrl(entity.getFotoUrl());
        }

        return dto;
    }
}