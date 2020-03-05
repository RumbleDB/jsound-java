package extendedSchemas.atomicTypes.time.facets;

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
    String filePath = "atomicTypes/time/facets/timeTimezoneFile.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/time/facets/timeTimezoneSchema.json",
                "targetType",
                false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("timeTimezoneRequired").isTimeType());
        assertTrue(
            schema.get("timeTimezoneRequired").getFacets().getDefinedFacets().contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.REQUIRED,
            ((AtomicFacets) schema.get("timeTimezoneRequired").getFacets()).explicitTimezone
        );

        assertTrue(schema.get("timeTimezoneOptional").isTimeType());
        assertTrue(
            schema.get("timeTimezoneOptional").getFacets().getDefinedFacets().contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.OPTIONAL,
            ((AtomicFacets) schema.get("timeTimezoneOptional").getFacets()).explicitTimezone
        );

        assertTrue(schema.get("timeTimezoneProhibited").isTimeType());
        assertTrue(
            schema.get("timeTimezoneProhibited")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.PROHIBITED,
            ((AtomicFacets) schema.get("timeTimezoneProhibited").getFacets()).explicitTimezone
        );

        assertTrue(schema.get("restrictedTimeTimezoneRequired").isTimeType());
        assertEquals(
            schema.get("restrictedTimeTimezoneRequired").baseType.getTypeDescriptor(),
            schema.get("timeTimezoneRequired")
        );
        assertTrue(
            schema.get("restrictedTimeTimezoneRequired")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.REQUIRED,
            ((AtomicFacets) schema.get("restrictedTimeTimezoneRequired").getFacets()).explicitTimezone
        );


        assertTrue(schema.get("restrictedTimeTimezoneOptionalFromOptional").isTimeType());
        assertEquals(
            schema.get("restrictedTimeTimezoneOptionalFromOptional").baseType.getTypeDescriptor(),
            schema.get("timeTimezoneOptional")
        );
        assertTrue(
            schema.get("restrictedTimeTimezoneOptionalFromOptional")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.OPTIONAL,
            ((AtomicFacets) schema.get("restrictedTimeTimezoneOptionalFromOptional").getFacets()).explicitTimezone
        );


        assertTrue(schema.get("restrictedTimeTimezoneRequiredFromOptional").isTimeType());
        assertEquals(
            schema.get("restrictedTimeTimezoneRequiredFromOptional").baseType.getTypeDescriptor(),
            schema.get("timeTimezoneOptional")
        );
        assertTrue(
            schema.get("restrictedTimeTimezoneRequiredFromOptional")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.REQUIRED,
            ((AtomicFacets) schema.get("restrictedTimeTimezoneRequiredFromOptional").getFacets()).explicitTimezone
        );


        assertTrue(schema.get("restrictedTimeTimezoneProhibitedFromOptional").isTimeType());
        assertEquals(
            schema.get("restrictedTimeTimezoneProhibitedFromOptional").baseType.getTypeDescriptor(),
            schema.get("timeTimezoneOptional")
        );
        assertTrue(
            schema.get("restrictedTimeTimezoneProhibitedFromOptional")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.PROHIBITED,
            ((AtomicFacets) schema.get("restrictedTimeTimezoneProhibitedFromOptional").getFacets()).explicitTimezone
        );


        assertTrue(schema.get("restrictedTimeTimezoneProhibited").isTimeType());
        assertEquals(
            schema.get("restrictedTimeTimezoneProhibited").baseType.getTypeDescriptor(),
            schema.get("timeTimezoneProhibited")
        );
        assertTrue(
            schema.get("restrictedTimeTimezoneProhibited")
                .getFacets()
                .getDefinedFacets()
                .contains(FacetTypes.EXPLICITTIMEZONE)
        );
        assertEquals(
            TimezoneFacet.PROHIBITED,
            ((AtomicFacets) schema.get("restrictedTimeTimezoneProhibited").getFacets()).explicitTimezone
        );
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
