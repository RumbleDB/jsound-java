package extendedSchemas.atomicTypes.dayTimeDuration.facets;

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
            "extendedSchemas/atomicTypes/dayTimeDuration/facets/dayTimeDurationBoundariesSchema.json",
            "atomicTypes/dayTimeDuration/facets/dayTimeDurationBoundariesFile.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("dayTimeDurationMinInclusive").isDayTimeDurationType());
        assertTrue(schema.get("dayTimeDurationMinInclusive").getFacets().getDefinedFacets().contains(FacetTypes.MININCLUSIVE));
        assertEquals(
            "P2DT5H",
            ((AtomicFacets) schema.get("dayTimeDurationMinInclusive").getFacets()).minInclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("dayTimeDurationMinExclusive").isDayTimeDurationType());
        assertTrue(schema.get("dayTimeDurationMinExclusive").getFacets().getDefinedFacets().contains(FacetTypes.MINEXCLUSIVE));
        assertEquals(
            "P2DT5H",
            ((AtomicFacets) schema.get("dayTimeDurationMinExclusive").getFacets()).minExclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("dayTimeDurationMaxInclusive").isDayTimeDurationType());
        assertTrue(schema.get("dayTimeDurationMaxInclusive").getFacets().getDefinedFacets().contains(FacetTypes.MAXINCLUSIVE));
        assertEquals(
            "P2DT5H",
            ((AtomicFacets) schema.get("dayTimeDurationMaxInclusive").getFacets()).maxInclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("dayTimeDurationMaxExclusive").isDayTimeDurationType());
        assertTrue(schema.get("dayTimeDurationMaxExclusive").getFacets().getDefinedFacets().contains(FacetTypes.MAXEXCLUSIVE));
        assertEquals(
            "P2DT5H",
            ((AtomicFacets) schema.get("dayTimeDurationMaxExclusive").getFacets()).maxExclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("restrictedDayTimeDurationMinInclusive").isDayTimeDurationType());
        assertEquals(
            schema.get("restrictedDayTimeDurationMinInclusive").baseType.getTypeDescriptor(),
            schema.get("dayTimeDurationMinInclusive")
        );
        assertTrue(
            schema.get("restrictedDayTimeDurationMinInclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MININCLUSIVE)
        );
        assertEquals(
            "P2DT6H",
            ((AtomicFacets) schema.get("restrictedDayTimeDurationMinInclusive").getFacets()).minInclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedDayTimeDurationMinExclusive").isDayTimeDurationType());
        assertEquals(
            schema.get("restrictedDayTimeDurationMinExclusive").baseType.getTypeDescriptor(),
            schema.get("dayTimeDurationMinExclusive")
        );
        assertTrue(
            schema.get("restrictedDayTimeDurationMinExclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MINEXCLUSIVE)
        );
        assertEquals(
            "P2DT6H",
            ((AtomicFacets) schema.get("restrictedDayTimeDurationMinExclusive").getFacets()).minExclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedDayTimeDurationMaxInclusive").isDayTimeDurationType());
        assertEquals(
            schema.get("restrictedDayTimeDurationMaxInclusive").baseType.getTypeDescriptor(),
            schema.get("dayTimeDurationMaxInclusive")
        );
        assertTrue(
            schema.get("restrictedDayTimeDurationMaxInclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            "P2DT4H",
            ((AtomicFacets) schema.get("restrictedDayTimeDurationMaxInclusive").getFacets()).maxInclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedDayTimeDurationMaxExclusive").isDayTimeDurationType());
        assertEquals(
            schema.get("restrictedDayTimeDurationMaxExclusive").baseType.getTypeDescriptor(),
            schema.get("dayTimeDurationMaxExclusive")
        );
        assertTrue(
            schema.get("restrictedDayTimeDurationMaxExclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MAXEXCLUSIVE)
        );
        assertEquals(
            "P2DT4H",
            ((AtomicFacets) schema.get("restrictedDayTimeDurationMaxExclusive").getFacets()).maxExclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("combinedBoundariesFacets").isDayTimeDurationType());
        assertEquals(
            schema.get("combinedBoundariesFacets").baseType.getTypeDescriptor(),
            schema.get("dayTimeDurationMaxInclusive")
        );
        assertTrue(
            schema.get("combinedBoundariesFacets").getFacets().getDefinedFacets().contains(FacetTypes.MININCLUSIVE)
        );
        assertTrue(
            schema.get("combinedBoundariesFacets").getFacets().getDefinedFacets().contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            "P2DT5H",
            ((AtomicFacets) schema.get("combinedBoundariesFacets").getFacets()).maxInclusive.getItem().getStringValue()
        );
        assertEquals(
            "P2DT1H",
            ((AtomicFacets) schema.get("combinedBoundariesFacets").getFacets()).minInclusive.getItem().getStringValue()
        );
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
