package it.adozioni.animali.AdottanteTest;

import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Repository.AdottanteRepository;
import it.adozioni.animali.Service.AdottanteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdottanteServiceTest {

    // Creiamo un mock del repository per simulare il database senza occupare risorse reali
    @Mock
    private AdottanteRepository adottanteRepository;

    // Iniettiamo il mock del repository all'interno del servizio che vogliamo testare
    @InjectMocks
    private AdottanteService adottanteService;

    /**
     * Inizializzazione: Prima di ogni test, "apriamo" i mock
     * per assicurarci che le dipendenze siano pronte.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * TEST: Recupero entità Adottante per ID (Successo)
     * Verifica che il service restituisca correttamente l'oggetto se presente nel DB.
     */
    @Test
    void testFindByIdEntity_Success() {
        // GIVEN: Costruiamo un oggetto Adottante di esempio (Mock Data)
        Adottante mockAdottante = new Adottante();
        mockAdottante.setId(5);
        mockAdottante.setNome("Mario");
        mockAdottante.setCognome("Rossi");

        // Simulazione: Quando viene cercato l'ID 5, restituisci l'oggetto creato sopra
        when(adottanteRepository.findById(5)).thenReturn(Optional.of(mockAdottante));

        // WHEN: Eseguiamo il metodo del service che stiamo testando
        Adottante result = adottanteService.findByIdEntity(5);

        // THEN: Validazione dei risultati
        // 1. Verifichiamo che l'oggetto non sia nullo
        assertNotNull(result);
        // 2. Controlliamo che i dati corrispondano a quelli simulati
        assertEquals("Mario", result.getNome());
        assertEquals("Rossi", result.getCognome());

        // 3. Verifica comportamentale: ci assicuriamo che il repository sia stato interrogato
        // una sola volta e con l'ID corretto (fondamentale per evitare chiamate inutili al DB)
        verify(adottanteRepository, times(1)).findById(5);
    }
}