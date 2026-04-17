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
}