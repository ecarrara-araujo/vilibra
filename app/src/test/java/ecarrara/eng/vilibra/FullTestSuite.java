package ecarrara.eng.vilibra;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ecarrara.eng.vilibra.data.DataLayerTestSuite;
import ecarrara.eng.vilibra.domain.DomainLayerTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DataLayerTestSuite.class,
        DomainLayerTestSuite.class,
})
public class FullTestSuite {
}
