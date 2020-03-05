package compactSchemas.atomicTypes.doubleType;

import base.BaseTest;
import jsound.typedescriptors.object.FieldDescriptor;
import jsound.tyson.TYSONArray;
import jsound.tyson.TYSONItem;
import jsound.tyson.TYSONObject;
import jsound.tyson.TYSONValue;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class DoubleTest extends BaseTest {
    private static final String filePath = "atomicTypes/double/doubleFile.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> doubleObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/double/doubleSchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        doubleObj = schema.get("doubleObj").getFacets().getObjectContent();
    }

    @Test
    public void testGeneral() {
        assertTrue(schema.get("double").isDoubleType());
        assertTrue(schema.get("doubleObj").isObjectType());
        assertTrue(schema.get("arrayOfDoubles").isArrayType());
    }

    @Test
    public void testDoubleObj() {
        assertTrue(doubleObj.get("myDouble").getTypeOrReference().getTypeDescriptor().isDoubleType());
        assertTrue(doubleObj.get("requiredDouble").getTypeOrReference().getTypeDescriptor().isDoubleType());
        assertTrue(doubleObj.get("requiredDouble").isRequired());
        assertTrue(doubleObj.get("nullableDouble").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(
            doubleObj.get("nullableDouble")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getType()
                .isDoubleType()
        );
        assertTrue(
            doubleObj.get("nullableDouble")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getType()
                .isNullType()
        );
        assertTrue(doubleObj.get("doubleWithDefault").getTypeOrReference().getTypeDescriptor().isDoubleType());
        assertTrue(doubleObj.get("doubleWithDefault").getDefaultValue().isDoubleItem());
        assertEquals("420", doubleObj.get("doubleWithDefault").getDefaultValue().getStringValue());
        assertTrue(
            doubleObj.get("requiredDoubleWithDefault").getTypeOrReference().getTypeDescriptor().isDoubleType()
        );
        assertTrue(doubleObj.get("requiredDoubleWithDefault").isRequired());
        assertTrue(doubleObj.get("requiredDoubleWithDefault").getDefaultValue().isDoubleItem());
        assertEquals("6432.432", doubleObj.get("requiredDoubleWithDefault").getDefaultValue().getStringValue());
        assertTrue(doubleObj.get("uniqueDouble").isUnique());
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }

    @Test
    public void testAnnotate() throws IOException {
        TYSONObject tysonObject = (TYSONObject) jSoundSchema.annotateJSONFromPath(filePathPrefix + filePath);
        assertTrue(tysonObject.containsKey("doubles"));
        TYSONArray tysonArray = (TYSONArray) tysonObject.get("doubles");
        for (TYSONItem item : tysonArray) {
            TYSONObject object = (TYSONObject) item;
            assertEquals("doubleObj", object.getTypeName());

            assertTrue(object.containsKey("requiredDouble"));
            assertEquals("double", object.get("requiredDouble").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredDouble")).getItemValue().isDoubleItem());

            assertTrue(object.containsKey("doubleWithDefault"));
            assertEquals("double", object.get("doubleWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("doubleWithDefault")).getItemValue().isDoubleItem());

            assertTrue(object.containsKey("requiredDoubleWithDefault"));
            assertEquals("double", object.get("requiredDoubleWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredDoubleWithDefault")).getItemValue().isDoubleItem());
        }

        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(1)).get("nullableDouble"))).getItemValue().isNullItem()
        );
        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(2)).get("nullableDouble"))).getItemValue().isDoubleItem()
        );
        assertEquals(
            "723.112",
            ((TYSONValue) (((TYSONObject) tysonArray.get(3)).get("doubleWithDefault"))).getItemValue()
                .getStringValue()
        );

        assertEquals(
            "910",
            ((TYSONValue) (((TYSONObject) tysonArray.get(4)).get("requiredDoubleWithDefault"))).getItemValue()
                .getStringValue()
        );
        assertEquals(
            "doubleType",
            ((TYSONObject) tysonArray.get(5)).get("anotherDouble").getTypeName()
        );
        assertNotEquals(
            ((TYSONValue) (((TYSONObject) tysonArray.get(6)).get("uniqueDouble"))).getItemValue(),
            ((TYSONValue) (((TYSONObject) tysonArray.get(7)).get("uniqueDouble"))).getItemValue()
        );

    }
}
