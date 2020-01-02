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

public class IntegerTest extends BaseTest {
    static String filePath = "src/main/resources/compact/atomictypes/integer/integerFile.json";
    static String schemaPath = "src/main/resources/compact/atomictypes/integer/integerSchema.json";
    static String rootType = "rootType";
    public static boolean compact = true;

    private static Map<String, FieldDescriptor> integerObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
        integerObj = schema.get("integerObj").getFacets().getObjectContent();
    }

    @Test
    public void testGeneral() {
        assertTrue(schema.get("integerType").isIntegerType());
        assertTrue(schema.get("integerObj").isObjectType());
        assertTrue(schema.get("arrayOfIntegers").isArrayType());
    }

    @Test
    public void testintegerObj() {
        assertTrue(integerObj.get("myInteger").getTypeOrReference().getTypeDescriptor().isIntegerType());
        assertTrue(integerObj.get("requiredInteger").getTypeOrReference().getTypeDescriptor().isIntegerType());
        assertTrue(integerObj.get("requiredInteger").isRequired());
        assertTrue(integerObj.get("nullableInteger").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(
                integerObj.get("nullableInteger")
                        .getTypeOrReference()
                        .getTypeDescriptor()
                        .getFacets()
                        .getUnionContent()
                        .getTypes()
                        .get(0)
                        .getType()
                        .isIntegerType()
        );
        assertTrue(
                integerObj.get("nullableInteger")
                        .getTypeOrReference()
                        .getTypeDescriptor()
                        .getFacets()
                        .getUnionContent()
                        .getTypes()
                        .get(1)
                        .getType()
                        .isNullType()
        );
        assertTrue(integerObj.get("integerWithDefault").getTypeOrReference().getTypeDescriptor().isIntegerType());
        assertTrue(integerObj.get("integerWithDefault").getDefaultValue().isIntegerItem());
        assertEquals("42", integerObj.get("integerWithDefault").getDefaultValue().getStringValue());
        assertTrue(
                integerObj.get("requiredIntegerWithDefault").getTypeOrReference().getTypeDescriptor().isIntegerType()
        );
        assertTrue(integerObj.get("requiredIntegerWithDefault").isRequired());
        assertTrue(integerObj.get("requiredIntegerWithDefault").getDefaultValue().isIntegerItem());
        assertEquals("666", integerObj.get("requiredIntegerWithDefault").getDefaultValue().getStringValue());
        assertTrue(integerObj.get("uniqueInteger").isUnique());
    }

    @Test
    public void testValiinteger() {
        assertTrue(schemaItem.validate(fileItem, false));
    }

    @Test
    public void testAnnotate() {
        TYSONObject tysonObject = (TYSONObject) schemaItem.annotate(fileItem);
        assertTrue(tysonObject.containsKey("integers"));
        TYSONArray tysonArray = (TYSONArray) tysonObject.get("integers");
        for (TysonItem item : tysonArray) {
            TYSONObject object = (TYSONObject) item;
            assertEquals("integerObj", object.getTypeName());

            assertTrue(object.containsKey("requiredInteger"));
            assertEquals("integer", ((TYSONValue) object.get("requiredInteger")).getTypeName());
            assertTrue(((TYSONValue) object.get("requiredInteger")).getItemValue().isIntegerItem());

            assertTrue(object.containsKey("integerWithDefault"));
            assertEquals("integer", ((TYSONValue) object.get("integerWithDefault")).getTypeName());
            assertTrue(((TYSONValue) object.get("integerWithDefault")).getItemValue().isIntegerItem());

            assertTrue(object.containsKey("requiredIntegerWithDefault"));
            assertEquals("integer", ((TYSONValue) object.get("requiredIntegerWithDefault")).getTypeName());
            assertTrue(((TYSONValue) object.get("requiredIntegerWithDefault")).getItemValue().isIntegerItem());
        }

        assertTrue(
                ((TYSONValue) (((TYSONObject) tysonArray.get(1)).get("nullableInteger"))).getItemValue().isNullItem()
        );
        assertTrue(
                ((TYSONValue) (((TYSONObject) tysonArray.get(2)).get("nullableInteger"))).getItemValue().isIntegerItem()
        );
        assertEquals(
                "7",
                ((TYSONValue) (((TYSONObject) tysonArray.get(3)).get("integerWithDefault"))).getItemValue()
                        .getStringValue()
        );

        assertEquals(
                "9",
                ((TYSONValue) (((TYSONObject) tysonArray.get(4)).get("requiredIntegerWithDefault"))).getItemValue()
                        .getStringValue()
        );
        assertEquals(
                "integerType",
                ((TYSONValue) (((TYSONObject) tysonArray.get(5)).get("anotherInteger"))).getTypeName()
        );
        assertNotEquals(
                ((TYSONValue) (((TYSONObject) tysonArray.get(6)).get("uniqueInteger"))).getItemValue(),
                ((TYSONValue) (((TYSONObject) tysonArray.get(7)).get("uniqueInteger"))).getItemValue()
        );

    }
}
