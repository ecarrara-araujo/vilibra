package br.eng.ecarrara.vilibra.domain.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Representation of a user's Book.
 */
public class Book {

    /**
     * Book id for a book that was not persisted yet.
     */
    public static final long NOT_PERSISTED_ID = -1L;

    /**
     * Book id for an empty/nonexistent book.
     */
    public static final long NONEXISTENT_ID = -2L;

    public static final Book NO_BOOK = new Builder(NONEXISTENT_ID, "").build();

    public static final Date DATE_NOT_INFORMED;
    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, 1, 1);
        DATE_NOT_INFORMED = calendar.getTime();
    }

    /**
     * Internal unique book identifier.
     */
    private long id;

    private final String title;
    private final String subtitle;
    private final List<String> authors;
    private final String publisher;
    private final Date publishedDate;
    private final Integer pageCount;
    private final String isbn10;
    private final String isbn13;

    private Book(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.subtitle = builder.subtitle;
        this.authors = builder.authors;
        this.publisher = builder.publisher;
        this.publishedDate = builder.publishedDate;
        this.pageCount = builder.pageCount;
        this.isbn10 = builder.isbn10;
        this.isbn13 = builder.isbn13;
    }

    public long getId() {
        return id;
    }

    public void setId(final long newId) { this.id = newId; }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public boolean hasIsbn(String isbn) {
        boolean hasIsbn = (this.isbn10.equals(isbn) || this.isbn13.equals(isbn));
        return hasIsbn;
    }

    @Override public boolean equals(Object other) {
        if(other == this) {
            return true;
        }

        if(!(other instanceof Book)) {
            return false;
        }

        Book otherBook = (Book) other;
        if(this.pageCount.equals(otherBook.getPageCount()) &&
                this.isbn10.equals(otherBook.getIsbn10()) &&
                this.isbn13.equals(otherBook.getIsbn13()) &&
                this.title.equals(otherBook.getTitle()) &&
                this.subtitle.equals(otherBook.getSubtitle()) &&
                this.authors.equals(otherBook.getAuthors()) &&
                this.publisher.equals(otherBook.getPublisher()) &&
                this.publishedDate.equals(otherBook.getPublishedDate())
                ) {
            return true;
        }

        return false;
    }

    public static class Builder {

        private long id;
        private final String title;
        private String subtitle;
        private List<String> authors;
        private String publisher;
        private Date publishedDate;
        private Integer pageCount;
        private String isbn10;
        private String isbn13;

        public Builder(long id, String title) {
            this.id = id;
            this.title = title;
        }

        public Builder setSubtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public Builder setAuthors(List<String> authors) {
            this.authors = authors;
            return this;
        }

        public Builder setPublisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        public Builder setPublishedDate(Date publishedDate) {
            this.publishedDate = publishedDate;
            return this;
        }

        public Builder setPageCount(Integer pageCount) {
            this.pageCount = pageCount;
            return this;
        }

        public Builder setIsbn10(String isbn10) {
            this.isbn10 = isbn10;
            return this;
        }

        public Builder setIsbn13(String isbn13) {
            this.isbn13 = isbn13;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }
}
