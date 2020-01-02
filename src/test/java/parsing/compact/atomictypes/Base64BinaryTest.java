package parsing.compact.atomictypes;

import jsound.typedescriptors.object.FieldDescriptor;
import jsound.tyson.TYSONArray;
import jsound.tyson.TYSONObject;
import jsound.tyson.TYSONValue;
import jsound.tyson.TysonItem;
import org.junit.BeforeClass;
import org.junit.Test;
import parsing.BaseTest;

import java.io.IOException;
import java.util.Map;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class Base64BinaryTest extends BaseTest {
    static String filePath = "src/main/resources/compact/atomictypes/base64Binary/base64BinaryFile.json";
    static String schemaPath = "src/main/resources/compact/atomictypes/base64Binary/base64BinarySchema.json";
    static String rootType = "rootType";
    public static boolean compact = true;

    private static Map<String, FieldDescriptor> base64BinaryObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
        base64BinaryObj = schema.get("base64BinaryObj").getFacets().getObjectContent();
    }

    @Test
    public void testGeneral() {
        assertTrue(schema.get("base64BinaryType").isBase64BinaryType());
        assertTrue(schema.get("base64BinaryObj").isObjectType());
        assertTrue(schema.get("arrayOfBase64Binaries").isArrayType());
    }

    @Test
    public void testbase64BinaryObj() {
        assertTrue(base64BinaryObj.get("myBase64Binary").getTypeOrReference().getTypeDescriptor().isBase64BinaryType());
        assertTrue(
            base64BinaryObj.get("requiredBase64Binary").getTypeOrReference().getTypeDescriptor().isBase64BinaryType()
        );
        assertTrue(base64BinaryObj.get("requiredBase64Binary").isRequired());
        assertTrue(base64BinaryObj.get("nullableBase64Binary").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(
            base64BinaryObj.get("nullableBase64Binary")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getType()
                .isBase64BinaryType()
        );
        assertTrue(
            base64BinaryObj.get("nullableBase64Binary")
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
            base64BinaryObj.get("base64BinaryWithDefault").getTypeOrReference().getTypeDescriptor().isBase64BinaryType()
        );
        assertTrue(base64BinaryObj.get("base64BinaryWithDefault").getDefaultValue().isBase64BinaryItem());
        assertEquals("abcd", base64BinaryObj.get("base64BinaryWithDefault").getDefaultValue().getStringValue());
        assertTrue(
            base64BinaryObj.get("requiredBase64BinaryWithDefault")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isBase64BinaryType()
        );
        assertTrue(base64BinaryObj.get("requiredBase64BinaryWithDefault").isRequired());
        assertTrue(base64BinaryObj.get("requiredBase64BinaryWithDefault").getDefaultValue().isBase64BinaryItem());
        assertEquals(
            "abcdefgh",
            base64BinaryObj.get("requiredBase64BinaryWithDefault").getDefaultValue().getStringValue()
        );
        assertTrue(base64BinaryObj.get("uniqueBase64Binary").isUnique());
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }

    @Test
    public void testAnnotate() {
        TYSONObject tysonObject = (TYSONObject) schemaItem.annotate(fileItem);
        assertTrue(tysonObject.containsKey("base64Binaries"));
        TYSONArray tysonArray = (TYSONArray) tysonObject.get("base64Binaries");
        for (TysonItem item : tysonArray) {
            TYSONObject object = (TYSONObject) item;
            assertEquals("base64BinaryObj", object.getTypeName());

            assertTrue(object.containsKey("requiredBase64Binary"));
            assertEquals("base64Binary", ((TYSONValue) object.get("requiredBase64Binary")).getTypeName());
            assertTrue(((TYSONValue) object.get("requiredBase64Binary")).getItemValue().isBase64BinaryItem());

            assertTrue(object.containsKey("base64BinaryWithDefault"));
            assertEquals("base64Binary", ((TYSONValue) object.get("base64BinaryWithDefault")).getTypeName());
            assertTrue(((TYSONValue) object.get("base64BinaryWithDefault")).getItemValue().isBase64BinaryItem());

            assertTrue(object.containsKey("requiredBase64BinaryWithDefault"));
            assertEquals("base64Binary", ((TYSONValue) object.get("requiredBase64BinaryWithDefault")).getTypeName());
            assertTrue(((TYSONValue) object.get("requiredBase64BinaryWithDefault")).getItemValue().isBase64BinaryItem());
        }

        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(1)).get("nullableBase64Binary"))).getItemValue().isNullItem()
        );
        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(2)).get("nullableBase64Binary"))).getItemValue().isBase64BinaryItem()
        );
        assertEquals(
            "abc12345",
            ((TYSONValue) (((TYSONObject) tysonArray.get(3)).get("base64BinaryWithDefault"))).getItemValue()
                .getStringValue()
        );

        assertEquals(
            "a b c =",
            ((TYSONValue) (((TYSONObject) tysonArray.get(4)).get("requiredBase64BinaryWithDefault"))).getItemValue()
                .getStringValue()
        );
        assertEquals(
            "base64BinaryType",
            ((TYSONValue) (((TYSONObject) tysonArray.get(5)).get("anotherBase64Binary"))).getTypeName()
        );
        assertNotEquals(
            ((TYSONValue) (((TYSONObject) tysonArray.get(6)).get("uniqueBase64Binary"))).getItemValue(),
            ((TYSONValue) (((TYSONObject) tysonArray.get(7)).get("uniqueBase64Binary"))).getItemValue()
        );

    }
}
