package extendedSchemas.atomicTypes.decimal.facets;

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

public class DigitsFacetsTest extends BaseTest {
    String filePath = "atomicTypes/decimal/facets/decimalDigitsFile.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/decimal/facets/decimalDigitsSchema.json",
                "targetType",
                false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("decimalTotDigits").isDecimalType());
        assertTrue(schema.get("decimalTotDigits").getFacets().getDefinedFacets().contains(FacetTypes.TOTALDIGITS));
        assertEquals(5, (int) ((AtomicFacets) schema.get("decimalTotDigits").getFacets()).totalDigits);

        assertTrue(schema.get("decimalFracDigits").isDecimalType());
        assertTrue(schema.get("decimalFracDigits").getFacets().getDefinedFacets().contains(FacetTypes.FRACTIONDIGITS));
        assertEquals(4, (int) ((AtomicFacets) schema.get("decimalFracDigits").getFacets()).fractionDigits);

        assertTrue(schema.get("restrictedDecimalTotDigits").isDecimalType());
        assertEquals(
            schema.get("restrictedDecimalTotDigits").baseType.getTypeDescriptor(),
            schema.get("decimalTotDigits")
        );
        assertTrue(
            schema.get("restrictedDecimalTotDigits").getFacets().getDefinedFacets().contains(FacetTypes.TOTALDIGITS)
        );
        assertEquals(4, (int) ((AtomicFacets) schema.get("restrictedDecimalTotDigits").getFacets()).totalDigits);

        assertTrue(schema.get("restrictedDecimalFracDigits").isDecimalType());
        assertEquals(
            schema.get("restrictedDecimalFracDigits").baseType.getTypeDescriptor(),
            schema.get("decimalFracDigits")
        );
        assertTrue(
            schema.get("restrictedDecimalFracDigits").getFacets().getDefinedFacets().contains(FacetTypes.FRACTIONDIGITS)
        );
        assertEquals(3, (int) ((AtomicFacets) schema.get("restrictedDecimalFracDigits").getFacets()).fractionDigits);

        assertTrue(schema.get("combinedDigitsFacets").isDecimalType());
        assertEquals(schema.get("combinedDigitsFacets").baseType.getTypeDescriptor(), schema.get("decimalTotDigits"));
        assertTrue(schema.get("combinedDigitsFacets").getFacets().getDefinedFacets().contains(FacetTypes.TOTALDIGITS));
        assertTrue(
            schema.get("combinedDigitsFacets").getFacets().getDefinedFacets().contains(FacetTypes.FRACTIONDIGITS)
        );
        assertEquals(5, (int) ((AtomicFacets) schema.get("combinedDigitsFacets").getFacets()).totalDigits);
        assertEquals(4, (int) ((AtomicFacets) schema.get("combinedDigitsFacets").getFacets()).fractionDigits);
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
