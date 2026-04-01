package it.adozioni.animali.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Invia una mail all'adottante quando l'animale è pronto.
     * Include il contratto PDF e le istruzioni operative per il ritiro.
     */
    public void inviaContrattoConAllegato(String destinatario, String nomeAnimale, byte[] pdfContenuto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // Multipart = true per permettere l'allegato PDF
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(destinatario);
            helper.setSubject("🎉 Buone notizie: Il contratto per " + nomeAnimale + " è pronto!");

            // Costruzione del corpo mail informativo
            StringBuilder corpoMail = new StringBuilder();
            corpoMail.append("Gentile Adottante,\n\n");
            corpoMail.append("siamo felici di comunicarti che la documentazione per l'adozione di ").append(nomeAnimale).append(" è stata completata con successo.\n\n");
            corpoMail.append("Il tuo nuovo amico è ufficialmente pronto per iniziare una nuova vita con te! In allegato a questa email troverai il contratto di adozione ufficiale.\n\n");

            corpoMail.append("--- ISTRUZIONI PER IL RITIRO ---\n");
            corpoMail.append("1. STAMPA: Ti preghiamo di stampare il contratto allegato.\n");
            corpoMail.append("2. FIRMA: Apponi la tua firma autografa negli spazi indicati.\n");
            corpoMail.append("3. DOCUMENTI: Porta con te un documento d'identità valido il giorno dell'appuntamento.\n");
            corpoMail.append("4. DOTAZIONE: Ricorda di portare guinzaglio, pettorina e una copertina per facilitare il trasporto.\n\n");

            corpoMail.append("Il nostro staff ti contatterà telefonicamente nelle prossime ore per fissare l'orario esatto del ritiro presso il nostro Centro Adozioni.\n\n");
            corpoMail.append("Non vediamo l'ora di vedervi insieme!\n\n");
            corpoMail.append("Cordiali saluti,\n");
            corpoMail.append("Lo Staff del Centro Adozioni - AccademiJava");

            helper.setText(corpoMail.toString());

            // Allegato PDF generato dinamicamente
            String nomeFile = "Contratto_Adozione_" + nomeAnimale.replace(" ", "_") + ".pdf";
            helper.addAttachment(nomeFile, new ByteArrayResource(pdfContenuto));

            mailSender.send(message);
            System.out.println("LOG: Email di adozione inviata con successo a " + destinatario);

        } catch (MessagingException e) {
            System.err.println("ERRORE CRITICO INVIO EMAIL: " + e.getMessage());
            // L'eccezione viene loggata ma non blocca il download del PDF nel controller grazie al try-catch locale
        }
    }
}