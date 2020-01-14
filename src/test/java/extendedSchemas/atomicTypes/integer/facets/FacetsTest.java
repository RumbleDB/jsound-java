package extendedSchemas.atomicTypes.integer.facets;

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
            "extendedSchemas/atomicTypes/integer/facets/integerSchema.json",
            "atomicTypes/integer/facets/integerFile.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("integerTotDigits").isIntegerType());
        assertTrue(schema.get("integerTotDigits").getFacets().getDefinedFacets().contains(FacetTypes.TOTALDIGITS));
        assertEquals(5, (int) ((AtomicFacets) schema.get("integerTotDigits").getFacets()).totalDigits);

        assertTrue(schema.get("integerFracDigits").isIntegerType());
        assertTrue(schema.get("integerFracDigits").getFacets().getDefinedFacets().contains(FacetTypes.FRACTIONDIGITS));
        assertEquals(4, (int) ((AtomicFacets) schema.get("integerFracDigits").getFacets()).fractionDigits);

        assertTrue(schema.get("restrictedIntegerTotDigits").isIntegerType());
        assertEquals(
            schema.get("restrictedIntegerTotDigits").baseType.getTypeDescriptor(),
            schema.get("integerTotDigits")
        );
        assertTrue(
            schema.get("restrictedIntegerTotDigits").getFacets().getDefinedFacets().contains(FacetTypes.TOTALDIGITS)
        );
        assertEquals(4, (int) ((AtomicFacets) schema.get("restrictedIntegerTotDigits").getFacets()).totalDigits);

        assertTrue(schema.get("restrictedIntegerFracDigits").isIntegerType());
        assertEquals(
            schema.get("restrictedIntegerFracDigits").baseType.getTypeDescriptor(),
            schema.get("integerFracDigits")
        );
        assertTrue(
            schema.get("restrictedIntegerFracDigits").getFacets().getDefinedFacets().contains(FacetTypes.FRACTIONDIGITS)
        );
        assertEquals(3, (int) ((AtomicFacets) schema.get("restrictedIntegerFracDigits").getFacets()).fractionDigits);

        assertTrue(schema.get("combinedDigitsFacets").isIntegerType());
        assertEquals(schema.get("combinedDigitsFacets").baseType.getTypeDescriptor(), schema.get("integerTotDigits"));
        assertTrue(schema.get("combinedDigitsFacets").getFacets().getDefinedFacets().contains(FacetTypes.TOTALDIGITS));
        assertTrue(
            schema.get("combinedDigitsFacets").getFacets().getDefinedFacets().contains(FacetTypes.FRACTIONDIGITS)
        );
        assertEquals(5, (int) ((AtomicFacets) schema.get("combinedDigitsFacets").getFacets()).totalDigits);
        assertEquals(4, (int) ((AtomicFacets) schema.get("combinedDigitsFacets").getFacets()).fractionDigits);
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
