package it.adozioni.animali.VolontarioTest;

import it.adozioni.animali.Dto.VolontarioDto;
import it.adozioni.animali.Mapper.VolontarioMapper;
import it.adozioni.animali.Model.Volontario;
import it.adozioni.animali.Repository.VolontarioRepository;
import it.adozioni.animali.Service.VolontarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VolontarioServiceTest {

    @Mock
    private VolontarioRepository volontarioRepository;

    // Usiamo @Spy per far funzionare il ModelMapper reale contenuto nel tuo VolontarioMapper
    @Spy
    private VolontarioMapper volontarioMapper = new VolontarioMapper();

    @InjectMocks
    private VolontarioService volontarioService;

    // 🔹 TEST 1 - Cerca per CF
    @Test
    void testFindByCf() {
        // GIVEN
        Volontario v = new Volontario();
        v.setCf("ABC123");
        when(volontarioRepository.findByCf("ABC123")).thenReturn(v);

        // WHEN
        Volontario result = volontarioService.cercaPerCf("ABC123");

        // THEN
        assertNotNull(result);
        assertEquals("ABC123", result.getCf());
        verify(volontarioRepository).findByCf("ABC123");
    }

    // 🔹 TEST 2 - Cerca per nome
    @Test
    void testFindByNome() {
        // GIVEN
        Volontario v = new Volontario();
        v.setNome("Luca");
        when(volontarioRepository.findByNome("Luca")).thenReturn(List.of(v));

        // WHEN
        List<Volontario> result = volontarioService.cercaPerNome("Luca");

        // THEN
        assertFalse(result.isEmpty());
        assertEquals("Luca", result.get(0).getNome());
        verify(volontarioRepository).findByNome("Luca");
    }

    // 🔹 TEST 3 - Cerca per turno (Verifica conversione DTO)
    @Test
    void testFindByTurno() {
        // GIVEN
        String turno = "Mattina";
        Volontario v = new Volontario();
        v.setTurno(turno);
        v.setNome("Marco");
        v.setCf("MRC80L");

        when(volontarioRepository.findByTurno(turno)).thenReturn(List.of(v));

        // WHEN
        // Lo Spy userà il metodo toDTO reale del tuo VolontarioMapper
        List<VolontarioDto> result = volontarioService.findByTurnoDto(turno);

        // THEN
        assertNotNull(result);
        assertFalse(result.isEmpty(), "La lista non dovrebbe essere vuota");

        // Verifichiamo che il ModelMapper abbia fatto il suo lavoro
        VolontarioDto dto = result.get(0);
        assertNotNull(dto);

        verify(volontarioRepository).findByTurno(turno);
    }
}