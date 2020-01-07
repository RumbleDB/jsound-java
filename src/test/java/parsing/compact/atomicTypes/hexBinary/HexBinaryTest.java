package parsing.compact.atomicTypes.hexBinary;

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

public class HexBinaryTest extends BaseTest {
    static String filePath = "src/main/resources/compact/atomicTypes/hexBinary/hexBinaryFile.json";
    static String schemaPath = "src/main/resources/compact/atomicTypes/hexBinary/hexBinarySchema.json";
    static String rootType = "rootType";
    public static boolean compact = true;

    private static Map<String, FieldDescriptor> hexBinaryObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
        hexBinaryObj = schema.get("hexBinaryObj").getFacets().getObjectContent();
    }

    @Test
    public void testGeneral() {
        assertTrue(schema.get("hexBinaryType").isHexBinaryType());
        assertTrue(schema.get("hexBinaryObj").isObjectType());
        assertTrue(schema.get("arrayOfHexBinaries").isArrayType());
    }

    @Test
    public void testHexBinaryObj() {
        assertTrue(hexBinaryObj.get("myHexBinary").getTypeOrReference().getTypeDescriptor().isHexBinaryType());
        assertTrue(hexBinaryObj.get("requiredHexBinary").getTypeOrReference().getTypeDescriptor().isHexBinaryType());
        assertTrue(hexBinaryObj.get("requiredHexBinary").isRequired());
        assertTrue(hexBinaryObj.get("nullableHexBinary").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(
            hexBinaryObj.get("nullableHexBinary")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getType()
                .isHexBinaryType()
        );
        assertTrue(
            hexBinaryObj.get("nullableHexBinary")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getType()
                .isNullType()
        );
        assertTrue(hexBinaryObj.get("hexBinaryWithDefault").getTypeOrReference().getTypeDescriptor().isHexBinaryType());
        assertTrue(hexBinaryObj.get("hexBinaryWithDefault").getDefaultValue().isHexBinaryItem());
        assertEquals("0123abcd", hexBinaryObj.get("hexBinaryWithDefault").getDefaultValue().getStringValue());
        assertTrue(
            hexBinaryObj.get("requiredHexBinaryWithDefault").getTypeOrReference().getTypeDescriptor().isHexBinaryType()
        );
        assertTrue(hexBinaryObj.get("requiredHexBinaryWithDefault").isRequired());
        assertTrue(hexBinaryObj.get("requiredHexBinaryWithDefault").getDefaultValue().isHexBinaryItem());
        assertEquals("aaBB", hexBinaryObj.get("requiredHexBinaryWithDefault").getDefaultValue().getStringValue());
        assertTrue(hexBinaryObj.get("uniqueHexBinary").isUnique());
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }

    @Test
    public void testAnnotate() {
        TYSONObject tysonObject = (TYSONObject) schemaItem.annotate(fileItem);
        assertTrue(tysonObject.containsKey("hexBinaries"));
        TYSONArray tysonArray = (TYSONArray) tysonObject.get("hexBinaries");
        for (TysonItem item : tysonArray) {
            TYSONObject object = (TYSONObject) item;
            assertEquals("hexBinaryObj", object.getTypeName());

            assertTrue(object.containsKey("requiredHexBinary"));
            assertEquals("hexBinary", ((TYSONValue) object.get("requiredHexBinary")).getTypeName());
            assertTrue(((TYSONValue) object.get("requiredHexBinary")).getItemValue().isHexBinaryItem());

            assertTrue(object.containsKey("hexBinaryWithDefault"));
            assertEquals("hexBinary", ((TYSONValue) object.get("hexBinaryWithDefault")).getTypeName());
            assertTrue(((TYSONValue) object.get("hexBinaryWithDefault")).getItemValue().isHexBinaryItem());

            assertTrue(object.containsKey("requiredHexBinaryWithDefault"));
            assertEquals("hexBinary", ((TYSONValue) object.get("requiredHexBinaryWithDefault")).getTypeName());
            assertTrue(((TYSONValue) object.get("requiredHexBinaryWithDefault")).getItemValue().isHexBinaryItem());
        }

        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(1)).get("nullableHexBinary"))).getItemValue().isNullItem()
        );
        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(2)).get("nullableHexBinary"))).getItemValue().isHexBinaryItem()
        );
        assertEquals(
            "abc12345",
            ((TYSONValue) (((TYSONObject) tysonArray.get(3)).get("hexBinaryWithDefault"))).getItemValue()
                .getStringValue()
        );

        assertEquals(
            "aBCd12",
            ((TYSONValue) (((TYSONObject) tysonArray.get(4)).get("requiredHexBinaryWithDefault"))).getItemValue()
                .getStringValue()
        );
        assertEquals(
            "hexBinaryType",
            ((TYSONValue) (((TYSONObject) tysonArray.get(5)).get("anotherHexBinary"))).getTypeName()
        );
        assertNotEquals(
            ((TYSONValue) (((TYSONObject) tysonArray.get(6)).get("uniqueHexBinary"))).getItemValue(),
            ((TYSONValue) (((TYSONObject) tysonArray.get(7)).get("uniqueHexBinary"))).getItemValue()
        );

    }
}
