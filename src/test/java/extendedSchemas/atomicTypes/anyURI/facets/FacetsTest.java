package extendedSchemas.atomicTypes.anyURI.facets;

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
            "extendedSchemas/atomicTypes/anyURI/facets/anyURISchema.json",
            "atomicTypes/anyURI/facets/anyURIFile.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("anyURILength").isAnyURIType());
        assertTrue(schema.get("anyURILength").getFacets().getDefinedFacets().contains(FacetTypes.LENGTH));
        assertEquals(18, (int) ((AtomicFacets) schema.get("anyURILength").getFacets()).length);

        assertTrue(schema.get("anyURIMinLength").isAnyURIType());
        assertTrue(schema.get("anyURIMinLength").getFacets().getDefinedFacets().contains(FacetTypes.MINLENGTH));
        assertEquals(5, (int) ((AtomicFacets) schema.get("anyURIMinLength").getFacets()).minLength);

        assertTrue(schema.get("anyURIMaxLength").isAnyURIType());
        assertTrue(schema.get("anyURIMaxLength").getFacets().getDefinedFacets().contains(FacetTypes.MAXLENGTH));
        assertEquals(20, (int) ((AtomicFacets) schema.get("anyURIMaxLength").getFacets()).maxLength);

        assertTrue(schema.get("restrictedAnyURILength").isAnyURIType());
        assertEquals(schema.get("restrictedAnyURILength").baseType.getTypeDescriptor(), schema.get("anyURILength"));
        assertTrue(schema.get("restrictedAnyURILength").getFacets().getDefinedFacets().contains(FacetTypes.LENGTH));
        assertEquals(18, (int) ((AtomicFacets) schema.get("restrictedAnyURILength").getFacets()).length);

        assertTrue(schema.get("restrictedAnyURIMinLength").isAnyURIType());
        assertEquals(
            schema.get("restrictedAnyURIMinLength").baseType.getTypeDescriptor(),
            schema.get("anyURIMinLength")
        );
        assertTrue(
            schema.get("restrictedAnyURIMinLength").getFacets().getDefinedFacets().contains(FacetTypes.MINLENGTH)
        );
        assertEquals(6, (int) ((AtomicFacets) schema.get("restrictedAnyURIMinLength").getFacets()).minLength);

        assertTrue(schema.get("restrictedAnyURIMaxLength").isAnyURIType());
        assertEquals(
            schema.get("restrictedAnyURIMaxLength").baseType.getTypeDescriptor(),
            schema.get("anyURIMaxLength")
        );
        assertTrue(
            schema.get("restrictedAnyURIMaxLength").getFacets().getDefinedFacets().contains(FacetTypes.MAXLENGTH)
        );
        assertEquals(19, (int) ((AtomicFacets) schema.get("restrictedAnyURIMaxLength").getFacets()).maxLength);

        assertTrue(schema.get("combinedLengthFacets").isAnyURIType());
        assertEquals(schema.get("combinedLengthFacets").baseType.getTypeDescriptor(), schema.get("anyURIMaxLength"));
        assertTrue(schema.get("combinedLengthFacets").getFacets().getDefinedFacets().contains(FacetTypes.MAXLENGTH));
        assertTrue(schema.get("combinedLengthFacets").getFacets().getDefinedFacets().contains(FacetTypes.MINLENGTH));
        assertEquals(20, (int) ((AtomicFacets) schema.get("combinedLengthFacets").getFacets()).maxLength);
        assertEquals(9, (int) ((AtomicFacets) schema.get("combinedLengthFacets").getFacets()).minLength);
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
