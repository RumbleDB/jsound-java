package compactSchemas.atomicTypes.duration;

import base.BaseTest;
import jsound.typedescriptors.object.FieldDescriptor;
import jsound.tyson.TYSONArray;
import jsound.tyson.TYSONItem;
import jsound.tyson.TYSONObject;
import jsound.tyson.TYSONValue;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class DurationTest extends BaseTest {
    private static final String filePath = "atomicTypes/duration/durationFile.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> durationObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/duration/durationSchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        durationObj = schema.get("durationObj").getFacets().getObjectContent();
    }

    @Test
    public void testGeneral() {
        assertTrue(schema.get("durationType").isDurationType());
        assertTrue(schema.get("durationObj").isObjectType());
        assertTrue(schema.get("arrayOfDurations").isArrayType());
    }

    @Test
    public void testDurationObj() {
        assertTrue(durationObj.get("myDuration").getTypeOrReference().getTypeDescriptor().isDurationType());
        assertTrue(durationObj.get("requiredDuration").getTypeOrReference().getTypeDescriptor().isDurationType());
        assertTrue(durationObj.get("requiredDuration").isRequired());
        assertTrue(durationObj.get("nullableDuration").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(
            durationObj.get("nullableDuration")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getType()
                .isDurationType()
        );
        assertTrue(
            durationObj.get("nullableDuration")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getType()
                .isNullType()
        );
        assertTrue(durationObj.get("durationWithDefault").getTypeOrReference().getTypeDescriptor().isDurationType());
        assertTrue(durationObj.get("durationWithDefault").getDefaultValue().isDurationItem());
        assertEquals("P9Y2M", durationObj.get("durationWithDefault").getDefaultValue().getStringValue());
        assertTrue(
            durationObj.get("requiredDurationWithDefault").getTypeOrReference().getTypeDescriptor().isDurationType()
        );
        assertTrue(durationObj.get("requiredDurationWithDefault").isRequired());
        assertTrue(durationObj.get("requiredDurationWithDefault").getDefaultValue().isDurationItem());
        assertEquals("P1Y7MT5M30S", durationObj.get("requiredDurationWithDefault").getDefaultValue().getStringValue());
        assertTrue(durationObj.get("uniqueDuration").isUnique());
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }

    @Test
    public void testAnnotate() throws IOException {
        TYSONObject tysonObject = (TYSONObject) jSoundSchema.annotateJSONFromPath(filePathPrefix + filePath);
        assertTrue(tysonObject.containsKey("durations"));
        TYSONArray tysonArray = (TYSONArray) tysonObject.get("durations");
        for (TYSONItem item : tysonArray) {
            TYSONObject object = (TYSONObject) item;
            assertEquals("durationObj", object.getTypeName());

            assertTrue(object.containsKey("requiredDuration"));
            assertEquals("duration", object.get("requiredDuration").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredDuration")).getItemValue().isDurationItem());

            assertTrue(object.containsKey("durationWithDefault"));
            assertEquals("duration", object.get("durationWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("durationWithDefault")).getItemValue().isDurationItem());

            assertTrue(object.containsKey("requiredDurationWithDefault"));
            assertEquals("duration", object.get("requiredDurationWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredDurationWithDefault")).getItemValue().isDurationItem());
        }

        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(1)).get("nullableDuration"))).getItemValue().isNullItem()
        );
        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(2)).get("nullableDuration"))).getItemValue().isDurationItem()
        );
        assertEquals(
            "-P60D",
            ((TYSONValue) (((TYSONObject) tysonArray.get(3)).get("durationWithDefault"))).getItemValue()
                .getStringValue()
        );

        assertEquals(
            "PT7M7.890S",
            ((TYSONValue) (((TYSONObject) tysonArray.get(4)).get("requiredDurationWithDefault"))).getItemValue()
                .getStringValue()
        );
        assertEquals(
            "durationType",
            (((TYSONObject) tysonArray.get(5)).get("anotherDuration")).getTypeName()
        );
        assertNotEquals(
            ((TYSONValue) (((TYSONObject) tysonArray.get(6)).get("uniqueDuration"))).getItemValue(),
            ((TYSONValue) (((TYSONObject) tysonArray.get(7)).get("uniqueDuration"))).getItemValue()
        );

    }
}
