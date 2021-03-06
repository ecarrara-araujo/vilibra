package br.eng.ecarrara.vilibra.data;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import br.eng.ecarrara.vilibra.book.data.datasource.contentprovider.BookContentProviderCacheTest;
import br.eng.ecarrara.vilibra.data.mapper.BookBorrowingContentProviderMapperTest;
import br.eng.ecarrara.vilibra.data.repository.BookBorrowingContentProviderRepositoryTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        /* Data Mappers */
        BookBorrowingContentProviderMapperTest.class,

        /* Repositories */
        BookBorrowingContentProviderRepositoryTest.class,

        /* Cache */
        BookContentProviderCacheTest.class
})
public class DataLayerTestSuite {
}
