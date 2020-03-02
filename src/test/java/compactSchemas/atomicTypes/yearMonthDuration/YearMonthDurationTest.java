package compactSchemas.atomicTypes.yearMonthDuration;

import base.BaseTest;
import jsound.typedescriptors.object.FieldDescriptor;
import jsound.tyson.TYSONArray;
import jsound.tyson.TYSONItem;
import jsound.tyson.TYSONObject;
import jsound.tyson.TYSONValue;
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

public class YearMonthDurationTest extends BaseTest {
    private static final String filePath = "atomicTypes/yearMonthDuration/yearMonthDurationFile.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> yearMonthDurationObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/yearMonthDuration/yearMonthDurationSchema.json";
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
        yearMonthDurationObj = schema.get("yearMonthDurationObj").getFacets().getObjectContent();
    }

    @Test
    public void testGeneral() {
        assertTrue(schema.get("yearMonthDurationType").isYearMonthDurationType());
        assertTrue(schema.get("yearMonthDurationObj").isObjectType());
        assertTrue(schema.get("arrayOfYearMonthDurations").isArrayType());
    }

    @Test
    public void testYearMonthDurationObj() {
        assertTrue(
            yearMonthDurationObj.get("myYearMonthDuration")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isYearMonthDurationType()
        );
        assertTrue(
            yearMonthDurationObj.get("requiredYearMonthDuration")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isYearMonthDurationType()
        );
        assertTrue(yearMonthDurationObj.get("requiredYearMonthDuration").isRequired());
        assertTrue(
            yearMonthDurationObj.get("nullableYearMonthDuration").getTypeOrReference().getTypeDescriptor().isUnionType()
        );
        assertTrue(
            yearMonthDurationObj.get("nullableYearMonthDuration")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getType()
                .isYearMonthDurationType()
        );
        assertTrue(
            yearMonthDurationObj.get("nullableYearMonthDuration")
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
            yearMonthDurationObj.get("yearMonthDurationWithDefault")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isYearMonthDurationType()
        );
        assertTrue(
            yearMonthDurationObj.get("yearMonthDurationWithDefault").getDefaultValue().isYearMonthDurationItem()
        );
        assertEquals(
            "P2Y4M",
            yearMonthDurationObj.get("yearMonthDurationWithDefault").getDefaultValue().getStringValue()
        );
        assertTrue(
            yearMonthDurationObj.get("requiredYearMonthDurationWithDefault")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isYearMonthDurationType()
        );
        assertTrue(yearMonthDurationObj.get("requiredYearMonthDurationWithDefault").isRequired());
        assertTrue(
            yearMonthDurationObj.get("requiredYearMonthDurationWithDefault").getDefaultValue().isYearMonthDurationItem()
        );
        assertEquals(
            "P10Y3M",
            yearMonthDurationObj.get("requiredYearMonthDurationWithDefault").getDefaultValue().getStringValue()
        );
        assertTrue(yearMonthDurationObj.get("uniqueYearMonthDuration").isUnique());
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }

    @Test
    public void testAnnotate() {
        TYSONObject tysonObject = (TYSONObject) schemaItem.annotate(fileItem);
        assertTrue(tysonObject.containsKey("yearMonthDurations"));
        TYSONArray tysonArray = (TYSONArray) tysonObject.get("yearMonthDurations");
        for (TYSONItem item : tysonArray) {
            TYSONObject object = (TYSONObject) item;
            assertEquals("yearMonthDurationObj", object.getTypeName());

            assertTrue(object.containsKey("requiredYearMonthDuration"));
            assertEquals("yearMonthDuration", object.get("requiredYearMonthDuration").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredYearMonthDuration")).getItemValue().isYearMonthDurationItem());

            assertTrue(object.containsKey("yearMonthDurationWithDefault"));
            assertEquals("yearMonthDuration", object.get("yearMonthDurationWithDefault").getTypeName());
            assertTrue(
                ((TYSONValue) object.get("yearMonthDurationWithDefault")).getItemValue().isYearMonthDurationItem()
            );

            assertTrue(object.containsKey("requiredYearMonthDurationWithDefault"));
            assertEquals(
                "yearMonthDuration",
                object.get("requiredYearMonthDurationWithDefault").getTypeName()
            );
            assertTrue(
                ((TYSONValue) object.get("requiredYearMonthDurationWithDefault")).getItemValue()
                    .isYearMonthDurationItem()
            );
        }

        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(1)).get("nullableYearMonthDuration"))).getItemValue()
                .isNullItem()
        );
        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(2)).get("nullableYearMonthDuration"))).getItemValue()
                .isYearMonthDurationItem()
        );
        assertEquals(
            "-P5Y",
            ((TYSONValue) (((TYSONObject) tysonArray.get(3)).get("yearMonthDurationWithDefault"))).getItemValue()
                .getStringValue()
        );

        assertEquals(
            "-P1M",
            ((TYSONValue) (((TYSONObject) tysonArray.get(4)).get("requiredYearMonthDurationWithDefault")))
                .getItemValue()
                .getStringValue()
        );
        assertEquals(
            "yearMonthDurationType",
            ((TYSONObject) tysonArray.get(5)).get("anotherYearMonthDuration").getTypeName()
        );
        assertNotEquals(
            ((TYSONValue) (((TYSONObject) tysonArray.get(6)).get("uniqueYearMonthDuration"))).getItemValue(),
            ((TYSONValue) (((TYSONObject) tysonArray.get(7)).get("uniqueYearMonthDuration"))).getItemValue()
        );

    }
}
