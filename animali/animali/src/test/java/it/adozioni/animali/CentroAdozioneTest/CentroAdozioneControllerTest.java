package it.adozioni.animali.CentroAdozioneTest;

import it.adozioni.animali.Controller.CentroAdozioneController;
import it.adozioni.animali.Dto.CentroAdozioneDto;
import it.adozioni.animali.Service.CentroAdozioneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CentroAdozioneControllerTest {

    @Mock
    private CentroAdozioneService service;

    @InjectMocks
    private CentroAdozioneController controller;

    /**
     * TEST: Ricerca per città (Lato Utente)
     * Verifica che, fornendo una città, il controller interroghi il service
     * e restituisca una stringa formattata correttamente con il prefisso "USER".
     */
    @Test
    void testCercaComeUser_Success() {
        // GIVEN: Configurazione dei dati di input e del mock
        String citta = "Roma";
        List<CentroAdozioneDto> mockLista = List.of(new CentroAdozioneDto());
        when(service.findByCitta(citta)).thenReturn(mockLista);

        // WHEN: Esecuzione della chiamata al controller
        String result = controller.cercaComeUser(citta);

        // THEN: Verifiche sul risultato (Contenuto della stringa e chiamata al service)
        assertThat(result).contains("--- SONO USER ---");
        assertThat(result).contains(citta);
        verify(service).findByCitta(citta);
    }

    /**
     * TEST: Visualizzazione lista completa (Lato Utente)
     * Verifica che la richiesta della lista totale dei centri richiami il metodo
     * corretto del service e mantenga la formattazione prevista per l'utente.
     */
    @Test
    void testVisualizzaComeUser_Success() {
        // GIVEN: Simuliamo un database che restituisce una lista vuota
        when(service.listaTuttiICentri()).thenReturn(Collections.emptyList());

        // WHEN: Chiamata al metodo di visualizzazione
        String result = controller.visualizzaComeUser();

        // THEN: Verifichiamo che l'intestazione sia corretta e il service sia stato consultato
        assertThat(result).contains("--- SONO USER ---");
        verify(service).listaTuttiICentri();
    }

    /**
     * TEST: Creazione nuovo centro (Lato Admin)
     * Valida il flusso di salvataggio di un nuovo centro, assicurandosi che
     * il controller utilizzi il prefisso "ADMIN" per identificare l'operazione privilegiata.
     */
    @Test
    void testCreaComeAdmin_Success() {
        // GIVEN: Creazione di un DTO di esempio
        CentroAdozioneDto dto = new CentroAdozioneDto();
        dto.setNomeCentro("Rifugio Test");
        when(service.salvaNuovo(dto)).thenReturn(dto);

        // WHEN: Invio del DTO al controller per il salvataggio
        String result = controller.creaComeAdmin(dto);

        // THEN: Verifica che la risposta confermi il ruolo Admin e riporti il nome del centro
        assertThat(result).contains("--- SONO ADMIN ---");
        assertThat(result).contains("Rifugio Test");
        verify(service).salvaNuovo(dto);
    }
}