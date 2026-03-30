package it.adozioni.animali.CentroAdozioneTest;

import it.adozioni.animali.Model.CentroAdozione;
import it.adozioni.animali.Repository.CentroAdozioneRepository;
import it.adozioni.animali.Service.CentroAdozioneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CentroAdozioneServiceTest {

    @Mock
    private CentroAdozioneRepository repository;

    @InjectMocks
    private CentroAdozioneService service;

    @Test
    void testFindById() {
        // GIVEN
        Integer id = 1;
        CentroAdozione entity = new CentroAdozione();
        entity.setId(Long.valueOf(id));
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        // WHEN
        CentroAdozione result = service.findById(id);

        // THEN
        assertThat(result.getId()).isEqualTo(id);
        verify(repository).findById(id);
    }

    @Test
    void testFindAll() {
        // GIVEN
        List<CentroAdozione> lista = List.of(new CentroAdozione(), new CentroAdozione());
        when(repository.findAll()).thenReturn(lista);

        // WHEN
        List<CentroAdozione> result = service.findAll();

        // THEN
        assertThat(result).hasSize(2);
        verify(repository).findAll();
    }
}