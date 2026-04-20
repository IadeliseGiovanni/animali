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
        super(animaleRepository, animaleMapper);
        this.animaleRepository = animaleRepository;
        this.animaleMapper = animaleMapper;
    }

    // Fondamentale: accetta Integer per coerenza con il DB e i Model
    public Animale findByIdEntity(Integer id) {
        if (id == null) {
            System.err.println("SERVICE LOG: L'ID ricevuto è NULL!");
            return null;
        }

        System.out.println("SERVICE LOG: Cerco nel Database l'animale con ID: " + id);

        Animale trovato = animaleRepository.findById(id).orElse(null);

        if (trovato == null) {
            System.err.println("SERVICE LOG: Nessun animale trovato in PostgreSQL con ID: " + id);
        } else {
            System.out.println("SERVICE LOG: Animale trovato! Nome: " + trovato.getNome());
        }

        return trovato;
    }

    public List<AnimaleDto> findByNome(String nome) {
        return animaleMapper.toDTOList(animaleRepository.findByNome(nome));
    }

    public List<AnimaleDto> findAllDisponibili() {
        return animaleMapper.toDTOList(animaleRepository.findByAdottatoFalse());
    }

    public List<AnimaleDto> findAll() {
        return animaleMapper.toDTOList(animaleRepository.findAll());
    }

    public List<AnimaleDto> filterAnimali(String specie, String genere, Long centroId) {
        String s = (specie != null && !specie.isEmpty()) ? specie : null;
        String g = (genere != null && !genere.isEmpty()) ? genere : null;

        // Qui dobbiamo assicurarci che la query nel repository filtri per adottato = false
        // Se la tua query findFiltered è una @Query personalizzata, dovrai aggiungere "AND a.adottato = false"
        List<Animale> lista = animaleRepository.findFiltered(s, g, centroId);

        // Se non vuoi toccare la query SQL, puoi filtrare qui con gli Stream (meno performante ma veloce da implementare)
        return animaleMapper.toDTOList(
                lista.stream().filter(a -> !a.isAdottato()).toList()
        );
    }
//
    public List<AnimaleDto> getAnimaliByCentro(Long centroId) {
        return animaleMapper.toDTOList(animaleRepository.findByCentroAdozioneId(centroId));
    }
}