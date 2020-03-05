package extendedSchemas.atomicTypes.date.facets;

import base.BaseTest;
import jsound.atomicItems.DateItem;
import jsound.atomicItems.DateTimeItem;
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
    String filePath = "atomicTypes/date/facets/dateBoundariesFile.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/date/facets/dateBoundariesSchema.json",
                "targetType",
                false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("dateMinInclusive").isDateType());
        assertTrue(schema.get("dateMinInclusive").getFacets().getDefinedFacets().contains(FacetTypes.MININCLUSIVE));
        assertEquals(
            createDate("2004-04-12"),
            ((AtomicFacets) schema.get("dateMinInclusive").getFacets()).minInclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("dateMinExclusive").isDateType());
        assertTrue(schema.get("dateMinExclusive").getFacets().getDefinedFacets().contains(FacetTypes.MINEXCLUSIVE));
        assertEquals(
            createDate("2004-04-12"),
            ((AtomicFacets) schema.get("dateMinExclusive").getFacets()).minExclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("dateMaxInclusive").isDateType());
        assertTrue(schema.get("dateMaxInclusive").getFacets().getDefinedFacets().contains(FacetTypes.MAXINCLUSIVE));
        assertEquals(
            createDate("2004-04-12"),
            ((AtomicFacets) schema.get("dateMaxInclusive").getFacets()).maxInclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("dateMaxExclusive").isDateType());
        assertTrue(schema.get("dateMaxExclusive").getFacets().getDefinedFacets().contains(FacetTypes.MAXEXCLUSIVE));
        assertEquals(
            createDate("2004-04-12"),
            ((AtomicFacets) schema.get("dateMaxExclusive").getFacets()).maxExclusive.getItem().getStringValue()
        );

        assertTrue(schema.get("restrictedDateMinInclusive").isDateType());
        assertEquals(
            schema.get("restrictedDateMinInclusive").baseType.getTypeDescriptor(),
            schema.get("dateMinInclusive")
        );
        assertTrue(
            schema.get("restrictedDateMinInclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MININCLUSIVE)
        );
        assertEquals(
            createDate("2004-04-14"),
            ((AtomicFacets) schema.get("restrictedDateMinInclusive").getFacets()).minInclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedDateMinExclusive").isDateType());
        assertEquals(
            schema.get("restrictedDateMinExclusive").baseType.getTypeDescriptor(),
            schema.get("dateMinExclusive")
        );
        assertTrue(
            schema.get("restrictedDateMinExclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MINEXCLUSIVE)
        );
        assertEquals(
            createDate("2004-04-14"),
            ((AtomicFacets) schema.get("restrictedDateMinExclusive").getFacets()).minExclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedDateMaxInclusive").isDateType());
        assertEquals(
            schema.get("restrictedDateMaxInclusive").baseType.getTypeDescriptor(),
            schema.get("dateMaxInclusive")
        );
        assertTrue(
            schema.get("restrictedDateMaxInclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            createDate("2004-04-12"),
            ((AtomicFacets) schema.get("restrictedDateMaxInclusive").getFacets()).maxInclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("restrictedDateMaxExclusive").isDateType());
        assertEquals(
            schema.get("restrictedDateMaxExclusive").baseType.getTypeDescriptor(),
            schema.get("dateMaxExclusive")
        );
        assertTrue(
            schema.get("restrictedDateMaxExclusive")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.MAXEXCLUSIVE)
        );
        assertEquals(
            createDate("2004-04-11"),
            ((AtomicFacets) schema.get("restrictedDateMaxExclusive").getFacets()).maxExclusive.getItem()
                .getStringValue()
        );

        assertTrue(schema.get("combinedBoundariesFacets").isDateType());
        assertEquals(
            schema.get("combinedBoundariesFacets").baseType.getTypeDescriptor(),
            schema.get("dateMaxInclusive")
        );
        assertTrue(
            schema.get("combinedBoundariesFacets").getFacets().getDefinedFacets().contains(FacetTypes.MININCLUSIVE)
        );
        assertTrue(
            schema.get("combinedBoundariesFacets").getFacets().getDefinedFacets().contains(FacetTypes.MAXINCLUSIVE)
        );
        assertEquals(
            createDate("2004-04-12"),
            ((AtomicFacets) schema.get("combinedBoundariesFacets").getFacets()).maxInclusive.getItem().getStringValue()
        );
        assertEquals(
            createDate("2004-02-12"),
            ((AtomicFacets) schema.get("combinedBoundariesFacets").getFacets()).minInclusive.getItem().getStringValue()
        );
    }


    private String createDate(String value) {
        DateTime date = DateTimeItem.parseDateTime(value, AtomicTypes.DATE);
        if (!value.endsWith("Z") && date.getZone() == DateTimeZone.getDefault()) {
            return new DateItem(date.withZoneRetainFields(DateTimeZone.UTC), false).getStringValue();
        }
        return new DateItem(date, true).getStringValue();
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
