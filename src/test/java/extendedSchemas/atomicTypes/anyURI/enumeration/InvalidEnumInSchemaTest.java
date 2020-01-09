package extendedSchemas.atomicTypes.anyURI.enumeration;

import base.BaseTest;
import jsound.exceptions.InvalidEnumValueException;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertFalse;

public class InvalidEnumInSchemaTest extends BaseTest {
    private static final String filePath = "atomicTypes/anyURI/enumeration/anyURIEnumeration.json";
    protected static boolean compact = false;

    @Test(expected = InvalidEnumValueException.class)
    public void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/anyURI/invalidEnumInSchema.json";
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
        assertFalse(
            schema.get("anyURIType")
                .validate(
                    schema.get("anyURIType")
                        .getFacets()
                        .getEnumeration()
                        .get(1),
                    false
                )
        );
    }
}
