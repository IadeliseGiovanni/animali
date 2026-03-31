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

    final private AnimaleMapper animaleMapper;
    final private AnimaleRepository animaleRepository;
    final private AdottanteRepository adottanteRepository;
    final private AdottanteMapper adottanteMapper;

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
        Animale animale = animaleRepository.findById(animaleId)
                .orElseThrow(() -> new RuntimeException("Animale non trovato"));

        Adottante adottante = adottanteRepository.findById(adottanteId)
                .orElseThrow(() -> new RuntimeException("Adottante non trovato"));

        animale.setAdottato(true);
        animale.setAdottante(adottante);
        animaleRepository.save(animale);

        return String.format(
                "CERTIFICATO DI ADOZIONE\n" +
                        "--------------------------\n" +
                        "Il sottoscritto %s %s, residente a %s,\n" + // Aggiunto segnaposto per residenza
                        "si impegna a prendersi cura dell'animale descritto di seguito.\n" +
                        "Testo Plain: L'animale dovrà essere trattato con cura e rispetto.\n\n" +
                        "DATI ANIMALE:\n" +
                        "Nome: %s\n" +
                        "Microchip: %s\n\n" +
                        "Firma dell'adottante: ________________\n" +
                        "(Firmato digitalmente da %s %s)",
                adottante.getNome(), adottante.getCognome(), "Indirizzo Non Specificato", // Sostituisci con campo reale
                animale.getNome(), animale.getMicrochip(),
                adottante.getNome(), adottante.getCognome()
        );
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

    public Animale findByIdEntity(Integer id) {
        return animaleRepository.findById(id).orElse(new Animale());
    }
}