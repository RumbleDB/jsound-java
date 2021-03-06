package compactSchemas.atomicTypes.dateTime;

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

public class DateTimeTest extends BaseTest {
    private static final String filePath = "atomicTypes/dateTime/dateTimeFile.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> dateTimeObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/dateTime/dateTimeSchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        dateTimeObj = schema.get("dateTimeObj").getFacets().getObjectContent();
    }

    @Test
    public void testGeneral() {
        assertTrue(schema.get("dateTimeType").isDateTimeType());
        assertTrue(schema.get("dateTimeObj").isObjectType());
        assertTrue(schema.get("arrayOfDateTimes").isArrayType());
    }

    @Test
    public void testDateTimeObj() {
        assertTrue(dateTimeObj.get("myDateTime").getTypeOrReference().getTypeDescriptor().isDateTimeType());
        assertTrue(dateTimeObj.get("requiredDateTime").getTypeOrReference().getTypeDescriptor().isDateTimeType());
        assertTrue(dateTimeObj.get("requiredDateTime").isRequired());
        assertTrue(dateTimeObj.get("nullableDateTime").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(
            dateTimeObj.get("nullableDateTime")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getType()
                .isDateTimeType()
        );
        assertTrue(
            dateTimeObj.get("nullableDateTime")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getType()
                .isNullType()
        );
        assertTrue(dateTimeObj.get("dateTimeWithDefault").getTypeOrReference().getTypeDescriptor().isDateTimeType());
        assertTrue(dateTimeObj.get("dateTimeWithDefault").getDefaultValue().isDateTimeItem());
        assertEquals(
            "2001-12-12T12:00:00-12:00",
            dateTimeObj.get("dateTimeWithDefault").getDefaultValue().getStringValue()
        );
        assertTrue(
            dateTimeObj.get("requiredDateTimeWithDefault").getTypeOrReference().getTypeDescriptor().isDateTimeType()
        );
        assertTrue(dateTimeObj.get("requiredDateTimeWithDefault").isRequired());
        assertTrue(dateTimeObj.get("requiredDateTimeWithDefault").getDefaultValue().isDateTimeItem());
        assertEquals(
            "2004-04-12T13:20:00Z",
            dateTimeObj.get("requiredDateTimeWithDefault").getDefaultValue().getStringValue()
        );
        assertTrue(dateTimeObj.get("uniqueDateTime").isUnique());
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }

    @Test
    public void testAnnotate() throws IOException {
        TYSONObject tysonObject = (TYSONObject) jSoundSchema.annotateJSONFromPath(filePathPrefix + filePath);
        assertTrue(tysonObject.containsKey("dateTimes"));
        TYSONArray tysonArray = (TYSONArray) tysonObject.get("dateTimes");
        for (TYSONItem item : tysonArray) {
            TYSONObject object = (TYSONObject) item;
            assertEquals("dateTimeObj", object.getTypeName());

            assertTrue(object.containsKey("requiredDateTime"));
            assertEquals("dateTime", object.get("requiredDateTime").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredDateTime")).getItemValue().isDateTimeItem());

            assertTrue(object.containsKey("dateTimeWithDefault"));
            assertEquals("dateTime", object.get("dateTimeWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("dateTimeWithDefault")).getItemValue().isDateTimeItem());

            assertTrue(object.containsKey("requiredDateTimeWithDefault"));
            assertEquals("dateTime", object.get("requiredDateTimeWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredDateTimeWithDefault")).getItemValue().isDateTimeItem());
        }

        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(1)).get("nullableDateTime"))).getItemValue().isNullItem()
        );
        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(2)).get("nullableDateTime"))).getItemValue().isDateTimeItem()
        );
        assertEquals(
            "2001-12-12T23:00:00",
            ((TYSONValue) (((TYSONObject) tysonArray.get(3)).get("dateTimeWithDefault"))).getItemValue()
                .getStringValue()
        );

        assertEquals(
            "2001-12-13T00:00:00",
            ((TYSONValue) (((TYSONObject) tysonArray.get(4)).get("requiredDateTimeWithDefault"))).getItemValue()
                .getStringValue()
        );
        assertEquals(
            "dateTimeType",
            ((TYSONValue) (((TYSONObject) tysonArray.get(5)).get("anotherDateTime"))).getTypeName()
        );
        assertNotEquals(
            ((TYSONValue) (((TYSONObject) tysonArray.get(6)).get("uniqueDateTime"))).getItemValue(),
            ((TYSONValue) (((TYSONObject) tysonArray.get(7)).get("uniqueDateTime"))).getItemValue()
        );

    }
}
