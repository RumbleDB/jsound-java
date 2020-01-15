package extendedSchemas.atomicTypes.yearMonthDuration.facets;

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

public class BoundariesFacetsTest extends BaseTest {
    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/atomicTypes/yearMonthDuration/facets/yearMonthDurationBoundariesSchema.json",
            "atomicTypes/yearMonthDuration/facets/yearMonthDurationBoundariesFile.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("yearMonthDurationMinInclusive").isYearMonthDurationType());
        assertTrue(
            schema.get("yearMonthDurationMinInclusive").getFacets().getDefinedFacets().contains(FacetTypes.MININCLUSIVE)
        );
        assertEquals(
            "P2Y5M",
            ((AtomicFacets) schema.get("yearMonthDurationMinInclusive").getFacets()).minInclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("yearMonthDurationMinExclusive").isYearMonthDurationType());
        assertTrue(
            schema.get("yearMonthDurationMinExclusive").getFacets().getDefinedFacets().contains(FacetTypes.MINEXCLUSIVE)
        );
        assertEquals(
            "P2Y5M",
            ((AtomicFacets) schema.get("yearMonthDurationMinExclusive").getFacets()).minExclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("yearMonthDurationMaxInclusive").isYearMonthDurationType());
        assertTrue(
            schema.get("yearMonthDurationMaxInclusive").getFacets().getDefinedFacets().contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            "P2Y5M",
            ((AtomicFacets) schema.get("yearMonthDurationMaxInclusive").getFacets()).maxInclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("yearMonthDurationMaxExclusive").isYearMonthDurationType());
        assertTrue(
            schema.get("yearMonthDurationMaxExclusive").getFacets().getDefinedFacets().contains(FacetTypes.MAXEXCLUSIVE)
        );
        assertEquals(
            "P2Y5M",
            ((AtomicFacets) schema.get("yearMonthDurationMaxExclusive").getFacets()).maxExclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedYearMonthDurationMinInclusive").isYearMonthDurationType());
        assertEquals(
            schema.get("restrictedYearMonthDurationMinInclusive").baseType.getTypeDescriptor(),
            schema.get("yearMonthDurationMinInclusive")
        );
        assertTrue(
            schema.get("restrictedYearMonthDurationMinInclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MININCLUSIVE)
        );
        assertEquals(
            "P2Y6M",
            ((AtomicFacets) schema.get("restrictedYearMonthDurationMinInclusive").getFacets()).minInclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedYearMonthDurationMinExclusive").isYearMonthDurationType());
        assertEquals(
            schema.get("restrictedYearMonthDurationMinExclusive").baseType.getTypeDescriptor(),
            schema.get("yearMonthDurationMinExclusive")
        );
        assertTrue(
            schema.get("restrictedYearMonthDurationMinExclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MINEXCLUSIVE)
        );
        assertEquals(
            "P2Y6M",
            ((AtomicFacets) schema.get("restrictedYearMonthDurationMinExclusive").getFacets()).minExclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedYearMonthDurationMaxInclusive").isYearMonthDurationType());
        assertEquals(
            schema.get("restrictedYearMonthDurationMaxInclusive").baseType.getTypeDescriptor(),
            schema.get("yearMonthDurationMaxInclusive")
        );
        assertTrue(
            schema.get("restrictedYearMonthDurationMaxInclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            "P2Y4M",
            ((AtomicFacets) schema.get("restrictedYearMonthDurationMaxInclusive").getFacets()).maxInclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedYearMonthDurationMaxExclusive").isYearMonthDurationType());
        assertEquals(
            schema.get("restrictedYearMonthDurationMaxExclusive").baseType.getTypeDescriptor(),
            schema.get("yearMonthDurationMaxExclusive")
        );
        assertTrue(
            schema.get("restrictedYearMonthDurationMaxExclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MAXEXCLUSIVE)
        );
        assertEquals(
            "P2Y4M",
            ((AtomicFacets) schema.get("restrictedYearMonthDurationMaxExclusive").getFacets()).maxExclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("combinedBoundariesFacets").isYearMonthDurationType());
        assertEquals(
            schema.get("combinedBoundariesFacets").baseType.getTypeDescriptor(),
            schema.get("yearMonthDurationMaxInclusive")
        );
        assertTrue(
            schema.get("combinedBoundariesFacets").getFacets().getDefinedFacets().contains(FacetTypes.MININCLUSIVE)
        );
        assertTrue(
            schema.get("combinedBoundariesFacets").getFacets().getDefinedFacets().contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            "P2Y5M",
            ((AtomicFacets) schema.get("combinedBoundariesFacets").getFacets()).maxInclusive.getItem().getStringValue()
        );
        assertEquals(
            "P2Y1M",
            ((AtomicFacets) schema.get("combinedBoundariesFacets").getFacets()).minInclusive.getItem().getStringValue()
        );
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
