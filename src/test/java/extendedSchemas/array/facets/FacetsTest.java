package extendedSchemas.array.facets;

import base.BaseTest;
import jsound.facets.ArrayFacets;
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
            "extendedSchemas/array/facets/arraySchema.json",
            "array/facets/arrayFile.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("arrayMinLength").isArrayType());
        assertTrue(schema.get("arrayMinLength").getFacets().getDefinedFacets().contains(FacetTypes.MINLENGTH));
        assertEquals(2, (int) ((ArrayFacets) schema.get("arrayMinLength").getFacets()).minLength);

        assertTrue(schema.get("arrayMaxLength").isArrayType());
        assertTrue(schema.get("arrayMaxLength").getFacets().getDefinedFacets().contains(FacetTypes.MAXLENGTH));
        assertEquals(5, (int) ((ArrayFacets) schema.get("arrayMaxLength").getFacets()).maxLength);

        assertTrue(schema.get("restrictedArrayMinLength").isArrayType());
        assertEquals(
            schema.get("restrictedArrayMinLength").baseType.getTypeDescriptor(),
            schema.get("arrayMinLength")
        );
        assertTrue(
            schema.get("restrictedArrayMinLength").getFacets().getDefinedFacets().contains(FacetTypes.MINLENGTH)
        );
        assertEquals(3, (int) ((ArrayFacets) schema.get("restrictedArrayMinLength").getFacets()).minLength);

        assertTrue(schema.get("restrictedArrayMaxLength").isArrayType());
        assertEquals(
            schema.get("restrictedArrayMaxLength").baseType.getTypeDescriptor(),
            schema.get("arrayMaxLength")
        );
        assertTrue(
            schema.get("restrictedArrayMaxLength").getFacets().getDefinedFacets().contains(FacetTypes.MAXLENGTH)
        );
        assertEquals(4, (int) ((ArrayFacets) schema.get("restrictedArrayMaxLength").getFacets()).maxLength);

        assertTrue(schema.get("combinedLengthFacets").isArrayType());
        assertEquals(schema.get("combinedLengthFacets").baseType.getTypeDescriptor(), schema.get("arrayMaxLength"));
        assertTrue(schema.get("combinedLengthFacets").getFacets().getDefinedFacets().contains(FacetTypes.MAXLENGTH));
        assertTrue(schema.get("combinedLengthFacets").getFacets().getDefinedFacets().contains(FacetTypes.MINLENGTH));
        assertEquals(5, (int) ((ArrayFacets) schema.get("combinedLengthFacets").getFacets()).maxLength);
        assertEquals(1, (int) ((ArrayFacets) schema.get("combinedLengthFacets").getFacets()).minLength);
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
