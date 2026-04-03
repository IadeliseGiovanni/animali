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

    @Test
    void testCercaComeUser_Success() {
        String citta = "Roma";
        List<CentroAdozioneDto> mockLista = List.of(new CentroAdozioneDto());
        when(service.findByCitta(citta)).thenReturn(mockLista);

        // Cambiato da findByCitta a cercaComeUser
        String result = controller.cercaComeUser(citta);

        assertThat(result).contains("--- SONO USER ---");
        assertThat(result).contains(citta);
        verify(service).findByCitta(citta);
    }

    @Test
    void testVisualizzaComeUser_Success() {
        when(service.listaTuttiICentri()).thenReturn(Collections.emptyList());

        // Cambiato da getAllCentri a visualizzaComeUser
        String result = controller.visualizzaComeUser();

        assertThat(result).contains("--- SONO USER ---");
        verify(service).listaTuttiICentri();
    }

    @Test
    void testCreaComeAdmin_Success() {
        CentroAdozioneDto dto = new CentroAdozioneDto();
        dto.setNomeCentro("Rifugio Test");
        when(service.salvaNuovo(dto)).thenReturn(dto);

        // Cambiato da crea a creaComeAdmin
        String result = controller.creaComeAdmin(dto);

        assertThat(result).contains("--- SONO ADMIN ---");
        assertThat(result).contains("Rifugio Test");
        verify(service).salvaNuovo(dto);
    }
}