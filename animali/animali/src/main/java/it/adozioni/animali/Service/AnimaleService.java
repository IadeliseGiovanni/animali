package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.AnimaleDto;
import it.adozioni.animali.Mapper.AnimaleMapper;
import it.adozioni.animali.Model.Animale;
import it.adozioni.animali.Repository.AnimaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnimaleService extends AbstractService<Animale, AnimaleDto> {

    private final AnimaleRepository animaleRepository;
    private final AnimaleMapper animaleMapper;

    @Autowired
    public AnimaleService(AnimaleRepository animaleRepository, AnimaleMapper animaleMapper) {
        // Passiamo i parametri al costruttore della classe astratta padre
        super(animaleRepository, animaleMapper);
        this.animaleRepository = animaleRepository;
        this.animaleMapper = animaleMapper;
    }

    /**
     * Recupera l'oggetto Animale (Entity) direttamente dal database.
     * Necessario per la generazione del contratto PDF nel controller.
     */
    public Animale findByIdEntity(Integer id) {
        // .orElse(null) evita errori se l'ID non esiste nel DB
        return animaleRepository.findById(id).orElse(null);
    }

    // Metodo per cercare animali per nome
    public List<AnimaleDto> findByNome(String nome) {
        return animaleMapper.toDTOList(animaleRepository.findByNome(nome));
    }

    // Metodo per ottenere la lista completa in formato DTO
    public List<AnimaleDto> findAll() {
        return animaleMapper.toDTOList(animaleRepository.findAll());
    }
}