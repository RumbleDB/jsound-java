package compactSchemas.atomicTypes.date;

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

public class DateTest extends BaseTest {
    private static final String filePath = "atomicTypes/date/dateFile.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> dateObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/date/dateSchema.json";
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
        dateObj = schema.get("dateObj").getFacets().getObjectContent();
    }

    @Test
    public void testGeneral() {
        assertTrue(schema.get("dateType").isDateType());
        assertTrue(schema.get("dateObj").isObjectType());
        assertTrue(schema.get("arrayOfDates").isArrayType());
    }

    @Test
    public void testDateObj() {
        assertTrue(dateObj.get("myDate").getTypeOrReference().getTypeDescriptor().isDateType());
        assertTrue(dateObj.get("requiredDate").getTypeOrReference().getTypeDescriptor().isDateType());
        assertTrue(dateObj.get("requiredDate").isRequired());
        assertTrue(dateObj.get("nullableDate").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(
            dateObj.get("nullableDate")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getType()
                .isDateType()
        );
        assertTrue(
            dateObj.get("nullableDate")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getType()
                .isNullType()
        );
        assertTrue(dateObj.get("dateWithDefault").getTypeOrReference().getTypeDescriptor().isDateType());
        assertTrue(dateObj.get("dateWithDefault").getDefaultValue().isDateItem());
        assertEquals("2001-01-01-03:00", dateObj.get("dateWithDefault").getDefaultValue().getStringValue());
        assertTrue(
            dateObj.get("requiredDateWithDefault").getTypeOrReference().getTypeDescriptor().isDateType()
        );
        assertTrue(dateObj.get("requiredDateWithDefault").isRequired());
        assertTrue(dateObj.get("requiredDateWithDefault").getDefaultValue().isDateItem());
        assertEquals("2004-04-12Z", dateObj.get("requiredDateWithDefault").getDefaultValue().getStringValue());
        assertTrue(dateObj.get("uniqueDate").isUnique());
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }

    @Test
    public void testAnnotate() {
        TYSONObject tysonObject = (TYSONObject) schemaItem.annotate(fileItem);
        assertTrue(tysonObject.containsKey("dates"));
        TYSONArray tysonArray = (TYSONArray) tysonObject.get("dates");
        for (TYSONItem item : tysonArray) {
            TYSONObject object = (TYSONObject) item;
            assertEquals("dateObj", object.getTypeName());

            assertTrue(object.containsKey("requiredDate"));
            assertEquals("date", object.get("requiredDate").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredDate")).getItemValue().isDateItem());

            assertTrue(object.containsKey("dateWithDefault"));
            assertEquals("date", object.get("dateWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("dateWithDefault")).getItemValue().isDateItem());

            assertTrue(object.containsKey("requiredDateWithDefault"));
            assertEquals("date", object.get("requiredDateWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredDateWithDefault")).getItemValue().isDateItem());
        }

        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(1)).get("nullableDate"))).getItemValue().isNullItem()
        );
        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(2)).get("nullableDate"))).getItemValue().isDateItem()
        );
        assertEquals(
            "2001-02-28",
            ((TYSONValue) (((TYSONObject) tysonArray.get(3)).get("dateWithDefault"))).getItemValue()
                .getStringValue()
        );

        assertEquals(
            "2001-12-16",
            ((TYSONValue) (((TYSONObject) tysonArray.get(4)).get("requiredDateWithDefault"))).getItemValue()
                .getStringValue()
        );
        assertEquals(
            "dateType",
            (((TYSONObject) tysonArray.get(5)).get("anotherDate")).getTypeName()
        );
        assertNotEquals(
            ((TYSONValue) (((TYSONObject) tysonArray.get(6)).get("uniqueDate"))).getItemValue(),
            ((TYSONValue) (((TYSONObject) tysonArray.get(7)).get("uniqueDate"))).getItemValue()
        );

    }
}
