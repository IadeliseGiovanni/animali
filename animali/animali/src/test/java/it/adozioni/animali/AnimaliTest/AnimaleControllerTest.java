package it.adozioni.animali.AnimaliTest;

import it.adozioni.animali.Controller.AnimaleController;
import it.adozioni.animali.Dto.AdozioneRequestDto;
import it.adozioni.animali.Dto.ResultDto;
import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Model.Animale;
import it.adozioni.animali.Service.AdottanteService;
import it.adozioni.animali.Service.AnimaleService;
import it.adozioni.animali.Service.DocumentoService;
import it.adozioni.animali.Service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnimaleControllerTest {

    @Mock
    private AnimaleService animaleService;

    @Mock
    private AdottanteService adottanteService;

    @Mock
    private DocumentoService documentoService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AnimaleController controller;

    // 🔹 TEST 1 - CASO OK
    @Test
    void testGeneraContratto_OK() throws Exception {
        // GIVEN
        AdozioneRequestDto dto = new AdozioneRequestDto();
        dto.setIdAnimale(1);
        dto.setIdAdottante(2);

        Animale animale = new Animale();
        animale.setNome("Fido");

        Adottante adottante = new Adottante();
        adottante.setEmail("test@email.com");

        byte[] pdf = new byte[]{1, 2, 3};

        when(animaleService.findByIdEntity(1)).thenReturn(animale);
        when(adottanteService.findByIdEntity(2)).thenReturn(adottante);
        when(documentoService.creaPdf(animale, adottante)).thenReturn(pdf);

        // WHEN
        ResponseEntity<?> response = controller.generaContratto(dto);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(pdf);

        verify(emailService).inviaContrattoConAllegato(
                adottante.getEmail(), animale.getNome(), pdf);
        verify(emailService).inviaNotificaRicezioneAlCentro(
                adottante.getEmail(), animale.getNome());
    }

    // 🔹 TEST 2 - CASO NOT FOUND
    @Test
    void testGeneraContratto_NotFound() {
        // GIVEN
        AdozioneRequestDto dto = new AdozioneRequestDto();
        dto.setIdAnimale(1);
        dto.setIdAdottante(2);

        when(animaleService.findByIdEntity(1)).thenReturn(null);

        // WHEN
        ResponseEntity<?> response = controller.generaContratto(dto);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(404);

        assertThat(response.getBody()).isInstanceOf(ResultDto.class);
        ResultDto<?> body = (ResultDto<?>) response.getBody();
        assertThat(body).isNotNull();
        // Corretta la stringa in base al tuo log
        assertThat(body.getMessage()).isEqualTo("Errore: Animale o Adottante non trovati nel database.");
        assertThat(body.isSuccess()).isFalse();
    }

    // 🔹 TEST 3 - CASO ERRORE (exception)
    @Test
    void testGeneraContratto_Exception() throws Exception {
        // GIVEN
        AdozioneRequestDto dto = new AdozioneRequestDto();
        dto.setIdAnimale(1);
        dto.setIdAdottante(2);

        Animale animale = new Animale();
        Adottante adottante = new Adottante();

        when(animaleService.findByIdEntity(1)).thenReturn(animale);
        when(adottanteService.findByIdEntity(2)).thenReturn(adottante);

        when(documentoService.creaPdf(animale, adottante))
                .thenThrow(new RuntimeException("Errore PDF"));

        // WHEN
        ResponseEntity<?> response = controller.generaContratto(dto);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(500);

        assertThat(response.getBody()).isInstanceOf(ResultDto.class);
        ResultDto<?> body = (ResultDto<?>) response.getBody();
        assertThat(body).isNotNull();
        // Corretta la stringa in base al tuo log (che concatena il messaggio dell'eccezione)
        assertThat(body.getMessage()).isEqualTo("Errore critico durante il processo: Errore PDF");
        assertThat(body.isSuccess()).isFalse();
    }
}