package extendedSchemas.atomicTypes.base64Binary.facets;

import base.BaseTest;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FacetsTest extends BaseTest {
    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/atomicTypes/base64Binary/facets/base64BinarySchema.json",
            "atomicTypes/base64Binary/facets/base64BinaryFile.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("base64BinaryLength").isBase64BinaryType());
        assertTrue(schema.get("base64BinaryLength").getFacets().getDefinedFacets().contains(FacetTypes.LENGTH));
        assertEquals(8, (int) ((AtomicFacets) schema.get("base64BinaryLength").getFacets()).length);

        assertTrue(schema.get("base64BinaryMinLength").isBase64BinaryType());
        assertTrue(schema.get("base64BinaryMinLength").getFacets().getDefinedFacets().contains(FacetTypes.MINLENGTH));
        assertEquals(5, (int) ((AtomicFacets) schema.get("base64BinaryMinLength").getFacets()).minLength);

        assertTrue(schema.get("base64BinaryMaxLength").isBase64BinaryType());
        assertTrue(schema.get("base64BinaryMaxLength").getFacets().getDefinedFacets().contains(FacetTypes.MAXLENGTH));
        assertEquals(20, (int) ((AtomicFacets) schema.get("base64BinaryMaxLength").getFacets()).maxLength);

        assertTrue(schema.get("restrictedBase64BinaryLength").isBase64BinaryType());
        assertEquals(schema.get("restrictedBase64BinaryLength").baseType.getTypeDescriptor(), schema.get("base64BinaryLength"));
        assertTrue(schema.get("restrictedBase64BinaryLength").getFacets().getDefinedFacets().contains(FacetTypes.LENGTH));
        assertEquals(8, (int) ((AtomicFacets) schema.get("restrictedBase64BinaryLength").getFacets()).length);

        assertTrue(schema.get("restrictedBase64BinaryMinLength").isBase64BinaryType());
        assertEquals(
            schema.get("restrictedBase64BinaryMinLength").baseType.getTypeDescriptor(),
            schema.get("base64BinaryMinLength")
        );
        assertTrue(
            schema.get("restrictedBase64BinaryMinLength").getFacets().getDefinedFacets().contains(FacetTypes.MINLENGTH)
        );
        assertEquals(6, (int) ((AtomicFacets) schema.get("restrictedBase64BinaryMinLength").getFacets()).minLength);

        assertTrue(schema.get("restrictedBase64BinaryMaxLength").isBase64BinaryType());
        assertEquals(
            schema.get("restrictedBase64BinaryMaxLength").baseType.getTypeDescriptor(),
            schema.get("base64BinaryMaxLength")
        );
        assertTrue(
            schema.get("restrictedBase64BinaryMaxLength").getFacets().getDefinedFacets().contains(FacetTypes.MAXLENGTH)
        );
        assertEquals(19, (int) ((AtomicFacets) schema.get("restrictedBase64BinaryMaxLength").getFacets()).maxLength);

        assertTrue(schema.get("combinedLengthFacets").isBase64BinaryType());
        assertEquals(schema.get("combinedLengthFacets").baseType.getTypeDescriptor(), schema.get("base64BinaryMaxLength"));
        assertTrue(schema.get("combinedLengthFacets").getFacets().getDefinedFacets().contains(FacetTypes.MAXLENGTH));
        assertTrue(schema.get("combinedLengthFacets").getFacets().getDefinedFacets().contains(FacetTypes.MINLENGTH));
        assertEquals(20, (int) ((AtomicFacets) schema.get("combinedLengthFacets").getFacets()).maxLength);
        assertEquals(7, (int) ((AtomicFacets) schema.get("combinedLengthFacets").getFacets()).minLength);
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
