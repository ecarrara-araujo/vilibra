package br.eng.ecarrara.vilibra.fakedata;

import br.eng.ecarrara.vilibra.domain.entity.BookBorrower;

public class BookBorrowerFixture {

    /**
     * Prepare a {@link BookBorrower} with some fake data to be used with tests.
     *
     * @return
     */
    public static BookBorrower getMasterBorrowerTestData() {
        return new BookBorrower("http://ecarrara.eng.vilibra/1");
    }

}
