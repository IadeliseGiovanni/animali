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

    @Mock
    private AdottanteRepository adottanteRepository;

    @InjectMocks
    private AdottanteService adottanteService;

    @BeforeEach
    void setUp() {
        // Inizializza i mock prima di ogni test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByIdEntity_Success() {
        // GIVEN: Prepariamo un oggetto finto (Mock)
        Adottante mockAdottante = new Adottante();
        mockAdottante.setId(5);
        mockAdottante.setNome("Mario");
        mockAdottante.setCognome("Rossi");

        // Simuliamo il comportamento del DB
        when(adottanteRepository.findById(5)).thenReturn(Optional.of(mockAdottante));

        // WHEN: Eseguiamo il metodo da testare
        Adottante result = adottanteService.findByIdEntity(5);

        // THEN: Verifichiamo che il risultato sia corretto
        assertNotNull(result);
        assertEquals("Mario", result.getNome());
        assertEquals("Rossi", result.getCognome());

        // Verifichiamo che il repository sia stato chiamato esattamente una volta
        verify(adottanteRepository, times(1)).findById(5);
    }
}