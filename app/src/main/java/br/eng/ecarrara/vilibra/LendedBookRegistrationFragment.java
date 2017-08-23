package br.eng.ecarrara.vilibra;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import javax.inject.Inject;

import br.eng.ecarrara.vilibra.book.data.datasource.BookRemoteDataSource;
import br.eng.ecarrara.vilibra.book.domain.entity.Book;
import br.eng.ecarrara.vilibra.book.domain.usecase.SearchForBook;
import br.eng.ecarrara.vilibra.core.di.VilibraInjector;
import br.eng.ecarrara.vilibra.data.VilibraContract;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LendedBookRegistrationFragment extends Fragment {

    @Inject
    BookRemoteDataSource bookRemoteDataSource;

    @Inject
    SearchForBook searchForBookUseCase;

    @BindView(R.id.main_content_frame)
    View mainContentFrame;

    @BindView(R.id.progress_frame)
    View progressFrame;

    @BindView(R.id.barcode_scanner_view)
    DecoratedBarcodeView barcodeScannerView;

    @BindView(R.id.isbn_edit_text)
    EditText isbnEditText;

    private String lastBarcodeContentRead;
    private Uri borrowedBookUri;

    private CompositeDisposable disposables = new CompositeDisposable();

    public interface Callback {
        public void onError(String message);
    }

    private BarcodeCallback barcodeReadingCallback = new BarcodeCallback() {

        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastBarcodeContentRead)) {
                return;
            }

            lastBarcodeContentRead = result.getText();
            isbnEditText.setText(result.getText());
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }

    };

    public LendedBookRegistrationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lended_book_registration, container, false);
        VilibraInjector.INSTANCE.getVilibraComponent().inject(this);
        ButterKnife.bind(this, rootView);

        barcodeScannerView.decodeContinuous(barcodeReadingCallback);

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        clearLoadingState();
        barcodeScannerView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeScannerView.pause();
    }

    @OnClick(R.id.confirm_button)
    public void confirm(View view) {
        onRetrieveBookInfo();
    }

    private void clearLoadingState() {
        mainContentFrame.setVisibility(View.VISIBLE);
        progressFrame.setVisibility(View.GONE);
    }

    private void setLoadingState() {
        mainContentFrame.setVisibility(View.GONE);
        progressFrame.setVisibility(View.VISIBLE);
    }

    private void onRetrieveBookInfo() {
        setLoadingState();
        String isbn = ((EditText) mainContentFrame
                .findViewById(R.id.isbn_edit_text)).getText().toString();

        disposables.add(searchForBookUseCase.execute(isbn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<Book>() {
                            @Override
                            public void accept(Book book) throws Exception {
                                displayBookDetail(book);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                ((Callback) getActivity()).onError(
                                        getString(R.string.error_book_data_retrieve)
                                );
                            }
                        }
                )
        );
    }

    private void displayBookDetail(Book book) {
        clearLoadingState();
        Intent detailIntent = new Intent(getActivity(), LendedBookDetailActivity.class);
        detailIntent.putExtra(LendedBookDetailActivity.EXTRA_KEY_BOOK_URI,
                VilibraContract.BookEntry.buildBookUri(book.getId()));
        getActivity().startActivity(detailIntent);
        getActivity().finish();
    }

}
