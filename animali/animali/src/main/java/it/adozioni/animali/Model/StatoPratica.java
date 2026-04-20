package it.adozioni.animali.Model;

public enum StatoPratica {
    PENDING,        // Appena inviata
    IN_VALUTAZIONE, // Presa in carico dall'admin
    APPROVATA,      // Pratica conclusa con successo
    RIFIUTATA       // Pratica chiusa negativamente
}