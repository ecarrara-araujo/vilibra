package ecarrara.eng.vilibra.test.fixture;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import java.security.InvalidParameterException;

import ecarrara.eng.vilibra.data.VilibraContract;
import ecarrara.eng.vilibra.data.mapper.BookBorrowingContentProviderMapper;
import ecarrara.eng.vilibra.data.mapper.BookContentProviderMapper;
import ecarrara.eng.vilibra.domain.entity.Book;
import ecarrara.eng.vilibra.domain.entity.BookBorrowing;

public class VilibraProviderFixture {

    private Context context;
    private BookContentProviderMapper bookContentProviderMapper;
    private BookBorrowingContentProviderMapper bookBorrowingContentProviderMapper;

    private BookBorrowing devsTestBookBorrowing;
    private Book devsTestBook;
    private Book dominandoAndroid;
    private Book proAndroid4;

    public VilibraProviderFixture(Context context) {
        if(context == null) {
            throw new InvalidParameterException("Context cannot be null.");
        }
        this.context = context;
        this.bookContentProviderMapper = new BookContentProviderMapper();
        this.bookBorrowingContentProviderMapper = new BookBorrowingContentProviderMapper();
    }

    public BookBorrowing getDevsTestBookBorrowing() {
        if(this.devsTestBook == null) {
            insertDevsTestBookWithBorrowing();
        }
        return this.devsTestBookBorrowing;
    }

    public Book getDevsTestBook() {
        if(this.devsTestBook == null) {
            insertDevsTestBookWithBorrowing();
        }
        return this.devsTestBook;
    }

    public Book getDominandoAndroid() {
        if(this.dominandoAndroid == null) {
            insertDominandoAndroidTestBook();
        }
        return this.dominandoAndroid;
    }

    public Book getProAndroid4() {
        if(this.proAndroid4 == null) {
            insertProAndroid4TestBook();
        }
        return this.proAndroid4;
    }

    public void prepareTestProvider() {
        insertDevsTestBookWithBorrowing();
        insertDominandoAndroidTestBook();
        insertProAndroid4TestBook();
    }

    public void insertDevsTestBookWithBorrowing() {
        this.devsTestBook = BookFixture.getTestBookDevsTestBook();
        ContentValues testBookContentValues = bookContentProviderMapper.transform(this.devsTestBook);
        Uri insertedBook = context.getContentResolver()
                .insert(VilibraContract.BookEntry.CONTENT_URI, testBookContentValues);
        long insertedBookId = ContentUris.parseId(insertedBook);
        this.devsTestBook.setId(insertedBookId);

        this.devsTestBookBorrowing = BookBorrowingFixture.getTestBookBorrowing(this.devsTestBook);
        ContentValues testBorrowingContentValues =
                bookBorrowingContentProviderMapper.transform(this.devsTestBookBorrowing);
        Uri insertedBookBorrowing = context.getContentResolver()
                .insert(VilibraContract.LendingEntry.CONTENT_URI, testBorrowingContentValues);
        long insertedBookBorrowingId = ContentUris.parseId(insertedBookBorrowing);
        this.devsTestBookBorrowing.setId(insertedBookBorrowingId);
    }

    public void insertDominandoAndroidTestBook() {
        this.dominandoAndroid = BookFixture.getTestBookDominandoAndroid();
        ContentValues testBookContentValues =
                bookContentProviderMapper.transform(this.dominandoAndroid);
        Uri insertedBook = context.getContentResolver()
                .insert(VilibraContract.BookEntry.CONTENT_URI, testBookContentValues);
    }

    public void insertProAndroid4TestBook() {
        this.proAndroid4 = BookFixture.getTestBookProAndroid4();
        ContentValues testBookContentValues = bookContentProviderMapper.transform(this.proAndroid4);
        Uri insertedBook = context.getContentResolver()
                .insert(VilibraContract.BookEntry.CONTENT_URI, testBookContentValues);
    }

    public void clearVilibraDatabase() {
        context.getContentResolver().delete(VilibraContract.LendingEntry.CONTENT_URI, null, null);
        context.getContentResolver().delete(VilibraContract.BookEntry.CONTENT_URI, null, null);

        this.devsTestBookBorrowing = null;
        this.devsTestBook = null;
        this.dominandoAndroid = null;
        this.proAndroid4 = null;
    }

}
