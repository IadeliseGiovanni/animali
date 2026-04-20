package it.adozioni.animali.Service;

import it.adozioni.animali.Dto.PraticaAdozioneDto;
import it.adozioni.animali.Mapper.PraticaAdozioneMapper;
import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Model.Animale;
import it.adozioni.animali.Model.PraticaAdozione;
import it.adozioni.animali.Model.StatoPratica;
import it.adozioni.animali.Repository.AnimaleRepository;
import it.adozioni.animali.Repository.PraticaAdozioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PraticaAdozioneService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private PraticaAdozioneRepository praticaRepository;

    @Autowired
    private AnimaleRepository animaleRepository;

    @Autowired
    private PraticaAdozioneMapper praticaMapper;

    @Autowired
    private DocumentoService documentoService;

    @Transactional
    public PraticaAdozioneDto avviaPratica(Adottante utente, Integer animaleId) {

        // 1. Recupero l'animale e verifico che esista
        Animale animale = animaleRepository.findById(animaleId)
                .orElseThrow(() -> new RuntimeException("Errore: Animale con ID " + animaleId + " non trovato."));

        // 2. Controllo Idoneità dell'utente (campo isSchedato)
        // Se isSchedato è null o false, l'utente non può ancora adottare
        if (utente.getIsSchedato() == null || !utente.getIsSchedato()) {
            throw new RuntimeException("Accesso negato: Il tuo profilo è in fase di verifica. " +
                    "Potrai avviare pratiche solo dopo l'approvazione dell'idoneità.");
        }

        // 3. Controllo se l'animale è già stato adottato ufficialmente
        if (animale.isAdottato()) {
            throw new RuntimeException("Spiacenti, " + animale.getNome() + " è già stato adottato.");
        }

        // 4. Controllo Duplicati: l'utente ha già una pratica APERTA per questo specifico animale?
        // Escludiamo le pratiche RIFIUTATE (se è stata rifiutata in passato, può riprovare)
        boolean giaInCorso = praticaRepository.existsByAdottanteIdAndAnimaleIdAndStatoNot(
                utente.getId(),
                animaleId,
                StatoPratica.RIFIUTATA
        );

        if (giaInCorso) {
            throw new RuntimeException("Hai già una pratica attiva o in valutazione per " + animale.getNome() + ".");
        }

        // 5. Creazione della nuova entità Pratica
        PraticaAdozione nuovaPratica = new PraticaAdozione();
        nuovaPratica.setAdottante(utente);
        nuovaPratica.setAnimale(animale);
        nuovaPratica.setStato(StatoPratica.PENDING);
        nuovaPratica.setDataApertura(LocalDateTime.now());
        nuovaPratica.setNoteAdmin("In attesa di prima revisione.");

        // 6. Salvataggio su DB
        PraticaAdozione salvata = praticaRepository.save(nuovaPratica);

        // 7. Ritorno il DTO tramite il mapper
        return praticaMapper.toDTO(salvata);
    }

    public List<PraticaAdozioneDto> getAllPratiche() {
        return praticaRepository.findAll()
                .stream()
                .map(praticaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PraticaAdozioneDto> getPraticheByUtente(Integer adottanteId) {
        return praticaRepository.findByAdottanteId(adottanteId)
                .stream()
                .map(praticaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PraticaAdozioneDto aggiornaStatoPratica(Integer praticaId, StatoPratica nuovoStato, String note) {
        PraticaAdozione pratica = praticaRepository.findById(praticaId)
                .orElseThrow(() -> new RuntimeException("Pratica non trovata"));

        pratica.setStato(nuovoStato);
        pratica.setNoteAdmin(note);

        if (nuovoStato == StatoPratica.APPROVATA) {
            Animale animale = pratica.getAnimale();
            Adottante adottante = pratica.getAdottante();

            animale.setAdottato(true);
            animale.setAdottante(adottante);
            animaleRepository.save(animale);

            // Generazione e Invio Contratto
            byte[] pdf = documentoService.creaPdf(animale, adottante);
            if (pdf != null) {
                emailService.inviaContrattoConAllegato(adottante.getEmail(), animale.getNome(), pdf);
            }
        }

        return praticaMapper.toDTO(praticaRepository.save(pratica));
    }


}