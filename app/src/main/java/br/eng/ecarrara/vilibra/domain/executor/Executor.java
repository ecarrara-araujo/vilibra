package br.eng.ecarrara.vilibra.domain.executor;

/**
 * Defines an Interface for the execution of an {@link Interactor}.
 *
 * The implementation of an Executor can be based on different techniques and/or frameworks,
 * and it will define the way that an {@link Interactor} will be executed.
 *
 */
public interface Executor {

    void execute(final Interactor interactor);

}
