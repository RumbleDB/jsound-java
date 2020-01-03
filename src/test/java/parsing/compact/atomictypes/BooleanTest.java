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

public class BooleanTest extends BaseTest {
    static String filePath = "src/main/resources/compact/atomictypes/boolean/booleanFile.json";
    static String schemaPath = "src/main/resources/compact/atomictypes/boolean/booleanSchema.json";
    static String rootType = "rootType";
    public static boolean compact = true;

    private static Map<String, FieldDescriptor> booleanObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
        booleanObj = schema.get("booleanObj").getFacets().getObjectContent();
    }

    @Test
    public void testGeneral() {
        assertTrue(schema.get("booleanType").isBooleanType());
        assertTrue(schema.get("booleanObj").isObjectType());
        assertTrue(schema.get("arrayOfBooleans").isArrayType());
    }

    @Test
    public void testBooleanObj() {
        assertTrue(booleanObj.get("myBoolean").getTypeOrReference().getTypeDescriptor().isBooleanType());
        assertTrue(booleanObj.get("requiredBoolean").getTypeOrReference().getTypeDescriptor().isBooleanType());
        assertTrue(booleanObj.get("requiredBoolean").isRequired());
        assertTrue(booleanObj.get("nullableBoolean").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(
            booleanObj.get("nullableBoolean")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getType()
                .isBooleanType()
        );
        assertTrue(
            booleanObj.get("nullableBoolean")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getType()
                .isNullType()
        );
        assertTrue(booleanObj.get("booleanWithDefault").getTypeOrReference().getTypeDescriptor().isBooleanType());
        assertTrue(booleanObj.get("booleanWithDefault").getDefaultValue().isBooleanItem());
        assertEquals("true", booleanObj.get("booleanWithDefault").getDefaultValue().getStringValue());
        assertTrue(
            booleanObj.get("requiredBooleanWithDefault").getTypeOrReference().getTypeDescriptor().isBooleanType()
        );
        assertTrue(booleanObj.get("requiredBooleanWithDefault").isRequired());
        assertTrue(booleanObj.get("requiredBooleanWithDefault").getDefaultValue().isBooleanItem());
        assertEquals("false", booleanObj.get("requiredBooleanWithDefault").getDefaultValue().getStringValue());
        assertTrue(booleanObj.get("uniqueBoolean").isUnique());
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }

    @Test
    public void testAnnotate() {
        TYSONObject tysonObject = (TYSONObject) schemaItem.annotate(fileItem);
        assertTrue(tysonObject.containsKey("booleans"));
        TYSONArray tysonArray = (TYSONArray) tysonObject.get("booleans");
        for (TysonItem item : tysonArray) {
            TYSONObject object = (TYSONObject) item;
            assertEquals("booleanObj", object.getTypeName());

            assertTrue(object.containsKey("requiredBoolean"));
            assertEquals("boolean", ((TYSONValue) object.get("requiredBoolean")).getTypeName());
            assertTrue(((TYSONValue) object.get("requiredBoolean")).getItemValue().isBooleanItem());

            assertTrue(object.containsKey("booleanWithDefault"));
            assertEquals("boolean", ((TYSONValue) object.get("booleanWithDefault")).getTypeName());
            assertTrue(((TYSONValue) object.get("booleanWithDefault")).getItemValue().isBooleanItem());

            assertTrue(object.containsKey("requiredBooleanWithDefault"));
            assertEquals("boolean", ((TYSONValue) object.get("requiredBooleanWithDefault")).getTypeName());
            assertTrue(((TYSONValue) object.get("requiredBooleanWithDefault")).getItemValue().isBooleanItem());
        }

        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(1)).get("nullableBoolean"))).getItemValue().isNullItem()
        );
        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(2)).get("nullableBoolean"))).getItemValue().isBooleanItem()
        );
        assertEquals(
            "false",
            ((TYSONValue) (((TYSONObject) tysonArray.get(3)).get("booleanWithDefault"))).getItemValue()
                .getStringValue()
        );

        assertEquals(
            "true",
            ((TYSONValue) (((TYSONObject) tysonArray.get(4)).get("requiredBooleanWithDefault"))).getItemValue()
                .getStringValue()
        );
        assertEquals(
            "booleanType",
            ((TYSONValue) (((TYSONObject) tysonArray.get(5)).get("anotherBoolean"))).getTypeName()
        );
        assertNotEquals(
            ((TYSONValue) (((TYSONObject) tysonArray.get(6)).get("uniqueBoolean"))).getItemValue(),
            ((TYSONValue) (((TYSONObject) tysonArray.get(7)).get("uniqueBoolean"))).getItemValue()
        );

    }
}
