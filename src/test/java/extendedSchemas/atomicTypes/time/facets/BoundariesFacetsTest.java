package extendedSchemas.atomicTypes.time.facets;

import base.BaseTest;
import jsound.atomicItems.DateTimeItem;
import jsound.atomicItems.TimeItem;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import jsound.types.AtomicTypes;
import org.api.executors.JSoundExecutor;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoundariesFacetsTest extends BaseTest {
    String filePath = "atomicTypes/time/facets/timeBoundariesFile.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/time/facets/timeBoundariesSchema.json",
                "targetType",
                false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("timeMinInclusive").isTimeType());
        assertTrue(schema.get("timeMinInclusive").getFacets().getDefinedFacets().contains(FacetTypes.MININCLUSIVE));
        assertEquals(
            createTime("13:20:00"),
            ((AtomicFacets) schema.get("timeMinInclusive").getFacets()).minInclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("timeMinExclusive").isTimeType());
        assertTrue(schema.get("timeMinExclusive").getFacets().getDefinedFacets().contains(FacetTypes.MINEXCLUSIVE));
        assertEquals(
            createTime("13:20:00"),
            ((AtomicFacets) schema.get("timeMinExclusive").getFacets()).minExclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("timeMaxInclusive").isTimeType());
        assertTrue(schema.get("timeMaxInclusive").getFacets().getDefinedFacets().contains(FacetTypes.MAXINCLUSIVE));
        assertEquals(
            createTime("13:20:00"),
            ((AtomicFacets) schema.get("timeMaxInclusive").getFacets()).maxInclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("timeMaxExclusive").isTimeType());
        assertTrue(schema.get("timeMaxExclusive").getFacets().getDefinedFacets().contains(FacetTypes.MAXEXCLUSIVE));
        assertEquals(
            createTime("13:20:00"),
            ((AtomicFacets) schema.get("timeMaxExclusive").getFacets()).maxExclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("restrictedTimeMinInclusive").isTimeType());
        assertEquals(
            schema.get("restrictedTimeMinInclusive").baseType.getTypeDescriptor(),
            schema.get("timeMinInclusive")
        );
        assertTrue(
            schema.get("restrictedTimeMinInclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MININCLUSIVE)
        );
        assertEquals(
            createTime("14:20:00"),
            ((AtomicFacets) schema.get("restrictedTimeMinInclusive").getFacets()).minInclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedTimeMinExclusive").isTimeType());
        assertEquals(
            schema.get("restrictedTimeMinExclusive").baseType.getTypeDescriptor(),
            schema.get("timeMinExclusive")
        );
        assertTrue(
            schema.get("restrictedTimeMinExclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MINEXCLUSIVE)
        );
        assertEquals(
            createTime("13:20:00"),
            ((AtomicFacets) schema.get("restrictedTimeMinExclusive").getFacets()).minExclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedTimeMaxInclusive").isTimeType());
        assertEquals(
            schema.get("restrictedTimeMaxInclusive").baseType.getTypeDescriptor(),
            schema.get("timeMaxInclusive")
        );
        assertTrue(
            schema.get("restrictedTimeMaxInclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            createTime("12:20:00"),
            ((AtomicFacets) schema.get("restrictedTimeMaxInclusive").getFacets()).maxInclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedTimeMaxExclusive").isTimeType());
        assertEquals(
            schema.get("restrictedTimeMaxExclusive").baseType.getTypeDescriptor(),
            schema.get("timeMaxExclusive")
        );
        assertTrue(
            schema.get("restrictedTimeMaxExclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MAXEXCLUSIVE)
        );
        assertEquals(
            createTime("11:20:00"),
            ((AtomicFacets) schema.get("restrictedTimeMaxExclusive").getFacets()).maxExclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("combinedBoundariesFacets").isTimeType());
        assertEquals(
            schema.get("combinedBoundariesFacets").baseType.getTypeDescriptor(),
            schema.get("timeMaxInclusive")
        );
        assertTrue(
            schema.get("combinedBoundariesFacets").getFacets().getDefinedFacets().contains(FacetTypes.MININCLUSIVE)
        );
        assertTrue(
            schema.get("combinedBoundariesFacets").getFacets().getDefinedFacets().contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            createTime("13:20:00"),
            ((AtomicFacets) schema.get("combinedBoundariesFacets").getFacets()).maxInclusive.getItem().getStringValue()
        );
        assertEquals(
            createTime("11:20:00"),
            ((AtomicFacets) schema.get("combinedBoundariesFacets").getFacets()).minInclusive.getItem().getStringValue()
        );
    }


    private String createTime(String value) {
        DateTime date = DateTimeItem.parseDateTime(value, AtomicTypes.TIME);
        if (!value.endsWith("Z") && date.getZone() == DateTimeZone.getDefault()) {
            return new TimeItem(date.withZoneRetainFields(DateTimeZone.UTC), false).getStringValue();
        }
        return new TimeItem(date, true).getStringValue();
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
