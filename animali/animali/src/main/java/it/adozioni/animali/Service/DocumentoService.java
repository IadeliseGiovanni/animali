package it.adozioni.animali.Service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Model.Animale;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class DocumentoService {

    public byte[] creaPdf(Animale animale, Adottante adottante) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 60, 60, 50, 50);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // --- 1. SFONDO NATURALE PROFESSIONALE ---
            PdfContentByte canvas = writer.getDirectContentUnder();
            canvas.saveState();
            canvas.setColorFill(new Color(220, 228, 201)); // Sage Green
            canvas.rectangle(0, 0, PageSize.A4.getWidth(), PageSize.A4.getHeight());
            canvas.fill();
            canvas.restoreState();

            // --- 2. PALETTE E FONT ---
            Color forestGreen = new Color(30, 81, 40);
            Color darkEarth = new Color(62, 39, 35);

            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, forestGreen);
            Font fontSub = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, darkEarth);
            Font fontSection = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, forestGreen);
            Font fontText = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
            Font fontSmall = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.DARK_GRAY);

            // --- 3. HEADER ISTITUZIONALE ---
            Paragraph header = new Paragraph("SISTEMA NAZIONALE GESTIONE RIFUGI\nProtocollo Adozioni Interno", fontSub);
            header.setAlignment(Element.ALIGN_RIGHT);
            document.add(header);
            document.add(new Paragraph("\n"));

            // --- 4. TITOLO ---
            Paragraph titolo = new Paragraph("CERTIFICATO DI ADOZIONE DEFINITIVA", fontTitle);
            titolo.setAlignment(Element.ALIGN_CENTER);
            titolo.setSpacingAfter(5);
            document.add(titolo);

            String dataOggi = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Paragraph data = new Paragraph("Rilasciato in data: " + dataOggi, fontSmall);
            data.setAlignment(Element.ALIGN_CENTER);
            document.add(data);
            document.add(new Paragraph("\n\n"));

            // --- 5. SEZIONE I: DICHIARAZIONE DELL'ADOTTANTE ---
            document.add(new Paragraph("ART. 1 - DATI DELL'AFFIDATARIO", fontSection));
            document.add(new Paragraph("Il sottoscritto " + adottante.getNome().toUpperCase() + " " + adottante.getCognome().toUpperCase() +
                    ", registrato con email " + adottante.getEmail() + ", dichiara sotto la propria responsabilità di accogliere l'animale descritto all'Art. 2, " +
                    "assumendone la custodia legale e l'onere del mantenimento.", fontText));
            document.add(new Paragraph("\n"));

            // --- 6. SEZIONE II: DESCRIZIONE SOGGETTO ---
            document.add(new Paragraph("ART. 2 - IDENTIFICAZIONE ANIMALE", fontSection));
            document.add(new Paragraph("___________________________________________________________", FontFactory.getFont(FontFactory.HELVETICA, 8, Color.WHITE)));

            Paragraph infoAnimale = new Paragraph();
            infoAnimale.setFont(fontText);
            infoAnimale.add(new Chunk("NOME: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, forestGreen)));
            infoAnimale.add(animale.getNome().toUpperCase() + " | ");
            infoAnimale.add(new Chunk("SPECIE: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, forestGreen)));
            infoAnimale.add(animale.getSpecie() + "\n");
            infoAnimale.add(new Chunk("RAZZA: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, forestGreen)));
            infoAnimale.add((animale.getRazza() != null ? animale.getRazza() : "Incrocio") + " | ");
            infoAnimale.add(new Chunk("MICROCHIP: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, forestGreen)));
            infoAnimale.add((animale.getMicrochip() != null ? animale.getMicrochip() : "In fase di registrazione"));
            document.add(infoAnimale);
            document.add(new Paragraph("\n"));

            // --- 7. SEZIONE III: OBBLIGHI E RESPONSABILITÀ (IL CONTENUTO PROFESSIONALE) ---
            document.add(new Paragraph("ART. 3 - VINCOLI CONTRATTUALI", fontSection));
            Paragraph clausole = new Paragraph();
            clausole.setFont(fontSmall);
            clausole.setAlignment(Element.ALIGN_JUSTIFIED);
            clausole.add("3.1 L'adottante si impegna a garantire il benessere psico-fisico del soggetto, fornendo idoneo riparo, nutrizione e profilassi veterinaria.\n");
            clausole.add("3.2 È fatto divieto assoluto di cessione a terzi, vendita, o abbandono del soggetto senza previa comunicazione scritta al centro.\n");
            clausole.add("3.3 Il centro si riserva il diritto di effettuare controlli post-affido per verificare lo stato di salute e integrazione del soggetto.\n");
            clausole.add("3.4 Qualora venissero riscontrate negligenze, il centro potrà procedere al ritiro immediato dell'animale e alla segnalazione alle autorità competenti.");
            document.add(clausole);

            document.add(new Paragraph("\n\n\n\n"));

            // --- 8. FIRME ---
            Paragraph firme = new Paragraph("IL RESPONSABILE DEL CENTRO               L'ADOTTANTE DICHIARANTE\n\n", fontSub);
            firme.add(new Chunk("____________________________              ____________________________", fontSub));
            firme.setAlignment(Element.ALIGN_CENTER);
            document.add(firme);

            // --- 9. FOOTER ---
            Paragraph footer = new Paragraph("\n\n🐾 Documento generato elettronicamente dal sistema 'Animali Rescue'. Validità legale ai sensi delle norme vigenti.", fontSmall);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}