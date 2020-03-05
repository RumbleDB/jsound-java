package extendedSchemas.atomicTypes.doubleType.facets;

import base.BaseTest;
import jsound.atomicItems.DoubleItem;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoundariesFacetsTest extends BaseTest {
    String filePath = "atomicTypes/double/facets/doubleBoundariesFile.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/double/facets/doubleBoundariesSchema.json",
                "targetType",
                false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("doubleMinInclusive").isDoubleType());
        assertTrue(schema.get("doubleMinInclusive").getFacets().getDefinedFacets().contains(FacetTypes.MININCLUSIVE));
        assertEquals(
            new DoubleItem(1.234e2),
            ((AtomicFacets) schema.get("doubleMinInclusive").getFacets()).minInclusive.getItem()
        );

        assertTrue(schema.get("doubleMinExclusive").isDoubleType());
        assertTrue(schema.get("doubleMinExclusive").getFacets().getDefinedFacets().contains(FacetTypes.MINEXCLUSIVE));
        assertEquals(
            new DoubleItem(1.234e2),
            ((AtomicFacets) schema.get("doubleMinExclusive").getFacets()).minExclusive.getItem()
        );

        assertTrue(schema.get("doubleMaxInclusive").isDoubleType());
        assertTrue(schema.get("doubleMaxInclusive").getFacets().getDefinedFacets().contains(FacetTypes.MAXINCLUSIVE));
        assertEquals(
            new DoubleItem(1.234e2),
            ((AtomicFacets) schema.get("doubleMaxInclusive").getFacets()).maxInclusive.getItem()
        );

        assertTrue(schema.get("doubleMaxExclusive").isDoubleType());
        assertTrue(schema.get("doubleMaxExclusive").getFacets().getDefinedFacets().contains(FacetTypes.MAXEXCLUSIVE));
        assertEquals(
            new DoubleItem(1.234e2),
            ((AtomicFacets) schema.get("doubleMaxExclusive").getFacets()).maxExclusive.getItem()
        );

        assertTrue(schema.get("restrictedDoubleMinInclusive").isDoubleType());
        assertEquals(
            schema.get("restrictedDoubleMinInclusive").baseType.getTypeDescriptor(),
            schema.get("doubleMinInclusive")
        );
        assertTrue(
            schema.get("restrictedDoubleMinInclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MININCLUSIVE)
        );
        assertEquals(
            new DoubleItem((double) 200),
            ((AtomicFacets) schema.get("restrictedDoubleMinInclusive").getFacets()).minInclusive.getItem()
        );

        assertTrue(schema.get("restrictedDoubleMinExclusive").isDoubleType());
        assertEquals(
            schema.get("restrictedDoubleMinExclusive").baseType.getTypeDescriptor(),
            schema.get("doubleMinExclusive")
        );
        assertTrue(
            schema.get("restrictedDoubleMinExclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MINEXCLUSIVE)
        );
        assertEquals(
            new DoubleItem(123.5),
            ((AtomicFacets) schema.get("restrictedDoubleMinExclusive").getFacets()).minExclusive.getItem()
        );

        assertTrue(schema.get("restrictedDoubleMaxInclusive").isDoubleType());
        assertEquals(
            schema.get("restrictedDoubleMaxInclusive").baseType.getTypeDescriptor(),
            schema.get("doubleMaxInclusive")
        );
        assertTrue(
            schema.get("restrictedDoubleMaxInclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            new DoubleItem((double) 123),
            ((AtomicFacets) schema.get("restrictedDoubleMaxInclusive").getFacets()).maxInclusive.getItem()
        );

        assertTrue(schema.get("restrictedDoubleMaxExclusive").isDoubleType());
        assertEquals(
            schema.get("restrictedDoubleMaxExclusive").baseType.getTypeDescriptor(),
            schema.get("doubleMaxExclusive")
        );
        assertTrue(
            schema.get("restrictedDoubleMaxExclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MAXEXCLUSIVE)
        );
        assertEquals(
            new DoubleItem(1.234E1),
            ((AtomicFacets) schema.get("restrictedDoubleMaxExclusive").getFacets()).maxExclusive.getItem()
        );

        assertTrue(schema.get("combinedBoundariesFacets").isDoubleType());
        assertEquals(
            schema.get("combinedBoundariesFacets").baseType.getTypeDescriptor(),
            schema.get("doubleMaxInclusive")
        );
        assertTrue(
            schema.get("combinedBoundariesFacets").getFacets().getDefinedFacets().contains(FacetTypes.MININCLUSIVE)
        );
        assertTrue(
            schema.get("combinedBoundariesFacets").getFacets().getDefinedFacets().contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            new DoubleItem(1.234e2),
            ((AtomicFacets) schema.get("combinedBoundariesFacets").getFacets()).maxInclusive.getItem()
        );
        assertEquals(
            new DoubleItem(0.5),
            ((AtomicFacets) schema.get("combinedBoundariesFacets").getFacets()).minInclusive.getItem()
        );
    }



    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
