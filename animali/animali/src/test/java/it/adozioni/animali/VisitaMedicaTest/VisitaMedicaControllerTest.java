package it.adozioni.animali.VisitaMedicaTest;

import it.adozioni.animali.Controller.VisitaMedicaController;
import it.adozioni.animali.Dto.VisitaMedicaDto;
import it.adozioni.animali.Service.VisitaMedicaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class VisitaMedicaControllerTest {

    @Mock
    private VisitaMedicaService service;

    @InjectMocks
    private VisitaMedicaController controller;

    @Test
    void testFindByData() {
        // GIVEN
        LocalDateTime data = LocalDateTime.of(2026, 3, 30, 10, 0);
        List<VisitaMedicaDto> mockLista = List.of(new VisitaMedicaDto());
        when(service.findByData(data)).thenReturn(mockLista);

        // WHEN
        List<VisitaMedicaDto> result = controller.findByData(data);

        // THEN
        assertThat(result).hasSize(1);
        verify(service).findByData(data);
    }

    @Test
    void testFindByEsito() {
        // GIVEN
        String esito = "Regolare";
        List<VisitaMedicaDto> mockLista = List.of(new VisitaMedicaDto());
        when(service.findByEsito(esito)).thenReturn(mockLista);

        // WHEN
        List<VisitaMedicaDto> result = controller.findByEsito(esito);

        // THEN
        assertThat(result).isEqualTo(mockLista);
        verify(service).findByEsito(esito);
    }

    @Test
    void testFindByVeterinario() {
        // GIVEN
        String vet = "Dr. Bianchi";
        List<VisitaMedicaDto> mockLista = List.of(new VisitaMedicaDto());
        when(service.findByVeterinario(vet)).thenReturn(mockLista);

        // WHEN
        List<VisitaMedicaDto> result = controller.findByVeterinario(vet);

        // THEN
        assertThat(result).containsExactlyElementsOf(mockLista);
        verify(service).findByVeterinario(vet);
    }

    @Test
    void testFindByDataAndVeterinario() {
        // GIVEN
        LocalDateTime data = LocalDateTime.now();
        String vet = "Dr. Rossi";
        List<VisitaMedicaDto> mockLista = List.of(new VisitaMedicaDto());
        when(service.findByDataAndVeterinario(data, vet)).thenReturn(mockLista);

        // WHEN
        List<VisitaMedicaDto> result = controller.findByDataAndVeterinario(data, vet);

        // THEN
        assertThat(result).hasSize(1);
        verify(service).findByDataAndVeterinario(data, vet);
    }

    @Test
    void testFindByDataAndEsito() {
        // GIVEN
        LocalDateTime data = LocalDateTime.now();
        String esito = "In attesa";
        List<VisitaMedicaDto> mockLista = List.of(new VisitaMedicaDto(), new VisitaMedicaDto());
        when(service.findByDataAndEsito(data, esito)).thenReturn(mockLista);

        // WHEN
        List<VisitaMedicaDto> result = controller.findByDataAndEsito(data, esito);

        // THEN
        assertThat(result).hasSize(2);
        verify(service).findByDataAndEsito(data, esito);
    }
}
