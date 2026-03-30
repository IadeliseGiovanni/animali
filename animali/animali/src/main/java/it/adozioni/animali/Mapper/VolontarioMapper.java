package it.adozioni.animali.Mapper;

import it.adozioni.animali.Model.Volontario;
import it.adozioni.animali.Dto.VolontarioDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VolontarioMapper {

    // 🔹 Entity → DTO
    public VolontarioDto toDTO(Volontario entity) {
        if (entity == null) return null;

        VolontarioDto dto = new VolontarioDto();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setCognome(entity.getCognome());
        dto.setCf(entity.getCf());
        dto.setTurno(entity.getTurno());

        return dto;
    }

    // 🔹 DTO → Entity
    public Volontario toEntity(VolontarioDto dto) {
        if (dto == null) return null;

        Volontario entity = new Volontario();
        entity.setId(dto.getId());
        entity.setNome(dto.getNome());
        entity.setCognome(dto.getCognome());
        entity.setCf(dto.getCf());
        entity.setTurno(dto.getTurno());

        return entity;
    }

    // 🔹 Lista Entity → Lista DTO
    public List<VolontarioDto> toDTOList(List<Volontario> list) {
        return list.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Lista DTO → Lista Entity
    public List<Volontario> toEntityList(List<VolontarioDto> list) {
        return list.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
