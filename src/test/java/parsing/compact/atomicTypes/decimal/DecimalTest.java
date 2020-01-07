package parsing.compact.atomicTypes.decimal;

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

public class DecimalTest extends BaseTest {
    static String filePath = "src/main/resources/compact/atomicTypes/decimal/decimalFile.json";
    static String schemaPath = "src/main/resources/compact/atomicTypes/decimal/decimalSchema.json";
    static String rootType = "rootType";
    public static boolean compact = true;

    private static Map<String, FieldDescriptor> decimalObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
        decimalObj = schema.get("decimalObj").getFacets().getObjectContent();
    }

    @Test
    public void testGeneral() {
        assertTrue(schema.get("decimalType").isDecimalType());
        assertTrue(schema.get("decimalObj").isObjectType());
        assertTrue(schema.get("arrayOfDecimals").isArrayType());
    }

    @Test
    public void testDecimalObj() {
        assertTrue(decimalObj.get("myDecimal").getTypeOrReference().getTypeDescriptor().isDecimalType());
        assertTrue(decimalObj.get("requiredDecimal").getTypeOrReference().getTypeDescriptor().isDecimalType());
        assertTrue(decimalObj.get("requiredDecimal").isRequired());
        assertTrue(decimalObj.get("nullableDecimal").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(
            decimalObj.get("nullableDecimal")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getType()
                .isDecimalType()
        );
        assertTrue(
            decimalObj.get("nullableDecimal")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getType()
                .isNullType()
        );
        assertTrue(decimalObj.get("decimalWithDefault").getTypeOrReference().getTypeDescriptor().isDecimalType());
        assertTrue(decimalObj.get("decimalWithDefault").getDefaultValue().isDecimalItem());
        assertEquals("42.21", decimalObj.get("decimalWithDefault").getDefaultValue().getStringValue());
        assertTrue(
            decimalObj.get("requiredDecimalWithDefault").getTypeOrReference().getTypeDescriptor().isDecimalType()
        );
        assertTrue(decimalObj.get("requiredDecimalWithDefault").isRequired());
        assertTrue(decimalObj.get("requiredDecimalWithDefault").getDefaultValue().isDecimalItem());
        assertEquals("666.333", decimalObj.get("requiredDecimalWithDefault").getDefaultValue().getStringValue());
        assertTrue(decimalObj.get("uniqueDecimal").isUnique());
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }

    @Test
    public void testAnnotate() {
        TYSONObject tysonObject = (TYSONObject) schemaItem.annotate(fileItem);
        assertTrue(tysonObject.containsKey("decimals"));
        TYSONArray tysonArray = (TYSONArray) tysonObject.get("decimals");
        for (TysonItem item : tysonArray) {
            TYSONObject object = (TYSONObject) item;
            assertEquals("decimalObj", object.getTypeName());

            assertTrue(object.containsKey("requiredDecimal"));
            assertEquals("decimal", object.get("requiredDecimal").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredDecimal")).getItemValue().isDecimalItem());

            assertTrue(object.containsKey("decimalWithDefault"));
            assertEquals("decimal", object.get("decimalWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("decimalWithDefault")).getItemValue().isDecimalItem());

            assertTrue(object.containsKey("requiredDecimalWithDefault"));
            assertEquals("decimal", object.get("requiredDecimalWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredDecimalWithDefault")).getItemValue().isDecimalItem());
        }

        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(1)).get("nullableDecimal"))).getItemValue().isNullItem()
        );
        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(2)).get("nullableDecimal"))).getItemValue().isDecimalItem()
        );
        assertEquals(
            "7.65",
            ((TYSONValue) (((TYSONObject) tysonArray.get(3)).get("decimalWithDefault"))).getItemValue()
                .getStringValue()
        );

        assertEquals(
            "9.76537436",
            ((TYSONValue) (((TYSONObject) tysonArray.get(4)).get("requiredDecimalWithDefault"))).getItemValue()
                .getStringValue()
        );
        assertEquals(
            "decimalType",
            ((TYSONObject) tysonArray.get(5)).get("anotherDecimal").getTypeName()
        );
        assertNotEquals(
            ((TYSONValue) (((TYSONObject) tysonArray.get(6)).get("uniqueDecimal"))).getItemValue(),
            ((TYSONValue) (((TYSONObject) tysonArray.get(7)).get("uniqueDecimal"))).getItemValue()
        );

    }
}
