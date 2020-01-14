package extendedSchemas.facets.atomicTypes.length;

import base.BaseTest;
import jsound.exceptions.UnexpectedTypeException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

@RunWith(Parameterized.class)
public class InvalidLengthFacetTest extends BaseTest {

    @Parameterized.Parameter
    public int fileNumber;

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Object[] data() {
        return new Object[] {1,2,3,4,5};
    }

    @Test(expected = UnexpectedTypeException.class)
    public void lengthFacetTest() throws IOException {
        BaseTest.initializeApplication(
                "extendedSchemas/facets/atomicTypes/length/invalidLengthFacet" + fileNumber +".json",
                "atomicTypes/anyURI/facets/anyURIFile.json",
                false
        );
    }
}