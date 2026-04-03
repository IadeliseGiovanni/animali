package it.adozioni.animali.CentroAdozioneTest;

import it.adozioni.animali.Dto.CentroAdozioneDto;
import it.adozioni.animali.Mapper.CentroAdozioneMapper;
import it.adozioni.animali.Model.CentroAdozione;
import it.adozioni.animali.Repository.CentroAdozioneRepository;
import it.adozioni.animali.Service.CentroAdozioneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CentroAdozioneServiceTest {

    @Mock
    private CentroAdozioneRepository repository;

    @Mock
    private CentroAdozioneMapper mapper;

    @InjectMocks
    private CentroAdozioneService service;

    @Test
    public void testFindAllCentri() {
        // GIVEN
        CentroAdozione entity = new CentroAdozione();
        CentroAdozioneDto dto = new CentroAdozioneDto();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDTO(any(CentroAdozione.class))).thenReturn(dto);

        // WHEN - Usiamo il nome reale del tuo metodo
        List<CentroAdozioneDto> result = service.listaTuttiICentri();

        // THEN
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(repository).findAll();
    }

    @Test
    public void testFindByCitta() {
        // GIVEN
        String citta = "Roma";
        CentroAdozione entity = new CentroAdozione();
        CentroAdozioneDto dto = new CentroAdozioneDto();

        when(repository.findByCitta(citta)).thenReturn(List.of(entity));
        when(mapper.toDTO(any(CentroAdozione.class))).thenReturn(dto);

        // WHEN
        List<CentroAdozioneDto> result = service.findByCitta(citta);

        // THEN
        assertNotNull(result);
        verify(repository).findByCitta(citta);
    }

    @Test
    public void testSalvaNuovoCentro() {
        // GIVEN
        CentroAdozioneDto dtoInput = new CentroAdozioneDto();
        CentroAdozione entity = new CentroAdozione();

        when(mapper.toEntity(any(CentroAdozioneDto.class))).thenReturn(entity);
        when(repository.save(any(CentroAdozione.class))).thenReturn(entity);
        when(mapper.toDTO(any(CentroAdozione.class))).thenReturn(dtoInput);

        // WHEN
        CentroAdozioneDto result = service.salvaNuovo(dtoInput);

        // THEN
        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    public void testEliminaCentro() {
        // GIVEN
        Integer id = 1;

        // WHEN - Cambiato da delete() a elimina() per riflettere il tuo Service
        service.elimina(id);

        // THEN
        verify(repository, times(1)).deleteById(id);
    }
}