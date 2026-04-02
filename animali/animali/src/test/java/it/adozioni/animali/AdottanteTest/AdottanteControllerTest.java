package it.adozioni.animali.AdottanteTest;

import it.adozioni.animali.Dto.AdottanteDto; // Importa il DTO
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

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AdottanteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdottanteService adottanteService;

    @Test
    @WithMockUser
    void testGetAdottanteById_StatusOk() throws Exception {
        // GIVEN
        // Creiamo un AdottanteDto invece di Adottante (Model)
        AdottanteDto mockDto = new AdottanteDto();
        mockDto.setId(5);
        mockDto.setNome("Mario");

        // Adesso i tipi corrispondono: read(5) restituisce un AdottanteDto
        when(adottanteService.read(5)).thenReturn(mockDto);

        // WHEN & THEN
        mockMvc.perform(get("/Adottante/read")
                        .param("id", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Mario"))
                .andExpect(jsonPath("$.id").value(5));
    }
}