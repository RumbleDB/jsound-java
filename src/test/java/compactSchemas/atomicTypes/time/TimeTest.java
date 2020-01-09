package compactSchemas.atomicTypes.time;

import base.BaseTest;
import jsound.typedescriptors.object.FieldDescriptor;
import jsound.tyson.TYSONArray;
import jsound.tyson.TYSONObject;
import jsound.tyson.TYSONValue;
import jsound.tyson.TysonItem;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TimeTest extends BaseTest {
    public static final String filePath = "atomicTypes/time/timeFile.json";
    protected static String schemaPath = "atomicTypes/timeSchema.json";
    protected static boolean compact = true;
    public static Map<String, FieldDescriptor> timeObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
        timeObj = schema.get("timeObj").getFacets().getObjectContent();
    }

    @Test
    public void testGeneral() {
        assertTrue(schema.get("timeType").isTimeType());
        assertTrue(schema.get("timeObj").isObjectType());
        assertTrue(schema.get("arrayOfTimes").isArrayType());
    }

    @Test
    public void testTimeObj() {
        assertTrue(timeObj.get("myTime").getTypeOrReference().getTypeDescriptor().isTimeType());
        assertTrue(timeObj.get("requiredTime").getTypeOrReference().getTypeDescriptor().isTimeType());
        assertTrue(timeObj.get("requiredTime").isRequired());
        assertTrue(timeObj.get("nullableTime").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(
            timeObj.get("nullableTime")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getType()
                .isTimeType()
        );
        assertTrue(
            timeObj.get("nullableTime")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getType()
                .isNullType()
        );
        assertTrue(timeObj.get("timeWithDefault").getTypeOrReference().getTypeDescriptor().isTimeType());
        assertTrue(timeObj.get("timeWithDefault").getDefaultValue().isTimeItem());
        assertEquals("13:20:00", timeObj.get("timeWithDefault").getDefaultValue().getStringValue());
        assertTrue(
            timeObj.get("requiredTimeWithDefault").getTypeOrReference().getTypeDescriptor().isTimeType()
        );
        assertTrue(timeObj.get("requiredTimeWithDefault").isRequired());
        assertTrue(timeObj.get("requiredTimeWithDefault").getDefaultValue().isTimeItem());
        assertEquals("23:10:10.555Z", timeObj.get("requiredTimeWithDefault").getDefaultValue().getStringValue());
        assertTrue(timeObj.get("uniqueTime").isUnique());
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }

    @Test
    public void testAnnotate() {
        TYSONObject tysonObject = (TYSONObject) schemaItem.annotate(fileItem);
        assertTrue(tysonObject.containsKey("times"));
        TYSONArray tysonArray = (TYSONArray) tysonObject.get("times");
        for (TysonItem item : tysonArray) {
            TYSONObject object = (TYSONObject) item;
            assertEquals("timeObj", object.getTypeName());

            assertTrue(object.containsKey("requiredTime"));
            assertEquals("time", object.get("requiredTime").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredTime")).getItemValue().isTimeItem());

            assertTrue(object.containsKey("timeWithDefault"));
            assertEquals("time", object.get("timeWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("timeWithDefault")).getItemValue().isTimeItem());

            assertTrue(object.containsKey("requiredTimeWithDefault"));
            assertEquals("time", object.get("requiredTimeWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredTimeWithDefault")).getItemValue().isTimeItem());
        }

        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(1)).get("nullableTime"))).getItemValue().isNullItem()
        );
        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(2)).get("nullableTime"))).getItemValue().isTimeItem()
        );
        assertEquals(
            "20:20:28",
            ((TYSONValue) (((TYSONObject) tysonArray.get(3)).get("timeWithDefault"))).getItemValue()
                .getStringValue()
        );

        assertEquals(
            "20:01:12.160",
            ((TYSONValue) (((TYSONObject) tysonArray.get(4)).get("requiredTimeWithDefault"))).getItemValue()
                .getStringValue()
        );
        assertEquals(
            "timeType",
            ((TYSONObject) tysonArray.get(5)).get("anotherTime").getTypeName()
        );
        assertNotEquals(
            ((TYSONValue) (((TYSONObject) tysonArray.get(6)).get("uniqueTime"))).getItemValue(),
            ((TYSONValue) (((TYSONObject) tysonArray.get(7)).get("uniqueTime"))).getItemValue()
        );

    }
}
