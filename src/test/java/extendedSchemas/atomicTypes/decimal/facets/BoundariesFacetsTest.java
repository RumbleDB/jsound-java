package extendedSchemas.atomicTypes.decimal.facets;

import base.BaseTest;
import jsound.atomicItems.DecimalItem;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoundariesFacetsTest extends BaseTest {
    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/atomicTypes/decimal/facets/decimalBoundariesSchema.json",
            "atomicTypes/decimal/facets/decimalBoundariesFile.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("decimalMinInclusive").isDecimalType());
        assertTrue(schema.get("decimalMinInclusive").getFacets().getDefinedFacets().contains(FacetTypes.MININCLUSIVE));
        assertEquals(
            new DecimalItem(BigDecimal.valueOf(1.23)),
            ((AtomicFacets) schema.get("decimalMinInclusive").getFacets()).minInclusive.getItem()
        );

        assertTrue(schema.get("decimalMinExclusive").isDecimalType());
        assertTrue(schema.get("decimalMinExclusive").getFacets().getDefinedFacets().contains(FacetTypes.MINEXCLUSIVE));
        assertEquals(
            new DecimalItem(BigDecimal.valueOf(1.23)),
            ((AtomicFacets) schema.get("decimalMinExclusive").getFacets()).minExclusive.getItem()
        );

        assertTrue(schema.get("decimalMaxInclusive").isDecimalType());
        assertTrue(schema.get("decimalMaxInclusive").getFacets().getDefinedFacets().contains(FacetTypes.MAXINCLUSIVE));
        assertEquals(
            new DecimalItem(BigDecimal.valueOf(1.23)),
            ((AtomicFacets) schema.get("decimalMaxInclusive").getFacets()).maxInclusive.getItem()
        );

        assertTrue(schema.get("decimalMaxExclusive").isDecimalType());
        assertTrue(schema.get("decimalMaxExclusive").getFacets().getDefinedFacets().contains(FacetTypes.MAXEXCLUSIVE));
        assertEquals(
            new DecimalItem(BigDecimal.valueOf(1.23)),
            ((AtomicFacets) schema.get("decimalMaxExclusive").getFacets()).maxExclusive.getItem()
        );

        assertTrue(schema.get("restrictedDecimalMinInclusive").isDecimalType());
        assertEquals(
            schema.get("restrictedDecimalMinInclusive").baseType.getTypeDescriptor(),
            schema.get("decimalMinInclusive")
        );
        assertTrue(
            schema.get("restrictedDecimalMinInclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MININCLUSIVE)
        );
        assertEquals(
            new DecimalItem(BigDecimal.valueOf(2)),
            ((AtomicFacets) schema.get("restrictedDecimalMinInclusive").getFacets()).minInclusive.getItem()
        );

        assertTrue(schema.get("restrictedDecimalMinExclusive").isDecimalType());
        assertEquals(
            schema.get("restrictedDecimalMinExclusive").baseType.getTypeDescriptor(),
            schema.get("decimalMinExclusive")
        );
        assertTrue(
            schema.get("restrictedDecimalMinExclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MINEXCLUSIVE)
        );
        assertEquals(
            new DecimalItem(BigDecimal.valueOf(2)),
            ((AtomicFacets) schema.get("restrictedDecimalMinExclusive").getFacets()).minExclusive.getItem()
        );

        assertTrue(schema.get("restrictedDecimalMaxInclusive").isDecimalType());
        assertEquals(
            schema.get("restrictedDecimalMaxInclusive").baseType.getTypeDescriptor(),
            schema.get("decimalMaxInclusive")
        );
        assertTrue(
            schema.get("restrictedDecimalMaxInclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            new DecimalItem(BigDecimal.valueOf(1)),
            ((AtomicFacets) schema.get("restrictedDecimalMaxInclusive").getFacets()).maxInclusive.getItem()
        );

        assertTrue(schema.get("restrictedDecimalMaxExclusive").isDecimalType());
        assertEquals(
            schema.get("restrictedDecimalMaxExclusive").baseType.getTypeDescriptor(),
            schema.get("decimalMaxExclusive")
        );
        assertTrue(
            schema.get("restrictedDecimalMaxExclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MAXEXCLUSIVE)
        );
        assertEquals(
            new DecimalItem(BigDecimal.valueOf(1)),
            ((AtomicFacets) schema.get("restrictedDecimalMaxExclusive").getFacets()).maxExclusive.getItem()
        );

        assertTrue(schema.get("combinedBoundariesFacets").isDecimalType());
        assertEquals(
            schema.get("combinedBoundariesFacets").baseType.getTypeDescriptor(),
            schema.get("decimalMaxInclusive")
        );
        assertTrue(
            schema.get("combinedBoundariesFacets").getFacets().getDefinedFacets().contains(FacetTypes.MININCLUSIVE)
        );
        assertTrue(
            schema.get("combinedBoundariesFacets").getFacets().getDefinedFacets().contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            new DecimalItem(BigDecimal.valueOf(1.23)),
            ((AtomicFacets) schema.get("combinedBoundariesFacets").getFacets()).maxInclusive.getItem()
        );
        assertEquals(
            new DecimalItem(BigDecimal.valueOf(0.5)),
            ((AtomicFacets) schema.get("combinedBoundariesFacets").getFacets()).minInclusive.getItem()
        );
    }



    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
