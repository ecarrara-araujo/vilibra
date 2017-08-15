package br.eng.ecarrara.vilibra;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import br.eng.ecarrara.vilibra.data.DataLayerTestSuite;
import br.eng.ecarrara.vilibra.domain.DomainLayerTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DataLayerTestSuite.class,
        DomainLayerTestSuite.class,
})
public class FullTestSuite {
}
