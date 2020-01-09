package compactSchemas.atomicTypes.nullType;

import base.BaseTest;
import jsound.typedescriptors.object.FieldDescriptor;
import jsound.tyson.TYSONArray;
import jsound.tyson.TYSONObject;
import jsound.tyson.TYSONValue;
import jsound.tyson.TysonItem;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NullTest extends BaseTest {
    private static final String filePath = "atomicTypes/null/nullFile.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> nullObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/null/nullSchema.json";
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
        nullObj = schema.get("nullObj").getFacets().getObjectContent();
    }

    @Test
    public void testGeneral() {
        assertTrue(schema.get("nullType").isNullType());
        assertTrue(schema.get("nullObj").isObjectType());
        assertTrue(schema.get("arrayOfNulls").isArrayType());
    }

    @Test
    public void testNullObj() {
        assertTrue(nullObj.get("myNull").getTypeOrReference().getTypeDescriptor().isNullType());
        assertTrue(nullObj.get("requiredNull").getTypeOrReference().getTypeDescriptor().isNullType());
        assertTrue(nullObj.get("requiredNull").isRequired());
        assertTrue(nullObj.get("nullableNull").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(
            nullObj.get("nullableNull")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getType()
                .isNullType()
        );
        assertTrue(
            nullObj.get("nullableNull")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getType()
                .isNullType()
        );
        assertTrue(nullObj.get("nullWithDefault").getTypeOrReference().getTypeDescriptor().isNullType());
        assertTrue(nullObj.get("nullWithDefault").getDefaultValue().isNullItem());
        assertEquals("null", nullObj.get("nullWithDefault").getDefaultValue().getStringValue());
        assertTrue(
            nullObj.get("requiredNullWithDefault").getTypeOrReference().getTypeDescriptor().isNullType()
        );
        assertTrue(nullObj.get("requiredNullWithDefault").isRequired());
        assertTrue(nullObj.get("requiredNullWithDefault").getDefaultValue().isNullItem());
        assertEquals("null", nullObj.get("requiredNullWithDefault").getDefaultValue().getStringValue());
        assertTrue(nullObj.get("uniqueNull").isUnique());
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }

    @Test
    public void testAnnotate() {
        TYSONObject tysonObject = (TYSONObject) schemaItem.annotate(fileItem);
        assertTrue(tysonObject.containsKey("nulls"));
        TYSONArray tysonArray = (TYSONArray) tysonObject.get("nulls");
        for (TysonItem item : tysonArray) {
            TYSONObject object = (TYSONObject) item;
            assertEquals("nullObj", object.getTypeName());

            assertTrue(object.containsKey("requiredNull"));
            assertEquals("null", object.get("requiredNull").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredNull")).getItemValue().isNullItem());

            assertTrue(object.containsKey("nullWithDefault"));
            assertEquals("null", object.get("nullWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("nullWithDefault")).getItemValue().isNullItem());

            assertTrue(object.containsKey("requiredNullWithDefault"));
            assertEquals("null", object.get("requiredNullWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredNullWithDefault")).getItemValue().isNullItem());
        }

        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(1)).get("nullableNull"))).getItemValue().isNullItem()
        );
        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(2)).get("nullableNull"))).getItemValue().isNullItem()
        );
        assertEquals(
            "null",
            ((TYSONValue) (((TYSONObject) tysonArray.get(3)).get("nullWithDefault"))).getItemValue()
                .getStringValue()
        );

        assertEquals(
            "null",
            ((TYSONValue) (((TYSONObject) tysonArray.get(4)).get("requiredNullWithDefault"))).getItemValue()
                .getStringValue()
        );
        assertEquals(
            "nullType",
            (((TYSONObject) tysonArray.get(5)).get("anotherNull")).getTypeName()
        );
    }
}
