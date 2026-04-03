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

    // Simuliamo le dipendenze per non toccare il database reale o i mapper complessi
    @Mock
    private CentroAdozioneRepository repository;

    @Mock
    private CentroAdozioneMapper mapper;

    // Iniettiamo i mock creati sopra all'interno del servizio reale
    @InjectMocks
    private CentroAdozioneService service;

    /**
     * TEST: Recupero di tutti i centri
     * Verifica che il service estragga i dati dal repository e li converta in DTO.
     */
    @Test
    public void testFindAllCentri() {
        // GIVEN: Prepariamo un'entità finta e un DTO finto
        CentroAdozione entity = new CentroAdozione();
        CentroAdozioneDto dto = new CentroAdozioneDto();

        // Istruiamo i mock: "Quando il repo cerca tutti, restituisci l'entità; quando il mapper la vede, restituisci il DTO"
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDTO(any(CentroAdozione.class))).thenReturn(dto);

        // WHEN: Chiamiamo il metodo del servizio
        List<CentroAdozioneDto> result = service.listaTuttiICentri();

        // THEN: Verifichiamo che la lista non sia vuota e che il repository sia stato interrogato
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(repository).findAll();
    }

    /**
     * TEST: Ricerca per città
     * Valida la logica di filtraggio geografico dei centri.
     */
    @Test
    public void testFindByCitta() {
        // GIVEN: Definiamo una città di test
        String citta = "Roma";
        CentroAdozione entity = new CentroAdozione();
        CentroAdozioneDto dto = new CentroAdozioneDto();

        when(repository.findByCitta(citta)).thenReturn(List.of(entity));
        when(mapper.toDTO(any(CentroAdozione.class))).thenReturn(dto);

        // WHEN: Eseguiamo la ricerca
        List<CentroAdozioneDto> result = service.findByCitta(citta);

        // THEN: Il risultato deve essere presente e il repository deve aver ricevuto il parametro "Roma"
        assertNotNull(result);
        verify(repository).findByCitta(citta);
    }

    /**
     * TEST: Salvataggio di un nuovo centro
     * Questo è un test di flusso completo: Conversione -> Salvataggio -> Conversione.
     */
    @Test
    public void testSalvaNuovoCentro() {
        // GIVEN: Un DTO in ingresso e un'entità di supporto
        CentroAdozioneDto dtoInput = new CentroAdozioneDto();
        CentroAdozione entity = new CentroAdozione();

        // Simuliamo l'intero ciclo di vita dell'oggetto
        when(mapper.toEntity(any(CentroAdozioneDto.class))).thenReturn(entity);
        when(repository.save(any(CentroAdozione.class))).thenReturn(entity);
        when(mapper.toDTO(any(CentroAdozione.class))).thenReturn(dtoInput);

        // WHEN: Salviamo il nuovo centro
        CentroAdozioneDto result = service.salvaNuovo(dtoInput);

        // THEN: Verifichiamo che il processo sia andato a buon fine
        assertNotNull(result);
        verify(repository).save(any());
    }

    /**
     * TEST: Eliminazione di un centro
     * Verifica che il comando di cancellazione arrivi correttamente al database.
     */
    @Test
    public void testEliminaCentro() {
        // GIVEN: Un ID esistente
        Integer id = 1;

        // WHEN: Chiamiamo il metodo di eliminazione
        service.elimina(id);

        // THEN: Verifichiamo che il repository abbia ricevuto l'ordine di eliminare proprio quell'ID
        verify(repository, times(1)).deleteById(id);
    }
}