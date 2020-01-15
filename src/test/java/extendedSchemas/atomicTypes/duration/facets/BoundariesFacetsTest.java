package extendedSchemas.atomicTypes.duration.facets;

import base.BaseTest;
import jsound.atomicItems.DurationItem;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import jsound.types.ItemTypes;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static jsound.atomicItems.DurationItem.getDurationFromString;
import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoundariesFacetsTest extends BaseTest {
    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/atomicTypes/duration/facets/durationBoundariesSchema.json",
            "atomicTypes/duration/facets/durationBoundariesFile.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("durationMinInclusive").isDurationType());
        assertTrue(schema.get("durationMinInclusive").getFacets().getDefinedFacets().contains(FacetTypes.MININCLUSIVE));
        assertEquals(
            "P2MT5H",
            ((AtomicFacets) schema.get("durationMinInclusive").getFacets()).minInclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("durationMinExclusive").isDurationType());
        assertTrue(schema.get("durationMinExclusive").getFacets().getDefinedFacets().contains(FacetTypes.MINEXCLUSIVE));
        assertEquals(
            "P2MT5H",
            ((AtomicFacets) schema.get("durationMinExclusive").getFacets()).minExclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("durationMaxInclusive").isDurationType());
        assertTrue(schema.get("durationMaxInclusive").getFacets().getDefinedFacets().contains(FacetTypes.MAXINCLUSIVE));
        assertEquals(
            "P2MT5H",
            ((AtomicFacets) schema.get("durationMaxInclusive").getFacets()).maxInclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("durationMaxExclusive").isDurationType());
        assertTrue(schema.get("durationMaxExclusive").getFacets().getDefinedFacets().contains(FacetTypes.MAXEXCLUSIVE));
        assertEquals(
            "P2MT5H",
            ((AtomicFacets) schema.get("durationMaxExclusive").getFacets()).maxExclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("restrictedDurationMinInclusive").isDurationType());
        assertEquals(
            schema.get("restrictedDurationMinInclusive").baseType.getTypeDescriptor(),
            schema.get("durationMinInclusive")
        );
        assertTrue(
            schema.get("restrictedDurationMinInclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MININCLUSIVE)
        );
        assertEquals(
            "P2MT6H",
            ((AtomicFacets) schema.get("restrictedDurationMinInclusive").getFacets()).minInclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedDurationMinExclusive").isDurationType());
        assertEquals(
            schema.get("restrictedDurationMinExclusive").baseType.getTypeDescriptor(),
            schema.get("durationMinExclusive")
        );
        assertTrue(
            schema.get("restrictedDurationMinExclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MINEXCLUSIVE)
        );
        assertEquals(
            "P2MT6H",
            ((AtomicFacets) schema.get("restrictedDurationMinExclusive").getFacets()).minExclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedDurationMaxInclusive").isDurationType());
        assertEquals(
            schema.get("restrictedDurationMaxInclusive").baseType.getTypeDescriptor(),
            schema.get("durationMaxInclusive")
        );
        assertTrue(
            schema.get("restrictedDurationMaxInclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            "P2MT4H",
            ((AtomicFacets) schema.get("restrictedDurationMaxInclusive").getFacets()).maxInclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedDurationMaxExclusive").isDurationType());
        assertEquals(
            schema.get("restrictedDurationMaxExclusive").baseType.getTypeDescriptor(),
            schema.get("durationMaxExclusive")
        );
        assertTrue(
            schema.get("restrictedDurationMaxExclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MAXEXCLUSIVE)
        );
        assertEquals(
            "P2MT4H",
            ((AtomicFacets) schema.get("restrictedDurationMaxExclusive").getFacets()).maxExclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("combinedBoundariesFacets").isDurationType());
        assertEquals(
            schema.get("combinedBoundariesFacets").baseType.getTypeDescriptor(),
            schema.get("durationMaxInclusive")
        );
        assertTrue(
            schema.get("combinedBoundariesFacets").getFacets().getDefinedFacets().contains(FacetTypes.MININCLUSIVE)
        );
        assertTrue(
            schema.get("combinedBoundariesFacets").getFacets().getDefinedFacets().contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            "P2MT5H",
            ((AtomicFacets) schema.get("combinedBoundariesFacets").getFacets()).maxInclusive.getItem().getStringValue()
        );
        assertEquals(
            "P2MT1H",
            ((AtomicFacets) schema.get("combinedBoundariesFacets").getFacets()).minInclusive.getItem().getStringValue()
        );
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
