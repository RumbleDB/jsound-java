package extendedSchemas.facets.minExclusive;

import base.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class InvalidMinExclusiveFacetTest extends BaseTest {

    @Parameterized.Parameter
    public int fileNumber;

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Object[] data() {
        return new Object[] { 1, 2, 3, 4, 5 };
    }

    @Test(expected = IllegalArgumentException.class)
    public void lengthFacetTest() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/facets/minExclusive/invalidMinExclusiveFacet" + fileNumber + ".json",
            "atomicTypes/dateTime/facets/dateTimeFile.json",
            false
        );

        assertFalse(schemaItem.validate(fileItem, false));
    }
}
