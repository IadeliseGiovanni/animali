package it.adozioni.animali.Service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import it.adozioni.animali.Model.Adottante;
import it.adozioni.animali.Model.Animale;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class DocumentoService {

    public byte[] creaPdf(Animale animale, Adottante adottante) {
        // 1. Creiamo un contenitore per i byte in uscita
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // 2. Creiamo il documento (formato A4)
        Document document = new Document(PageSize.A4);

        try {
            // 3. Colleghiamo il "disegnatore" (PdfWriter) al contenitore
            PdfWriter.getInstance(document, out);

            // 4. Apriamo il documento per iniziare a scrivere
            document.open();

            // Font per il titolo
            Font fontTitolo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Paragraph titolo = new Paragraph("CONTRATTO DI ADOZIONE", fontTitolo);
            titolo.setAlignment(Element.ALIGN_CENTER);
            document.add(titolo);

            document.add(new Paragraph("\n")); // Spazio vuoto

            // 5. Inseriamo i dati dell'Adottante
            document.add(new Paragraph("Il sottoscritto/a: " + adottante.getNome() + " " + adottante.getCognome()));
            document.add(new Paragraph("\n"));

            // 6. Testo fisso (Plain text)
            document.add(new Paragraph("DICHIARA di assumersi la piena responsabilità dell'animale sotto indicato, " +
                    "impegnandosi a garantirne il benessere, le cure veterinarie e a non abbandonarlo."));
            document.add(new Paragraph("\n"));

            // 7. Dati Animale e Microchip
            document.add(new Paragraph("DATI DELL'ANIMALE:"));
            document.add(new Paragraph("Nome: " + animale.getNome()));
            document.add(new Paragraph("Identificativo (Microchip): " +
                    (animale.getMicrochip() != null ? animale.getMicrochip() : "NON PRESENTE")));

            document.add(new Paragraph("\n\n\n"));

            // 8. Spazio per la firma (allineato a destra)
            Paragraph firma = new Paragraph("Firma dell'adottante: ___________________________");
            firma.setAlignment(Element.ALIGN_RIGHT);
            document.add(firma);

            // 9. Chiudiamo il documento
            document.close();

        } catch (DocumentException e) {
            // Gestione errori di formattazione
            e.printStackTrace();
        }

        // 10. Trasformiamo tutto quello che abbiamo "disegnato" in un array di byte
        return out.toByteArray();
    }
}