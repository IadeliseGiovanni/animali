package it.adozioni.animali.Mapper;

import it.adozioni.animali.Dto.CentroAdozioneDto;
import it.adozioni.animali.Model.CentroAdozione;
import org.springframework.stereotype.Component;

@Component // Lo registriamo come componente così possiamo usarlo nel Service
public class CentroAdozioneMapper {

    // Da Model (Database) a DTO (Web)
    public CentroAdozioneDto toDto(CentroAdozione model) {
        if (model == null) return null;

        CentroAdozioneDto dto = new CentroAdozioneDto();
        dto.setNomeCentro(model.getNomeCentro());
        dto.setIndirizzo(model.getIndirizzo());
        dto.setCitta(model.getCitta());
        dto.setCapacitaMassima(model.getCapacitaMassima());
        dto.setIsNoProfit(model.getIsNoProfit());

        return dto;
    }

    // Da DTO (Web) a Model (Database) - Utile per i salvataggi
    public CentroAdozione toModel(CentroAdozioneDto dto) {
        if (dto == null) return null;

        CentroAdozione model = new CentroAdozione();
        model.setNomeCentro(dto.getNomeCentro());
        model.setIndirizzo(dto.getIndirizzo());
        model.setCitta(dto.getCitta());
        model.setCapacitaMassima(dto.getCapacitaMassima());
        model.setIsNoProfit(dto.getIsNoProfit());

        return model;
    }
}