package it.adozioni.animali.Mapper;
import it.adozioni.animali.Model.Animale;
import it.adozioni.animali.Dto.AnimaleDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AnimaleMapper extends AbstractConverter<Animale,AnimaleDto> {
    final private ModelMapper mapper = new ModelMapper();


    @Override
    public Animale toEntity(AnimaleDto dto) {
        return mapper.map(dto,Animale.class);
    }

    @Override
    public AnimaleDto toDTO(Animale entity) {
        return mapper.map(entity,AnimaleDto.class);
    }
}
//commento per commit