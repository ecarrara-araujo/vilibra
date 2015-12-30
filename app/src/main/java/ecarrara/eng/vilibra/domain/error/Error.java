package ecarrara.eng.vilibra.domain.error;

/**
 * Representation of an error.
 */
public interface Error {

    Exception getException();
    String getMessage();

}
