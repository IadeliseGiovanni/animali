package it.adozioni.animali.Mapper;

import it.adozioni.animali.Dto.CentroAdozioneDto;
import it.adozioni.animali.Model.CentroAdozione;
import org.springframework.stereotype.Component;

@Component
public class CentroAdozioneMapper extends AbstractConverter<CentroAdozione, CentroAdozioneDto> {

    @Override
    public CentroAdozioneDto toDto(CentroAdozione entity) {
        if (entity == null) return null;
        CentroAdozioneDto dto = new CentroAdozioneDto();
        dto.setNomeCentro(entity.getNomeCentro());
        dto.setIndirizzo(entity.getIndirizzo());
        dto.setCitta(entity.getCitta());
        dto.setCapacitaMassima(entity.getCapacitaMassima());
        dto.setIsNoProfit(entity.getIsNoProfit());
        return dto;
    }

    @Override
    public CentroAdozione toEntity(CentroAdozioneDto dto) {
        if (dto == null) return null;
        CentroAdozione entity = new CentroAdozione();
        entity.setNomeCentro(dto.getNomeCentro());
        entity.setIndirizzo(dto.getIndirizzo());
        entity.setCitta(dto.getCitta());
        entity.setCapacitaMassima(dto.getCapacitaMassima());
        entity.setIsNoProfit(dto.getIsNoProfit());
        return entity;
    }

    @Override
    public CentroAdozioneDto toDTO(CentroAdozione centroAdozione) {
        return null;
    }
}