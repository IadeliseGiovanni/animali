package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.AnimaleDto;
import it.adozioni.animali.Mapper.AdottanteMapper;
import it.adozioni.animali.Mapper.AnimaleMapper;
import it.adozioni.animali.Mapper.Converter;
import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Model.Animale;
import it.adozioni.animali.Repository.AdottanteRepository;
import it.adozioni.animali.Repository.AnimaleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnimaleService extends AbstractService<Animale, AnimaleDto>{

     final private AnimaleMapper animaleMapper;
     final private AnimaleRepository animaleRepository;
     final private AdottanteRepository adottanteRepository;
     final private AdottanteMapper adottanteMapper;

    protected AnimaleService(JpaRepository<Animale, Integer> repository, Converter<Animale, AnimaleDto> converter, AnimaleMapper animaleMapper, AnimaleRepository animaleRepository, AdottanteRepository adottanteRepository, AdottanteMapper adottanteMapper) {
        super(repository, converter);

        this.animaleMapper = animaleMapper;
        this.animaleRepository = animaleRepository;
        this.adottanteRepository = adottanteRepository;
        this.adottanteMapper = adottanteMapper;
    }

    public String generaContrattoAdozione(Integer animaleId, Integer adottanteId) {
        // 1. Recupero i dati
        Animale animale = animaleRepository.findById(animaleId)
                .orElseThrow(() -> new RuntimeException("Animale non trovato"));

        Adottante adottante = adottanteRepository.findById(adottanteId)
                .orElseThrow(() -> new RuntimeException("Adottante non trovato"));

        // 2. Logica di business: aggiorno l'animale
        animale.setAdottato(true);
        animale.setAdottante(adottante);
        animaleRepository.save(animale);

        // 3. Restituzione del contratto formattato
        return String.format(
                "CERTIFICATO DI ADOZIONE\n" +
                        "--------------------------\n" +
                        "Il sottoscritto %s %s, residente a %s,\n" +
                        "si impegna a prendersi cura dell'animale descritto di seguito.\n" +
                        "Testo Plain: L'animale dovrà essere trattato con cura e rispetto.\n\n" +
                        "DATI ANIMALE:\n" +
                        "Nome: %s\n" +
                        "Microchip: %s\n\n" +
                        "Firma dell'adottante: ________________\n" +
                        "(Firmato digitalmente da %s %s)",
                adottante.getNome(), adottante.getCognome(),
                animale.getNome(), animale.getMicrochip(),
                adottante.getNome(), adottante.getCognome()
        );
    }
    public List<AnimaleDto> findByNome(String nome){
        return animaleMapper.toDTOList(animaleRepository.findByNome(nome));
    }
    List<AnimaleDto>findByRazza(String razza){
        return animaleMapper.toDTOList(animaleRepository.findByRazza(razza));
    }
    List<AnimaleDto>findByGenere(String genere){
        return animaleMapper.toDTOList(animaleRepository.findByGenere(genere));
    }
    List<AnimaleDto>findBySpecie(String specie){
        return animaleMapper.toDTOList(animaleRepository.findBySpecie(specie));
    }
    List<AnimaleDto>findByRazzaAndSpecie(String razza, String specie){
        return animaleMapper.toDTOList(animaleRepository.findByRazzaAndSpecie(razza,specie));
    }
    List<AnimaleDto> findDisponibiliPerCentro( Integer centroId){
        return animaleMapper.toDTOList(animaleRepository.findDisponibiliPerCentro(centroId));
    }
    List<AnimaleDto> findLastFiveAdded(){
        return animaleMapper.toDTOList(animaleRepository.findLastFiveAdded());
    }
    public long countPerSpecieEIdCentro(String specie, Integer centroId) {
        return animaleRepository.countPerSpecieEIdCentro(specie, centroId);
    }
    public List<AnimaleDto> findAnimaliConMolteVisite(int minVisite) {
        // Esattamente come gli altri: chiamo il repo e passo il risultato al mapper
        return animaleMapper.toDTOList(animaleRepository.findAnimaliConMolteVisite(minVisite));
    }

    public List<AnimaleDto> findAll(){
        return animaleMapper.toDTOList(animaleRepository.findAll());
    }

    public Animale findByIdEntity(Integer id) {
        return animaleRepository.findById(id).orElse(new Animale());
    }
}
