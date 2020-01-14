package extendedSchemas.atomicTypes.hexBinary.facets;

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
            "extendedSchemas/atomicTypes/hexBinary/facets/hexBinarySchema.json",
            "atomicTypes/hexBinary/facets/hexBinaryFile.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("hexBinaryLength").isHexBinaryType());
        assertTrue(schema.get("hexBinaryLength").getFacets().getDefinedFacets().contains(FacetTypes.LENGTH));
        assertEquals(8, (int) ((AtomicFacets) schema.get("hexBinaryLength").getFacets()).length);

        assertTrue(schema.get("hexBinaryMinLength").isHexBinaryType());
        assertTrue(schema.get("hexBinaryMinLength").getFacets().getDefinedFacets().contains(FacetTypes.MINLENGTH));
        assertEquals(5, (int) ((AtomicFacets) schema.get("hexBinaryMinLength").getFacets()).minLength);

        assertTrue(schema.get("hexBinaryMaxLength").isHexBinaryType());
        assertTrue(schema.get("hexBinaryMaxLength").getFacets().getDefinedFacets().contains(FacetTypes.MAXLENGTH));
        assertEquals(20, (int) ((AtomicFacets) schema.get("hexBinaryMaxLength").getFacets()).maxLength);

        assertTrue(schema.get("restrictedHexBinaryLength").isHexBinaryType());
        assertEquals(
            schema.get("restrictedHexBinaryLength").baseType.getTypeDescriptor(),
            schema.get("hexBinaryLength")
        );
        assertTrue(schema.get("restrictedHexBinaryLength").getFacets().getDefinedFacets().contains(FacetTypes.LENGTH));
        assertEquals(8, (int) ((AtomicFacets) schema.get("restrictedHexBinaryLength").getFacets()).length);

        assertTrue(schema.get("restrictedHexBinaryMinLength").isHexBinaryType());
        assertEquals(
            schema.get("restrictedHexBinaryMinLength").baseType.getTypeDescriptor(),
            schema.get("hexBinaryMinLength")
        );
        assertTrue(
            schema.get("restrictedHexBinaryMinLength").getFacets().getDefinedFacets().contains(FacetTypes.MINLENGTH)
        );
        assertEquals(6, (int) ((AtomicFacets) schema.get("restrictedHexBinaryMinLength").getFacets()).minLength);

        assertTrue(schema.get("restrictedHexBinaryMaxLength").isHexBinaryType());
        assertEquals(
            schema.get("restrictedHexBinaryMaxLength").baseType.getTypeDescriptor(),
            schema.get("hexBinaryMaxLength")
        );
        assertTrue(
            schema.get("restrictedHexBinaryMaxLength").getFacets().getDefinedFacets().contains(FacetTypes.MAXLENGTH)
        );
        assertEquals(19, (int) ((AtomicFacets) schema.get("restrictedHexBinaryMaxLength").getFacets()).maxLength);

        assertTrue(schema.get("combinedLengthFacets").isHexBinaryType());
        assertEquals(schema.get("combinedLengthFacets").baseType.getTypeDescriptor(), schema.get("hexBinaryMaxLength"));
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
