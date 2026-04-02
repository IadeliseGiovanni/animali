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
    void testFindByCitta() {
        // GIVEN
        String citta = "Roma";
        List<CentroAdozioneDto> mockLista = List.of(new CentroAdozioneDto());
        when(service.findByCitta(citta)).thenReturn(mockLista);

        // WHEN
        List<CentroAdozioneDto> result = controller.findByCitta(citta);

        // THEN
        assertThat(result).hasSize(1);
        verify(service).findByCitta(citta);
    }

    @Test
    void testFindByIsNoProfit() {
        // GIVEN
        boolean noProfit = true;
        List<CentroAdozioneDto> mockLista = List.of(new CentroAdozioneDto(), new CentroAdozioneDto());
        when(service.findByIsNoProfit(noProfit)).thenReturn(mockLista);

        // WHEN
        List<CentroAdozioneDto> result = controller.findByIsNoProfit(noProfit);

        // THEN
        assertThat(result).hasSize(2);
        verify(service).findByIsNoProfit(noProfit);
    }

    @Test
    void testFindByNomeCentro() {
        // GIVEN
        String nome = "Rifugio Speranza";
        CentroAdozioneDto mockDto = new CentroAdozioneDto();
        when(service.findByNomeCentro(nome)).thenReturn(mockDto);

        // WHEN
        CentroAdozioneDto result = controller.findByNomeCentro(nome);

        // THEN
        assertThat(result).isNotNull();
        verify(service).findByNomeCentro(nome);
    }
}