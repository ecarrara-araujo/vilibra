package br.eng.ecarrara.vilibra.domain.entity;

/**
 * Someone that borrowed a book.
 */
public class BookBorrower {

    public static final BookBorrower NO_BOOK_BORROWER = new BookBorrower("");

    private final String contactInformationUri;

    public BookBorrower(String contactInformationUri) {
        this.contactInformationUri = contactInformationUri;
    }

    public String getContactInformationUri() {
        return contactInformationUri;
    }

    @Override public boolean equals(Object other) {
        if(other == this) {
            return true;
        }

        if(!(other instanceof BookBorrower)) {
            return false;
        }

        BookBorrower otherBookBorrower = (BookBorrower) other;
        if(this.contactInformationUri.equals(otherBookBorrower.getContactInformationUri())) {
            return true;
        }

        return false;
    }
}
