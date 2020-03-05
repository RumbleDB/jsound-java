package compactSchemas.atomicTypes.anyURI;

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

public class AnyURITest extends BaseTest {
    private static final String filePath = "atomicTypes/anyURI/anyURIFile.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> anyURIObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/anyURI/anyURISchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        anyURIObj = schema.get("anyURIObj").getFacets().getObjectContent();
    }

    @Test
    public void testGeneral() {
        assertTrue(schema.get("anyURIType").isAnyURIType());
        assertTrue(schema.get("anyURIObj").isObjectType());
        assertTrue(schema.get("arrayOfAnyURIs").isArrayType());
    }

    @Test
    public void testAnyURIObj() {
        assertTrue(anyURIObj.get("myAnyURI").getTypeOrReference().getTypeDescriptor().isAnyURIType());
        assertTrue(
            anyURIObj.get("requiredAnyURI").getTypeOrReference().getTypeDescriptor().isAnyURIType()
        );
        assertTrue(anyURIObj.get("requiredAnyURI").isRequired());
        assertTrue(anyURIObj.get("nullableAnyURI").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(
            anyURIObj.get("nullableAnyURI")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getType()
                .isAnyURIType()
        );
        assertTrue(
            anyURIObj.get("nullableAnyURI")
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
            anyURIObj.get("anyURIWithDefault").getTypeOrReference().getTypeDescriptor().isAnyURIType()
        );
        assertTrue(anyURIObj.get("anyURIWithDefault").getDefaultValue().isAnyURIItem());
        assertEquals("http://gitlab.com", anyURIObj.get("anyURIWithDefault").getDefaultValue().getStringValue());
        assertTrue(
            anyURIObj.get("requiredAnyURIWithDefault")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isAnyURIType()
        );
        assertTrue(anyURIObj.get("requiredAnyURIWithDefault").isRequired());
        assertTrue(anyURIObj.get("requiredAnyURIWithDefault").getDefaultValue().isAnyURIItem());
        assertEquals(
            "../prod.html",
            anyURIObj.get("requiredAnyURIWithDefault").getDefaultValue().getStringValue()
        );
        assertTrue(anyURIObj.get("uniqueAnyURI").isUnique());
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }

    @Test
    public void testAnnotate() throws IOException {
        TYSONObject tysonObject = (TYSONObject) jSoundSchema.annotateJSONFromPath(filePathPrefix + filePath);
        assertTrue(tysonObject.containsKey("anyURIs"));
        TYSONArray tysonArray = (TYSONArray) tysonObject.get("anyURIs");
        for (TYSONItem item : tysonArray) {
            TYSONObject object = (TYSONObject) item;
            assertEquals("anyURIObj", object.getTypeName());

            assertTrue(object.containsKey("requiredAnyURI"));
            assertEquals("anyURI", object.get("requiredAnyURI").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredAnyURI")).getItemValue().isAnyURIItem());

            assertTrue(object.containsKey("anyURIWithDefault"));
            assertEquals("anyURI", object.get("anyURIWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("anyURIWithDefault")).getItemValue().isAnyURIItem());

            assertTrue(object.containsKey("requiredAnyURIWithDefault"));
            assertEquals("anyURI", object.get("requiredAnyURIWithDefault").getTypeName());
            assertTrue(((TYSONValue) object.get("requiredAnyURIWithDefault")).getItemValue().isAnyURIItem());
        }

        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(1)).get("nullableAnyURI"))).getItemValue().isNullItem()
        );
        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(2)).get("nullableAnyURI"))).getItemValue().isAnyURIItem()
        );
        assertEquals(
            "urn:example:org",
            ((TYSONValue) (((TYSONObject) tysonArray.get(3)).get("anyURIWithDefault"))).getItemValue()
                .getStringValue()
        );

        assertEquals(
            "https://www.w3.org/TR/xquery-30/#id-expressions-on-datatypes",
            ((TYSONValue) (((TYSONObject) tysonArray.get(4)).get("requiredAnyURIWithDefault"))).getItemValue()
                .getStringValue()
        );
        assertEquals(
            "anyURIType",
            (((TYSONObject) tysonArray.get(5)).get("anotherAnyURI")).getTypeName()
        );
        assertNotEquals(
            ((TYSONValue) (((TYSONObject) tysonArray.get(6)).get("uniqueAnyURI"))).getItemValue(),
            ((TYSONValue) (((TYSONObject) tysonArray.get(7)).get("uniqueAnyURI"))).getItemValue()
        );

    }
}
