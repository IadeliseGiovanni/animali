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
    void testFindByCitta_Success() {
        String citta = "Roma";
        List<CentroAdozioneDto> mockLista = List.of(new CentroAdozioneDto());
        when(service.findByCitta(citta)).thenReturn(mockLista);

        List<CentroAdozioneDto> result = controller.findByCitta(citta);

        assertThat(result).hasSize(1);
        verify(service).findByCitta(citta);
    }

    // 🔹 Test città senza centri
    @Test
    void testFindByCitta_NotFound_ReturnsEmptyList() {
        String citta = "CittaInesistente";
        when(service.findByCitta(citta)).thenReturn(Collections.emptyList());

        List<CentroAdozioneDto> result = controller.findByCitta(citta);

        assertThat(result).isEmpty();
        verify(service).findByCitta(citta);
    }

    @Test
    void testFindByIsNoProfit() {
        boolean noProfit = true;
        List<CentroAdozioneDto> mockLista = List.of(new CentroAdozioneDto(), new CentroAdozioneDto());
        when(service.findByIsNoProfit(noProfit)).thenReturn(mockLista);

        List<CentroAdozioneDto> result = controller.findByIsNoProfit(noProfit);

        assertThat(result).hasSize(2);
        verify(service).findByIsNoProfit(noProfit);
    }

    @Test
    void testFindByNomeCentro_Success() {
        String nome = "Rifugio Speranza";
        CentroAdozioneDto mockDto = new CentroAdozioneDto();
        when(service.findByNomeCentro(nome)).thenReturn(mockDto);

        CentroAdozioneDto result = controller.findByNomeCentro(nome);

        assertThat(result).isNotNull();
        verify(service).findByNomeCentro(nome);
    }

    // 🔹 Test nome centro inesistente
    @Test
    void testFindByNomeCentro_NotFound_ReturnsNull() {
        String nome = "NomeFantasia";
        when(service.findByNomeCentro(nome)).thenReturn(null);

        CentroAdozioneDto result = controller.findByNomeCentro(nome);

        assertThat(result).isNull();
        verify(service).findByNomeCentro(nome);
    }

    // 🔹 Test con parametro null (Robustezza)
    @Test
    void testFindByCitta_NullParam() {
        when(service.findByCitta(null)).thenReturn(Collections.emptyList());

        List<CentroAdozioneDto> result = controller.findByCitta(null);

        assertThat(result).isEmpty();
        verify(service).findByCitta(null);
    }
}