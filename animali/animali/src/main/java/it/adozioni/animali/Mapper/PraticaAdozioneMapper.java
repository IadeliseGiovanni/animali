package it.adozioni.animali.Mapper;

import it.adozioni.animali.Dto.PraticaAdozioneDto;
import it.adozioni.animali.Model.PraticaAdozione;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class PraticaAdozioneMapper extends AbstractConverter<PraticaAdozione, PraticaAdozioneDto> {

    private final ModelMapper mapper = new ModelMapper();

    @PostConstruct
    public void init() {
        // Configurazione per mappare correttamente i campi annidati nel DTO
        mapper.addMappings(new PropertyMap<PraticaAdozione, PraticaAdozioneDto>() {
            @Override
            protected void configure() {
                map().setAdottanteId(source.getAdottante().getId());
                map().setAnimaleId(source.getAnimale().getId());
                map().setAnimaleNome(source.getAnimale().getNome());
                // Uniamo nome e cognome per il frontend
                using(ctx -> {
                    PraticaAdozione p = (PraticaAdozione) ctx.getSource();
                    return p.getAdottante().getNome() + " " + p.getAdottante().getCognome();
                }).map(source, destination.getAdottanteNominativo());
            }
        });
    }

    @Override
    public PraticaAdozione toEntity(PraticaAdozioneDto dto) {
        return mapper.map(dto, PraticaAdozione.class);
    }

    @Override
    public PraticaAdozioneDto toDTO(PraticaAdozione entity) {
        return mapper.map(entity, PraticaAdozioneDto.class);
    }
}