package it.adozioni.animali.AdottanteTest;

import it.adozioni.animali.Dto.AdottanteDto;
import it.adozioni.animali.Service.AdottanteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TEST DI INTEGRAZIONE: AdottanteController
 * Utilizziamo MockMvc per simulare le chiamate HTTP esterne (come farebbe un browser o Postman).
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Disabilitiamo i filtri di sicurezza per testare solo la logica dell'endpoint
public class AdottanteControllerTest {

    @Autowired // istanziando dipendenza
    private MockMvc mockMvc; // Simula il client che effettua le chiamate API

    @MockBean
    private AdottanteService adottanteService; // Mock del servizio per isolare il controller

    /**
     * TEST: Recupero Adottante tramite ID tramite richiesta GET
     * Verifica che l'endpoint /Adottante/read risponda correttamente con i dati in formato JSON.
     */
    @Test
    @WithMockUser // Simula un utente autenticato nel sistema
    void testGetAdottanteById_StatusOk() throws Exception {

        // GIVEN: Prepariamo un DTO finto che il servizio dovrà restituire
        AdottanteDto mockDto = new AdottanteDto();
        mockDto.setId(5);
        mockDto.setNome("Mario");

        // Definiamo il comportamento del mock: quando chiedi l'ID 5, restituisci il DTO
        when(adottanteService.read(5)).thenReturn(mockDto);

        // WHEN & THEN: Eseguiamo la chiamata GET e verifichiamo i risultati
        mockMvc.perform(get("/Adottante/read")
                        .param("id", "5") // Passiamo l'ID come parametro della query
                        .contentType(MediaType.APPLICATION_JSON)) // Specifichiamo che ci aspettiamo un JSON

                // Verifichiamo che il server risponda con Status 200 (OK)
                .andExpect(status().isOk())

                // Validiamo il contenuto del JSON restituito usando JsonPath
                .andExpect(jsonPath("$.nome").value("Mario"))
                .andExpect(jsonPath("$.id").value(5));
    }
}