package br.eng.ecarrara.vilibra.domain.error;

/**
 * Representation of an error.
 */
public interface Error {

    Exception getException();
    String getMessage();

}
