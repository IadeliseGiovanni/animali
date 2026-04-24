package it.adozioni.animali.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired //inietta la dipendenza
    private JavaMailSender mailSender;

    // Questa variabile conterrà "iadelisegiovanni2000@gmail.com" presa dal properties
    @Value("${spring.mail.username}")
    private String emailSorgente;

    /**
     * STEP 1: Invia il contratto PDF all'adottante.
     */
    public void inviaContrattoConAllegato(String destinatario, String nomeAnimale, byte[] pdfContenuto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // Il parametro true indica che la mail è multiparte (testo + allegato)
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // IMPORTANTE: Imposta la TUA email come mittente ufficiale
            helper.setFrom(emailSorgente);
            helper.setTo(destinatario);
            helper.setSubject("🎉 Congratulazioni! Il contratto per " + nomeAnimale + " è pronto");

            StringBuilder corpoMail = new StringBuilder();
            corpoMail.append("Gentile Adottante,\n\n");
            corpoMail.append("Siamo felici di comunicarti che la documentazione per l'adozione di ").append(nomeAnimale).append(" è pronta.\n\n");
            corpoMail.append("In allegato trovi il contratto ufficiale in formato PDF generato dal nostro sistema.\n");
            corpoMail.append("Ti preghiamo di prenderne visione e confermare la ricezione.\n\n");
            corpoMail.append("Cordiali saluti,\nStaff AccademiJava 🐾\nAmministratore: Giovanni Iadelise");

            helper.setText(corpoMail.toString());

            // Nome dinamico del file basato sull'animale
            String nomeFile = "Contratto_" + nomeAnimale.replace(" ", "_") + ".pdf";
            helper.addAttachment(nomeFile, new ByteArrayResource(pdfContenuto));

            mailSender.send(message);
            System.out.println("LOG: Email inviata con successo dal Centro (" + emailSorgente + ") a " + destinatario);
        } catch (MessagingException e) {
            System.err.println("ERRORE INVIO CONTRATTO: " + e.getMessage());
        }
    }

    /**h
     * STEP 2: Invia una mail di notifica al Centro (a te stesso).
     */
    public void inviaNotificaRicezioneAlCentro(String emailAdottante, String nomeAnimale) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setFrom(emailSorgente);
            helper.setTo(emailSorgente); // La notifica arriva a te stesso come amministratore
            helper.setSubject("✅ CONFERMA RICEZIONE SISTEMA: " + nomeAnimale);

            String testo = "REPORT DI SISTEMA - ACCADEMIJAVA\n\n" +
                    "L'utente (" + emailAdottante + ") ha generato correttamente il contratto " +
                    "per l'adozione di " + nomeAnimale + ".\n" +
                    "Il modulo PDF è stato correttamente inviato e processato.\n\n" +
                    "Amministratore di sistema: " + emailSorgente;

            helper.setText(testo);
            mailSender.send(message);
            System.out.println("LOG: Notifica di ricezione inviata all'amministratore (" + emailSorgente + ")");
        } catch (MessagingException e) {
            System.err.println("ERRORE NOTIFICA CENTRO: " + e.getMessage());
        }
    }

    public void sendVerificationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Benvenuto su PetFlow! 🐾 Conferma il tuo account");
        message.setText("Ciao! Clicca sul link per attivare il tuo profilo: " +
                "http://localhost:8080/api/auth/verify?token=" + token);

        mailSender.send(message);
    }

    public void inviaNuovaPassword(String destinatario, String nuovaPassword) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setFrom(emailSorgente);
            helper.setTo(destinatario);
            helper.setSubject("🔑 Reset Password - PetFlow");

            StringBuilder testo = new StringBuilder();
            testo.append("Ciao,\n\n");
            testo.append("Abbiamo ricevuto una richiesta di reset della password per il tuo account PetFlow.\n\n");
            testo.append("La tua nuova password temporanea è: ").append(nuovaPassword).append("\n\n");
            testo.append("Ti consigliamo di accedere al più presto e cambiare questa password dalle impostazioni del tuo profilo per garantire la massima sicurezza.\n\n");
            testo.append("Se non hai richiesto tu questo reset, contatta immediatamente il supporto.\n\n");
            testo.append("Cordiali saluti,\nLo Staff di PetFlow 🐾");

            helper.setText(testo.toString());
            mailSender.send(message);

            System.out.println("LOG: Email di reset inviata correttamente a " + destinatario);
        } catch (MessagingException e) {
            System.err.println("ERRORE INVIO MAIL RESET: " + e.getMessage());
        }
    }

    /**
     * Invia una mail di conferma dopo che l'utente ha resettato la password con successo.
     */
    public void inviaConfermaCambioPassword(String destinatario) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setFrom(emailSorgente);
            helper.setTo(destinatario);
            helper.setSubject("🔒 Sicurezza account: Password aggiornata");

            StringBuilder testo = new StringBuilder();
            testo.append("Ciao,\n\n");
            testo.append("Ti confermiamo che la password del tuo account PetFlow è stata modificata correttamente.\n\n");
            testo.append("Se hai effettuato tu questa operazione, puoi ignorare questa email.\n\n");
            testo.append("Se NON hai richiesto tu il cambio della password, ti invitiamo a contattare immediatamente il nostro supporto o a provare a recuperare l'accesso al tuo profilo.\n\n");
            testo.append("La sicurezza del tuo account è la nostra priorità.\n\n");
            testo.append("Cordiali saluti,\nLo Staff di PetFlow 🐾");

            helper.setText(testo.toString());
            mailSender.send(message);

            System.out.println("LOG: Email di conferma cambio password inviata a " + destinatario);
        } catch (MessagingException e) {
            System.err.println("ERRORE INVIO MAIL CONFERMA RESET: " + e.getMessage());
        }
    }

    public void inviaConfermaRichiestaIdoneita(String destinatario, String nomeUtente) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setFrom(emailSorgente);
            helper.setTo(destinatario);
            helper.setSubject("Richiesta Idoneità Ricevuta! 🐾 - PetFlow");

            StringBuilder testo = new StringBuilder();
            testo.append("Ciao ").append(nomeUtente).append(",\n\n");
            testo.append("Abbiamo ricevuto la tua richiesta per ottenere l'idoneità all'adozione su PetFlow.\n\n");
            testo.append("Il nostro staff valuterà i tuoi dati e ti contatterà al più presto per fissare un eventuale colloquio conoscitivo.\n\n");
            testo.append("Puoi controllare lo stato della tua richiesta direttamente dal tuo profilo.\n\n");
            testo.append("Grazie per aver scelto di donare una casa a un piccolo amico!\n\n");
            testo.append("Cordiali saluti,\nLo Staff di PetFlow 🐾");

            helper.setText(testo.toString());
            mailSender.send(message);

            System.out.println("LOG: Email richiesta idoneità inviata a " + destinatario);
        } catch (MessagingException e) {
            System.err.println("ERRORE INVIO MAIL IDONEITA: " + e.getMessage());
        }
    }

    public void inviaMailConferma(String emailDestinatario, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDestinatario);
        message.setSubject("Conferma il tuo nuovo indirizzo Email - PetFlow");
        message.setText("Clicca sul link per confermare la tua nuova email: " + link);
        mailSender.send(message);
    }

    public void inviaNotificaCambioEffettuato(String vecchiaEmail, String nuovaEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(vecchiaEmail);
        message.setSubject("Il tuo indirizzo Email è stato aggiornato");
        message.setText("Ti informiamo che l'email associata al tuo account è stata cambiata con successo in: " + nuovaEmail);
        mailSender.send(message);
    }

    public void sendResetEmail(String userEmail, String token) {
        // Il link punta alla rotta che creerai in Angular
        String resetUrl = "http://localhost:4200/reset-password?token=" + token;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(userEmail);
            helper.setSubject("PetFlow - Recupero Password");

            // Testo HTML dell'email
            String content = "<h3>Richiesta di Reset Password</h3>"
                    + "<p>Ciao,</p>"
                    + "<p>Abbiamo ricevuto una richiesta di reset password per il tuo account PetFlow.</p>"
                    + "<p>Clicca sul pulsante qui sotto per procedere:</p>"
                    + "<a href=\"" + resetUrl + "\" style=\""
                    + "background-color: #f38131; color: white; padding: 10px 20px; "
                    + "text-decoration: none; border-radius: 5px; display: inline-block;"
                    + "\">Resetta Password</a>"
                    + "<p>Se non hai richiesto tu il reset, ignora questa email.</p>"
                    + "<br><p>Il team PetFlow 🐾</p>";

            helper.setText(content, true); // 'true' indica che è HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Errore nell'invio dell'email: " + e.getMessage());
        }
    }
}