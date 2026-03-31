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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnimaleService extends AbstractService<Animale, AnimaleDto> {

    private final AnimaleMapper animaleMapper;
    private final AnimaleRepository animaleRepository;
    private final AdottanteRepository adottanteRepository;
    private final AdottanteMapper adottanteMapper;

    public AnimaleService(JpaRepository<Animale, Integer> repository,
                          Converter<Animale, AnimaleDto> converter,
                          AnimaleMapper animaleMapper,
                          AnimaleRepository animaleRepository,
                          AdottanteRepository adottanteRepository,
                          AdottanteMapper adottanteMapper) {
        super(repository, converter);

        this.animaleMapper = animaleMapper;
        this.animaleRepository = animaleRepository;
        this.adottanteRepository = adottanteRepository;
        this.adottanteMapper = adottanteMapper;
    }

    @Transactional
    public String generaContrattoAdozione(Integer animaleId, Integer adottanteId) {
        // Recupero le entità reali. Se non esistono, il metodo lancia un'eccezione
        // e ferma l'esecuzione, evitando dati 'null' nel contratto.
        Animale animale = animaleRepository.findById(animaleId)
                .orElseThrow(() -> new RuntimeException("Animale con ID " + animaleId + " non trovato"));

        Adottante adottante = adottanteRepository.findById(adottanteId)
                .orElseThrow(() -> new RuntimeException("Adottante con ID " + adottanteId + " non trovato"));

        // Logica di business: aggiorno lo stato dell'animale
        animale.setAdottato(true);
        animale.setAdottante(adottante);
        animaleRepository.save(animale);

        return String.format(
                "CERTIFICATO DI ADOZIONE\n" +
                        "--------------------------\n" +
                        "Il sottoscritto %s %s,\n" +
                        "si impegna a prendersi cura dell'animale descritto di seguito.\n" +
                        "Testo Plain: L'animale dovrà essere trattato con cura e rispetto.\n\n" +
                        "DATI ANIMALE:\n" +
                        "Nome: %s\n" +
                        "Microchip: %s\n\n" +
                        "Firma dell'adottante: ________________\n" +
                        "(Firmato digitalmente da %s %s)",
                adottante.getNome(), adottante.getCognome(), "Indirizzo da definire",
                animale.getNome(), animale.getMicrochip(),
                adottante.getNome(), adottante.getCognome()
        );
    }

    // Metodo fondamentale per il Controller
    public Animale findByIdEntity(Integer id) {
        // IMPORTANTE: .orElse(null) permette al Controller di ricevere un vero null
        // e rispondere con un errore 404 invece di generare un PDF vuoto.
        return animaleRepository.findById(id).orElse(null);
    }

    public List<AnimaleDto> findByNome(String nome) {
        return animaleMapper.toDTOList(animaleRepository.findByNome(nome));
    }

    public List<AnimaleDto> findByRazza(String razza) {
        return animaleMapper.toDTOList(animaleRepository.findByRazza(razza));
    }

    public List<AnimaleDto> findByGenere(String genere) {
        return animaleMapper.toDTOList(animaleRepository.findByGenere(genere));
    }

    public List<AnimaleDto> findBySpecie(String specie) {
        return animaleMapper.toDTOList(animaleRepository.findBySpecie(specie));
    }

    public List<AnimaleDto> findByRazzaAndSpecie(String razza, String specie) {
        return animaleMapper.toDTOList(animaleRepository.findByRazzaAndSpecie(razza, specie));
    }

    public List<AnimaleDto> findDisponibiliPerCentro(Integer centroId) {
        return animaleMapper.toDTOList(animaleRepository.findDisponibiliPerCentro(centroId));
    }

    public List<AnimaleDto> findLastFiveAdded() {
        return animaleMapper.toDTOList(animaleRepository.findLastFiveAdded());
    }

    public long countPerSpecieEIdCentro(String specie, Integer centroId) {
        return animaleRepository.countPerSpecieEIdCentro(specie, centroId);
    }

    public List<AnimaleDto> findAnimaliConMolteVisite(int minVisite) {
        return animaleMapper.toDTOList(animaleRepository.findAnimaliConMolteVisite(minVisite));
    }

    public List<AnimaleDto> findAll() {
        return animaleMapper.toDTOList(animaleRepository.findAll());
    }
}