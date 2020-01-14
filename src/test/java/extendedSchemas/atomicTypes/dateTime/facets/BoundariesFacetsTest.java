package extendedSchemas.atomicTypes.dateTime.facets;

import base.BaseTest;
import jsound.atomicItems.DateTimeItem;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import jsound.types.AtomicTypes;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
            "extendedSchemas/atomicTypes/dateTime/facets/dateTimeBoundariesSchema.json",
            "atomicTypes/dateTime/facets/dateTimeBoundariesFile.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("dateTimeMinInclusive").isDateTimeType());
        assertTrue(schema.get("dateTimeMinInclusive").getFacets().getDefinedFacets().contains(FacetTypes.MININCLUSIVE));
        assertEquals(
            createDateTime("2004-04-12T13:20:00"),
            ((AtomicFacets) schema.get("dateTimeMinInclusive").getFacets()).minInclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("dateTimeMinExclusive").isDateTimeType());
        assertTrue(schema.get("dateTimeMinExclusive").getFacets().getDefinedFacets().contains(FacetTypes.MINEXCLUSIVE));
        assertEquals(
            createDateTime("2004-04-12T13:20:00"),
            ((AtomicFacets) schema.get("dateTimeMinExclusive").getFacets()).minExclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("dateTimeMaxInclusive").isDateTimeType());
        assertTrue(schema.get("dateTimeMaxInclusive").getFacets().getDefinedFacets().contains(FacetTypes.MAXINCLUSIVE));
        assertEquals(
            createDateTime("2004-04-12T13:20:00"),
            ((AtomicFacets) schema.get("dateTimeMaxInclusive").getFacets()).maxInclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("dateTimeMaxExclusive").isDateTimeType());
        assertTrue(schema.get("dateTimeMaxExclusive").getFacets().getDefinedFacets().contains(FacetTypes.MAXEXCLUSIVE));
        assertEquals(
            createDateTime("2004-04-12T13:20:00"),
            ((AtomicFacets) schema.get("dateTimeMaxExclusive").getFacets()).maxExclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("restrictedDateTimeMinInclusive").isDateTimeType());
        assertEquals(
            schema.get("restrictedDateTimeMinInclusive").baseType.getTypeDescriptor(),
            schema.get("dateTimeMinInclusive")
        );
        assertTrue(
            schema.get("restrictedDateTimeMinInclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MININCLUSIVE)
        );
        assertEquals(
            createDateTime("2004-04-12T14:20:00"),
            ((AtomicFacets) schema.get("restrictedDateTimeMinInclusive").getFacets()).minInclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedDateTimeMinExclusive").isDateTimeType());
        assertEquals(
            schema.get("restrictedDateTimeMinExclusive").baseType.getTypeDescriptor(),
            schema.get("dateTimeMinExclusive")
        );
        assertTrue(
            schema.get("restrictedDateTimeMinExclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MINEXCLUSIVE)
        );
        assertEquals(
            createDateTime("2004-04-13T13:20:00"),
            ((AtomicFacets) schema.get("restrictedDateTimeMinExclusive").getFacets()).minExclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedDateTimeMaxInclusive").isDateTimeType());
        assertEquals(
            schema.get("restrictedDateTimeMaxInclusive").baseType.getTypeDescriptor(),
            schema.get("dateTimeMaxInclusive")
        );
        assertTrue(
            schema.get("restrictedDateTimeMaxInclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            createDateTime("2004-04-12T12:20:00"),
            ((AtomicFacets) schema.get("restrictedDateTimeMaxInclusive").getFacets()).maxInclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedDateTimeMaxExclusive").isDateTimeType());
        assertEquals(
            schema.get("restrictedDateTimeMaxExclusive").baseType.getTypeDescriptor(),
            schema.get("dateTimeMaxExclusive")
        );
        assertTrue(
            schema.get("restrictedDateTimeMaxExclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MAXEXCLUSIVE)
        );
        assertEquals(
            createDateTime("2004-04-11T13:20:00"),
            ((AtomicFacets) schema.get("restrictedDateTimeMaxExclusive").getFacets()).maxExclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("combinedBoundariesFacets").isDateTimeType());
        assertEquals(
            schema.get("combinedBoundariesFacets").baseType.getTypeDescriptor(),
            schema.get("dateTimeMaxInclusive")
        );
        assertTrue(
            schema.get("combinedBoundariesFacets").getFacets().getDefinedFacets().contains(FacetTypes.MININCLUSIVE)
        );
        assertTrue(
            schema.get("combinedBoundariesFacets").getFacets().getDefinedFacets().contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            createDateTime("2004-04-12T13:20:00"),
            ((AtomicFacets) schema.get("combinedBoundariesFacets").getFacets()).maxInclusive.getItem().getStringValue()
        );
        assertEquals(
            createDateTime("2004-03-12T13:20:00"),
            ((AtomicFacets) schema.get("combinedBoundariesFacets").getFacets()).minInclusive.getItem().getStringValue()
        );
    }


    private String createDateTime(String value) {
        DateTime date = DateTimeItem.parseDateTime(value, AtomicTypes.DATETIME);
        if (!value.endsWith("Z") && date.getZone() == DateTimeZone.getDefault()) {
            return new DateTimeItem(date.withZoneRetainFields(DateTimeZone.UTC), false).getStringValue();
        }
        return new DateTimeItem(date, true).getStringValue();
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
