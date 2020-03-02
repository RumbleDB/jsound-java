package compactSchemas.atomicTypes.string;

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

public class StringTest extends BaseTest {
    private static final String filePath = "atomicTypes/string/stringFile.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> stringObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/string/stringSchema.json";
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
        stringObj = schema.get("stringObj").getFacets().getObjectContent();
    }

    @Test
    public void testGeneral() {
        assertTrue(schema.get("stringType").isStringType());
        assertTrue(schema.get("stringObj").isObjectType());
        assertTrue(schema.get("arrayOfStrings").isArrayType());
    }

    @Test
    public void testStringObj() {
        assertTrue(stringObj.get("myString").getTypeOrReference().getTypeDescriptor().isStringType());
        assertTrue(stringObj.get("requiredString").getTypeOrReference().getTypeDescriptor().isStringType());
        assertTrue(stringObj.get("requiredString").isRequired());
        assertTrue(stringObj.get("nullableString").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(
            stringObj.get("nullableString")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getType()
                .isStringType()
        );
        assertTrue(
            stringObj.get("nullableString")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getType()
                .isNullType()
        );
        assertTrue(stringObj.get("stringWithDefault").getTypeOrReference().getTypeDescriptor().isStringType());
        assertTrue(stringObj.get("stringWithDefault").getDefaultValue().isStringItem());
        assertEquals("defString1", stringObj.get("stringWithDefault").getDefaultValue().getStringValue());
        assertTrue(stringObj.get("requiredStringWithDefault").getTypeOrReference().getTypeDescriptor().isStringType());
        assertTrue(stringObj.get("requiredStringWithDefault").isRequired());
        assertTrue(stringObj.get("requiredStringWithDefault").getDefaultValue().isStringItem());
        assertEquals("defString2", stringObj.get("requiredStringWithDefault").getDefaultValue().getStringValue());
        assertTrue(stringObj.get("uniqueString").isUnique());
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }

    @Test
    public void testAnnotate() {
        TYSONObject tysonObject = (TYSONObject) schemaItem.annotate(fileItem);
        assertTrue(tysonObject.containsKey("strings"));
        TYSONArray tysonArray = (TYSONArray) tysonObject.get("strings");
        for (TYSONItem item : tysonArray) {
            TYSONObject object = (TYSONObject) item;
            assertEquals("stringObj", object.getTypeName());

            assertTrue(object.containsKey("requiredString"));
            assertEquals("string", object.get("requiredString").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredString")).getItemValue().isStringItem());

            assertTrue(object.containsKey("stringWithDefault"));
            assertEquals("string", object.get("stringWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("stringWithDefault")).getItemValue().isStringItem());

            assertTrue(object.containsKey("requiredStringWithDefault"));
            assertEquals("string", object.get("requiredStringWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredStringWithDefault")).getItemValue().isStringItem());
        }

        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(1)).get("nullableString"))).getItemValue().isNullItem()
        );
        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(2)).get("nullableString"))).getItemValue().isStringItem()
        );
        assertEquals(
            "override1",
            ((TYSONValue) (((TYSONObject) tysonArray.get(3)).get("stringWithDefault"))).getItemValue()
                .getStringValue()
        );

        assertEquals(
            "override2",
            ((TYSONValue) (((TYSONObject) tysonArray.get(4)).get("requiredStringWithDefault"))).getItemValue()
                .getStringValue()
        );
        assertEquals(
            "stringType",
            ((TYSONObject) tysonArray.get(5)).get("anotherString").getTypeName()
        );
        assertNotEquals(
            ((TYSONValue) (((TYSONObject) tysonArray.get(6)).get("uniqueString"))).getItemValue(),
            ((TYSONValue) (((TYSONObject) tysonArray.get(7)).get("uniqueString"))).getItemValue()
        );

    }
}
