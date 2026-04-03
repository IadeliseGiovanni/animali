package it.adozioni.animali.Service;

import it.adozioni.animali.Mapper.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class AbstractService<ENTITY,DTO> implements ServiceDTO<DTO> {
//metodi CRUD Generalizzati

    protected JpaRepository<ENTITY,Integer> repository;
    protected Converter<ENTITY,DTO> converter;

    protected AbstractService(JpaRepository<ENTITY, Integer> repository,
                              Converter<ENTITY, DTO> converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Override
    public DTO insert(DTO dto) {
        return converter.toDTO(repository.save(converter.toEntity(dto)));
    }

    @Override
    public Iterable<DTO> getAll() {
        return converter.toDTOList(repository.findAll());
    }

    @Override
    public DTO read(Integer id) {
        return converter.toDTO(repository.findById(id).get());
    }

    @Override
    public DTO update(DTO dto) {
        return converter.toDTO(repository.save(converter.toEntity(dto)));
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
