package extendedSchemas.otherErrors.typeNotResolved;

import base.BaseTest;
import jsound.exceptions.TypeNotResolvedException;
import org.junit.Test;

import java.io.IOException;

public class TypeNotResolvedTest extends BaseTest {

    @Test(expected = TypeNotResolvedException.class)
    public void typeNotResolvedTest() throws IOException {
        BaseTest.initializeApplication(
                "extendedSchemas/otherErrors/typeNotResolved/schema.json",
                "object/objectFile.json",
                false
        );
    }
}
