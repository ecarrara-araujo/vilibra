package ecarrara.eng.vilibra.data;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ecarrara.eng.vilibra.data.cache.BookContentProviderCacheTest;
import ecarrara.eng.vilibra.data.mapper.BookBorrowingContentProviderMapperTest;
import ecarrara.eng.vilibra.data.mapper.BookContentProviderMapperTest;
import ecarrara.eng.vilibra.data.mapper.BookRestApiJsonMapperTest;
import ecarrara.eng.vilibra.data.repository.BookBorrowingContentProviderRepositoryTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        /* Data Mappers */
        BookContentProviderMapperTest.class,
        BookRestApiJsonMapperTest.class,
        BookBorrowingContentProviderMapperTest.class,

        /* Repositories */
        BookBorrowingContentProviderRepositoryTest.class,

        /* Cache */
        BookContentProviderCacheTest.class
})
public class DataLayerTestSuite {
}
