package extendedSchemas.atomicTypes.anyURI;

import base.BaseTest;
import jsound.exceptions.InvalidSchemaException;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertFalse;

public class InvalidDefaultTest extends BaseTest {
    private static final String filePath = "atomicTypes/anyURI/enumeration/anyURIEnumeration.json";
    protected static boolean compact = false;

    @Test(expected = InvalidSchemaException.class)
    public void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/anyURI/invalidDefaultSchema.json";
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
        assertFalse(
            schema.get("anyURIObj")
                .getFacets()
                .getObjectContent()
                .get("myAnyURI")
                .getTypeOrReference()
                .getTypeDescriptor()
                .validate(
                    schema.get("anyURIObj").getFacets().getObjectContent().get("myAnyURI").getDefaultValue(),
                    false
                )
        );
    }
}
