package it.adozioni.animali.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Wrapper standard per tutte le risposte dell'API.
 * @param <T> Il tipo di dato contenuto nel campo 'data'
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultDto<T> {

    private boolean success;
    private String message;
    private T data;

    // Metodo statico per risposte di successo
    public static <T> ResultDto<T> success(String message, T data) {
        return new ResultDto<>(true, message, data);
    }

    // Metodo statico per risposte di errore
    public static <T> ResultDto<T> error(String message) {
        return new ResultDto<>(false, message, null);
    }
    //
}