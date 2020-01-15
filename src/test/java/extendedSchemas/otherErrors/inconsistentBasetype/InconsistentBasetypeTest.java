package extendedSchemas.otherErrors.inconsistentBasetype;

import base.BaseTest;
import jsound.exceptions.InconsistentBaseTypeException;
import org.junit.Test;

import java.io.IOException;

public class InconsistentBasetypeTest extends BaseTest {

    @Test(expected = InconsistentBaseTypeException.class)
    public void inconsistentBaseTypeTest() throws IOException {
        BaseTest.initializeApplication(
                "extendedSchemas/otherErrors/inconsistentBasetype/schema.json",
                "object/objectFile.json",
                false
        );
    }
}
