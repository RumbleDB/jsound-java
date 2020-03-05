package extendedSchemas.atomicTypes.integer.facets;

import base.BaseTest;
import jsound.atomicItems.IntegerItem;
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
    String filePath = "atomicTypes/integer/facets/integerBoundariesFile.json";
    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/integer/facets/integerBoundariesSchema.json",
                "targetType",
                false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("integerMinInclusive").isIntegerType());
        assertTrue(schema.get("integerMinInclusive").getFacets().getDefinedFacets().contains(FacetTypes.MININCLUSIVE));
        assertEquals(
            new IntegerItem(1),
            ((AtomicFacets) schema.get("integerMinInclusive").getFacets()).minInclusive.getItem()
        );

        assertTrue(schema.get("integerMinExclusive").isIntegerType());
        assertTrue(schema.get("integerMinExclusive").getFacets().getDefinedFacets().contains(FacetTypes.MINEXCLUSIVE));
        assertEquals(
            new IntegerItem(1),
            ((AtomicFacets) schema.get("integerMinExclusive").getFacets()).minExclusive.getItem()
        );

        assertTrue(schema.get("integerMaxInclusive").isIntegerType());
        assertTrue(schema.get("integerMaxInclusive").getFacets().getDefinedFacets().contains(FacetTypes.MAXINCLUSIVE));
        assertEquals(
            new IntegerItem(1),
            ((AtomicFacets) schema.get("integerMaxInclusive").getFacets()).maxInclusive.getItem()
        );

        assertTrue(schema.get("integerMaxExclusive").isIntegerType());
        assertTrue(schema.get("integerMaxExclusive").getFacets().getDefinedFacets().contains(FacetTypes.MAXEXCLUSIVE));
        assertEquals(
            new IntegerItem(1),
            ((AtomicFacets) schema.get("integerMaxExclusive").getFacets()).maxExclusive.getItem()
        );

        assertTrue(schema.get("restrictedIntegerMinInclusive").isIntegerType());
        assertEquals(
            schema.get("restrictedIntegerMinInclusive").baseType.getTypeDescriptor(),
            schema.get("integerMinInclusive")
        );
        assertTrue(
            schema.get("restrictedIntegerMinInclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MININCLUSIVE)
        );
        assertEquals(
            new IntegerItem(2),
            ((AtomicFacets) schema.get("restrictedIntegerMinInclusive").getFacets()).minInclusive.getItem()
        );

        assertTrue(schema.get("restrictedIntegerMinExclusive").isIntegerType());
        assertEquals(
            schema.get("restrictedIntegerMinExclusive").baseType.getTypeDescriptor(),
            schema.get("integerMinExclusive")
        );
        assertTrue(
            schema.get("restrictedIntegerMinExclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MINEXCLUSIVE)
        );
        assertEquals(
            new IntegerItem(2),
            ((AtomicFacets) schema.get("restrictedIntegerMinExclusive").getFacets()).minExclusive.getItem()
        );

        assertTrue(schema.get("restrictedIntegerMaxInclusive").isIntegerType());
        assertEquals(
            schema.get("restrictedIntegerMaxInclusive").baseType.getTypeDescriptor(),
            schema.get("integerMaxInclusive")
        );
        assertTrue(
            schema.get("restrictedIntegerMaxInclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            new IntegerItem(0),
            ((AtomicFacets) schema.get("restrictedIntegerMaxInclusive").getFacets()).maxInclusive.getItem()
        );

        assertTrue(schema.get("restrictedIntegerMaxExclusive").isIntegerType());
        assertEquals(
            schema.get("restrictedIntegerMaxExclusive").baseType.getTypeDescriptor(),
            schema.get("integerMaxExclusive")
        );
        assertTrue(
            schema.get("restrictedIntegerMaxExclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MAXEXCLUSIVE)
        );
        assertEquals(
            new IntegerItem(0),
            ((AtomicFacets) schema.get("restrictedIntegerMaxExclusive").getFacets()).maxExclusive.getItem()
        );

        assertTrue(schema.get("combinedBoundariesFacets").isIntegerType());
        assertEquals(
            schema.get("combinedBoundariesFacets").baseType.getTypeDescriptor(),
            schema.get("integerMaxInclusive")
        );
        assertTrue(
            schema.get("combinedBoundariesFacets").getFacets().getDefinedFacets().contains(FacetTypes.MININCLUSIVE)
        );
        assertTrue(
            schema.get("combinedBoundariesFacets").getFacets().getDefinedFacets().contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            new IntegerItem(1),
            ((AtomicFacets) schema.get("combinedBoundariesFacets").getFacets()).maxInclusive.getItem()
        );
        assertEquals(
            new IntegerItem(-1),
            ((AtomicFacets) schema.get("combinedBoundariesFacets").getFacets()).minInclusive.getItem()
        );
    }



    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
