package extendedSchemas.atomicTypes.dateTime.facets;

import base.BaseTest;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import jsound.facets.TimezoneFacet;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TimezoneFacetsTest extends BaseTest {
    String filePath = "atomicTypes/dateTime/facets/dateTimeTimezoneFile.json";
    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/dateTime/facets/dateTimeTimezoneSchema.json",
                "targetType",
                false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("dateTimeTimezoneRequired").isDateTimeType());
        assertTrue(
            schema.get("dateTimeTimezoneRequired").getFacets().getDefinedFacets().contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.REQUIRED,
            ((AtomicFacets) schema.get("dateTimeTimezoneRequired").getFacets()).explicitTimezone
        );

        assertTrue(schema.get("dateTimeTimezoneOptional").isDateTimeType());
        assertTrue(
            schema.get("dateTimeTimezoneOptional").getFacets().getDefinedFacets().contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.OPTIONAL,
            ((AtomicFacets) schema.get("dateTimeTimezoneOptional").getFacets()).explicitTimezone
        );

        assertTrue(schema.get("dateTimeTimezoneProhibited").isDateTimeType());
        assertTrue(
            schema.get("dateTimeTimezoneProhibited")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.PROHIBITED,
            ((AtomicFacets) schema.get("dateTimeTimezoneProhibited").getFacets()).explicitTimezone
        );

        assertTrue(schema.get("restrictedDateTimeTimezoneRequired").isDateTimeType());
        assertEquals(
            schema.get("restrictedDateTimeTimezoneRequired").baseType.getTypeDescriptor(),
            schema.get("dateTimeTimezoneRequired")
        );
        assertTrue(
            schema.get("restrictedDateTimeTimezoneRequired")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.REQUIRED,
            ((AtomicFacets) schema.get("restrictedDateTimeTimezoneRequired").getFacets()).explicitTimezone
        );


        assertTrue(schema.get("restrictedDateTimeTimezoneOptionalFromOptional").isDateTimeType());
        assertEquals(
            schema.get("restrictedDateTimeTimezoneOptionalFromOptional").baseType.getTypeDescriptor(),
            schema.get("dateTimeTimezoneOptional")
        );
        assertTrue(
            schema.get("restrictedDateTimeTimezoneOptionalFromOptional")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.OPTIONAL,
            ((AtomicFacets) schema.get("restrictedDateTimeTimezoneOptionalFromOptional").getFacets()).explicitTimezone
        );


        assertTrue(schema.get("restrictedDateTimeTimezoneRequiredFromOptional").isDateTimeType());
        assertEquals(
            schema.get("restrictedDateTimeTimezoneRequiredFromOptional").baseType.getTypeDescriptor(),
            schema.get("dateTimeTimezoneOptional")
        );
        assertTrue(
            schema.get("restrictedDateTimeTimezoneRequiredFromOptional")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.REQUIRED,
            ((AtomicFacets) schema.get("restrictedDateTimeTimezoneRequiredFromOptional").getFacets()).explicitTimezone
        );


        assertTrue(schema.get("restrictedDateTimeTimezoneProhibitedFromOptional").isDateTimeType());
        assertEquals(
            schema.get("restrictedDateTimeTimezoneProhibitedFromOptional").baseType.getTypeDescriptor(),
            schema.get("dateTimeTimezoneOptional")
        );
        assertTrue(
            schema.get("restrictedDateTimeTimezoneProhibitedFromOptional")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.PROHIBITED,
            ((AtomicFacets) schema.get("restrictedDateTimeTimezoneProhibitedFromOptional").getFacets()).explicitTimezone
        );


        assertTrue(schema.get("restrictedDateTimeTimezoneProhibited").isDateTimeType());
        assertEquals(
            schema.get("restrictedDateTimeTimezoneProhibited").baseType.getTypeDescriptor(),
            schema.get("dateTimeTimezoneProhibited")
        );
        assertTrue(
            schema.get("restrictedDateTimeTimezoneProhibited")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.PROHIBITED,
            ((AtomicFacets) schema.get("restrictedDateTimeTimezoneProhibited").getFacets()).explicitTimezone
        );
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
