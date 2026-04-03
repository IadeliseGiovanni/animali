package it.adozioni.animali.Mapper;


import org.springframework.stereotype.Component;

import java.util.List;
// converte i dati ed è un mapper generalizzato quindi lo fa in maniera generale.

public interface Converter<Entity, DTO> {


    public Entity toEntity(DTO dto);

    public DTO toDTO(Entity entity);

    public List<DTO> toDTOList(Iterable<Entity> entityList);
    public List<Entity> toEntityList(Iterable<DTO> entityList);

}