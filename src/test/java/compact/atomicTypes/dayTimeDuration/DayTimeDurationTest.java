package compact.atomicTypes.dayTimeDuration;

import jsound.typedescriptors.object.FieldDescriptor;
import jsound.tyson.TYSONArray;
import jsound.tyson.TYSONObject;
import jsound.tyson.TYSONValue;
import jsound.tyson.TysonItem;
import org.junit.BeforeClass;
import org.junit.Test;
import base.BaseTest;

import java.io.IOException;
import java.util.Map;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class DayTimeDurationTest extends BaseTest {
    public static final String filePath = "atomicTypes/dayTimeDuration/dayTimeDurationFile.json";
    protected static String schemaPath = "atomicTypes/dayTimeDurationSchema.json";
    protected static boolean compact = true;
    public static Map<String, FieldDescriptor> dayTimeDurationObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
                (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
                filePath,
                compact
        );
        dayTimeDurationObj = schema.get("dayTimeDurationObj").getFacets().getObjectContent();
    }

    @Test
    public void testGeneral() {
        assertTrue(schema.get("dayTimeDurationType").isDayTimeDurationType());
        assertTrue(schema.get("dayTimeDurationObj").isObjectType());
        assertTrue(schema.get("arrayOfDayTimeDurations").isArrayType());
    }

    @Test
    public void testDayTimeDurationObj() {
        assertTrue(
            dayTimeDurationObj.get("myDayTimeDuration").getTypeOrReference().getTypeDescriptor().isDayTimeDurationType()
        );
        assertTrue(
            dayTimeDurationObj.get("requiredDayTimeDuration")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isDayTimeDurationType()
        );
        assertTrue(dayTimeDurationObj.get("requiredDayTimeDuration").isRequired());
        assertTrue(
            dayTimeDurationObj.get("nullableDayTimeDuration").getTypeOrReference().getTypeDescriptor().isUnionType()
        );
        assertTrue(
            dayTimeDurationObj.get("nullableDayTimeDuration")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getType()
                .isDayTimeDurationType()
        );
        assertTrue(
            dayTimeDurationObj.get("nullableDayTimeDuration")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getType()
                .isNullType()
        );
        assertTrue(
            dayTimeDurationObj.get("dayTimeDurationWithDefault")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isDayTimeDurationType()
        );
        assertTrue(dayTimeDurationObj.get("dayTimeDurationWithDefault").getDefaultValue().isDayTimeDurationItem());
        assertEquals(
            "P399DT2M",
            dayTimeDurationObj.get("dayTimeDurationWithDefault").getDefaultValue().getStringValue()
        );
        assertTrue(
            dayTimeDurationObj.get("requiredDayTimeDurationWithDefault")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isDayTimeDurationType()
        );
        assertTrue(dayTimeDurationObj.get("requiredDayTimeDurationWithDefault").isRequired());
        assertTrue(
            dayTimeDurationObj.get("requiredDayTimeDurationWithDefault").getDefaultValue().isDayTimeDurationItem()
        );
        assertEquals(
            "PT5M30.111S",
            dayTimeDurationObj.get("requiredDayTimeDurationWithDefault").getDefaultValue().getStringValue()
        );
        assertTrue(dayTimeDurationObj.get("uniqueDayTimeDuration").isUnique());
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }

    @Test
    public void testAnnotate() {
        TYSONObject tysonObject = (TYSONObject) schemaItem.annotate(fileItem);
        assertTrue(tysonObject.containsKey("dayTimeDurations"));
        TYSONArray tysonArray = (TYSONArray) tysonObject.get("dayTimeDurations");
        for (TysonItem item : tysonArray) {
            TYSONObject object = (TYSONObject) item;
            assertEquals("dayTimeDurationObj", object.getTypeName());

            assertTrue(object.containsKey("requiredDayTimeDuration"));
            assertEquals("dayTimeDuration", object.get("requiredDayTimeDuration").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredDayTimeDuration")).getItemValue().isDayTimeDurationItem());

            assertTrue(object.containsKey("dayTimeDurationWithDefault"));
            assertEquals("dayTimeDuration", object.get("dayTimeDurationWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("dayTimeDurationWithDefault")).getItemValue().isDayTimeDurationItem());

            assertTrue(object.containsKey("requiredDayTimeDurationWithDefault"));
            assertEquals(
                "dayTimeDuration",
                object.get("requiredDayTimeDurationWithDefault").getTypeName()
            );
            assertTrue(
                ((TYSONValue) object.get("requiredDayTimeDurationWithDefault")).getItemValue().isDayTimeDurationItem()
            );
        }

        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(1)).get("nullableDayTimeDuration"))).getItemValue()
                .isNullItem()
        );
        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(2)).get("nullableDayTimeDuration"))).getItemValue()
                .isDayTimeDurationItem()
        );
        assertEquals(
            "-P60D",
            ((TYSONValue) (((TYSONObject) tysonArray.get(3)).get("dayTimeDurationWithDefault"))).getItemValue()
                .getStringValue()
        );

        assertEquals(
            "PT7M7.890S",
            ((TYSONValue) (((TYSONObject) tysonArray.get(4)).get("requiredDayTimeDurationWithDefault"))).getItemValue()
                .getStringValue()
        );
        assertEquals(
            "dayTimeDurationType",
            ((TYSONObject) tysonArray.get(5)).get("anotherDayTimeDuration").getTypeName()
        );
        assertNotEquals(
            ((TYSONValue) (((TYSONObject) tysonArray.get(6)).get("uniqueDayTimeDuration"))).getItemValue(),
            ((TYSONValue) (((TYSONObject) tysonArray.get(7)).get("uniqueDayTimeDuration"))).getItemValue()
        );

    }
}
