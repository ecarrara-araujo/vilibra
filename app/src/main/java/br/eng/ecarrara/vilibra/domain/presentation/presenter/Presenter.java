package br.eng.ecarrara.vilibra.domain.presentation.presenter;

/**
 * Interface for implementation of Presenters as stated in the Model View Presenter pattern.
 */
public interface Presenter {

    void resume();

    void pause();

    void destroy();

}
