package ecarrara.eng.vilibra.domain.presentation.view;

/**
 * Interface for implementation of Views that displays data that is loaded asynchronously.
 *
 */
public interface LoadDataView {

    /**
     * Display a loading UI component.
     */
    void showLoading();

    /**
     * Hides the loading UI component.
     */
    void hideLoading();

    /**
     * Display a retry UI component.
     */
    void showRetry();

    /**
     * Hides the retry UI component.
     */
    void hideRetry();

    /**
     * Display a proper UI for error messages.
     */
    void showError(String message);

}
