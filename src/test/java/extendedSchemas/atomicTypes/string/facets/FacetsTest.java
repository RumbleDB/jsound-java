package extendedSchemas.atomicTypes.string.facets;

import base.BaseTest;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FacetsTest extends BaseTest {
    String filePath = "atomicTypes/string/facets/stringFile.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/string/facets/stringSchema.json",
                "targetType",
                false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("stringLength").isStringType());
        assertTrue(schema.get("stringLength").getFacets().getDefinedFacets().contains(FacetTypes.LENGTH));
        assertEquals(10, (int) ((AtomicFacets) schema.get("stringLength").getFacets()).length);

        assertTrue(schema.get("stringMinLength").isStringType());
        assertTrue(schema.get("stringMinLength").getFacets().getDefinedFacets().contains(FacetTypes.MINLENGTH));
        assertEquals(5, (int) ((AtomicFacets) schema.get("stringMinLength").getFacets()).minLength);

        assertTrue(schema.get("stringMaxLength").isStringType());
        assertTrue(schema.get("stringMaxLength").getFacets().getDefinedFacets().contains(FacetTypes.MAXLENGTH));
        assertEquals(20, (int) ((AtomicFacets) schema.get("stringMaxLength").getFacets()).maxLength);

        assertTrue(schema.get("restrictedStringLength").isStringType());
        assertEquals(schema.get("restrictedStringLength").baseType.getTypeDescriptor(), schema.get("stringLength"));
        assertTrue(schema.get("restrictedStringLength").getFacets().getDefinedFacets().contains(FacetTypes.LENGTH));
        assertEquals(10, (int) ((AtomicFacets) schema.get("restrictedStringLength").getFacets()).length);

        assertTrue(schema.get("restrictedStringMinLength").isStringType());
        assertEquals(
            schema.get("restrictedStringMinLength").baseType.getTypeDescriptor(),
            schema.get("stringMinLength")
        );
        assertTrue(
            schema.get("restrictedStringMinLength").getFacets().getDefinedFacets().contains(FacetTypes.MINLENGTH)
        );
        assertEquals(6, (int) ((AtomicFacets) schema.get("restrictedStringMinLength").getFacets()).minLength);

        assertTrue(schema.get("restrictedStringMaxLength").isStringType());
        assertEquals(
            schema.get("restrictedStringMaxLength").baseType.getTypeDescriptor(),
            schema.get("stringMaxLength")
        );
        assertTrue(
            schema.get("restrictedStringMaxLength").getFacets().getDefinedFacets().contains(FacetTypes.MAXLENGTH)
        );
        assertEquals(19, (int) ((AtomicFacets) schema.get("restrictedStringMaxLength").getFacets()).maxLength);

        assertTrue(schema.get("combinedLengthFacets").isStringType());
        assertEquals(schema.get("combinedLengthFacets").baseType.getTypeDescriptor(), schema.get("stringMaxLength"));
        assertTrue(schema.get("combinedLengthFacets").getFacets().getDefinedFacets().contains(FacetTypes.MAXLENGTH));
        assertTrue(schema.get("combinedLengthFacets").getFacets().getDefinedFacets().contains(FacetTypes.MINLENGTH));
        assertEquals(20, (int) ((AtomicFacets) schema.get("combinedLengthFacets").getFacets()).maxLength);
        assertEquals(9, (int) ((AtomicFacets) schema.get("combinedLengthFacets").getFacets()).minLength);
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
