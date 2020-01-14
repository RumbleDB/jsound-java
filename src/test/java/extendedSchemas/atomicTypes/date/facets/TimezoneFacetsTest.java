package extendedSchemas.atomicTypes.date.facets;

import base.BaseTest;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import jsound.facets.TimezoneFacet;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TimezoneFacetsTest extends BaseTest {
    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/atomicTypes/date/facets/dateTimezoneSchema.json",
            "atomicTypes/date/facets/dateTimezoneFile.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("dateTimezoneRequired").isDateType());
        assertTrue(
            schema.get("dateTimezoneRequired").getFacets().getDefinedFacets().contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.REQUIRED,
            ((AtomicFacets) schema.get("dateTimezoneRequired").getFacets()).explicitTimezone
        );

        assertTrue(schema.get("dateTimezoneOptional").isDateType());
        assertTrue(
            schema.get("dateTimezoneOptional").getFacets().getDefinedFacets().contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.OPTIONAL,
            ((AtomicFacets) schema.get("dateTimezoneOptional").getFacets()).explicitTimezone
        );

        assertTrue(schema.get("dateTimezoneProhibited").isDateType());
        assertTrue(
            schema.get("dateTimezoneProhibited")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.PROHIBITED,
            ((AtomicFacets) schema.get("dateTimezoneProhibited").getFacets()).explicitTimezone
        );

        assertTrue(schema.get("restrictedDateTimezoneRequired").isDateType());
        assertEquals(
            schema.get("restrictedDateTimezoneRequired").baseType.getTypeDescriptor(),
            schema.get("dateTimezoneRequired")
        );
        assertTrue(
            schema.get("restrictedDateTimezoneRequired")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.REQUIRED,
            ((AtomicFacets) schema.get("restrictedDateTimezoneRequired").getFacets()).explicitTimezone
        );


        assertTrue(schema.get("restrictedDateTimezoneOptionalFromOptional").isDateType());
        assertEquals(
            schema.get("restrictedDateTimezoneOptionalFromOptional").baseType.getTypeDescriptor(),
            schema.get("dateTimezoneOptional")
        );
        assertTrue(
            schema.get("restrictedDateTimezoneOptionalFromOptional")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.OPTIONAL,
            ((AtomicFacets) schema.get("restrictedDateTimezoneOptionalFromOptional").getFacets()).explicitTimezone
        );


        assertTrue(schema.get("restrictedDateTimezoneRequiredFromOptional").isDateType());
        assertEquals(
            schema.get("restrictedDateTimezoneRequiredFromOptional").baseType.getTypeDescriptor(),
            schema.get("dateTimezoneOptional")
        );
        assertTrue(
            schema.get("restrictedDateTimezoneRequiredFromOptional")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.REQUIRED,
            ((AtomicFacets) schema.get("restrictedDateTimezoneRequiredFromOptional").getFacets()).explicitTimezone
        );


        assertTrue(schema.get("restrictedDateTimezoneProhibitedFromOptional").isDateType());
        assertEquals(
            schema.get("restrictedDateTimezoneProhibitedFromOptional").baseType.getTypeDescriptor(),
            schema.get("dateTimezoneOptional")
        );
        assertTrue(
            schema.get("restrictedDateTimezoneProhibitedFromOptional")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.PROHIBITED,
            ((AtomicFacets) schema.get("restrictedDateTimezoneProhibitedFromOptional").getFacets()).explicitTimezone
        );


        assertTrue(schema.get("restrictedDateTimezoneProhibited").isDateType());
        assertEquals(
            schema.get("restrictedDateTimezoneProhibited").baseType.getTypeDescriptor(),
            schema.get("dateTimezoneProhibited")
        );
        assertTrue(
            schema.get("restrictedDateTimezoneProhibited")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.PROHIBITED,
            ((AtomicFacets) schema.get("restrictedDateTimezoneProhibited").getFacets()).explicitTimezone
        );
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
