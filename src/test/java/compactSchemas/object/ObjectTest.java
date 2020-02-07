package compactSchemas.object;

import base.BaseTest;
import jsound.atomicItems.IntegerItem;
import jsound.atomicItems.StringItem;
import jsound.item.ObjectItem;
import jsound.typedescriptors.object.FieldDescriptor;
import jsound.tyson.TYSONArray;
import jsound.tyson.TYSONObject;
import jsound.tyson.TYSONValue;
import jsound.tyson.TYSONItem;
import org.api.ItemWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ObjectTest extends BaseTest {
    private static final String filePath = "object/objectFile.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> objectType;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "object/objectSchema.json";
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
        objectType = schema.get("objectType").getFacets().getObjectContent();
    }

    @Test
    public void testGeneral() {
        assertTrue(schema.get("objectType").isObjectType());
        assertTrue(schema.get("myObject").isObjectType());
        assertTrue(schema.get("arrayOfObjects").isArrayType());
    }

    @Test
    public void testObjectObj() {
        assertTrue(objectType.get("myObject").getTypeOrReference().getTypeDescriptor().isObjectType());
        assertTrue(objectType.get("requiredObject").getTypeOrReference().getTypeDescriptor().isObjectType());
        assertTrue(objectType.get("requiredObject").isRequired());
        assertTrue(objectType.get("nullableObject").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(
            objectType.get("nullableObject")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getTypeDescriptor()
                .isObjectType()
        );
        assertTrue(
            objectType.get("nullableObject")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getType()
                .isNullType()
        );
        assertTrue(objectType.get("objectWithDefault").getTypeOrReference().getTypeDescriptor().isObjectType());
        assertTrue(objectType.get("objectWithDefault").getDefaultValue().isObjectItem());

        Map<String, ItemWrapper> itemMap = new HashMap<>();
        itemMap.put("stringField", new ItemWrapper(new StringItem("hello")));
        itemMap.put("integerField", new ItemWrapper(new IntegerItem(1)));
        ObjectItem objectItem = new ObjectItem(itemMap);
        assertEquals(objectItem, objectType.get("objectWithDefault").getDefaultValue().getItem());
        assertTrue(
            objectType.get("requiredObjectWithDefault").getTypeOrReference().getTypeDescriptor().isObjectType()
        );
        assertTrue(objectType.get("requiredObjectWithDefault").isRequired());
        assertTrue(objectType.get("requiredObjectWithDefault").getDefaultValue().isObjectItem());

        Map<String, ItemWrapper> itemMap2 = new HashMap<>();
        itemMap2.put("stringField", new ItemWrapper(new StringItem("hello")));
        itemMap2.put("integerField", new ItemWrapper(new IntegerItem(1)));
        Map<String, ItemWrapper> itemMap3 = new HashMap<>();
        itemMap3.put("stringField", new ItemWrapper(new StringItem("world!")));
        itemMap3.put("integerField", new ItemWrapper(new IntegerItem(2)));
        itemMap2.put("objectField", new ItemWrapper(new ObjectItem(itemMap3)));
        ObjectItem objectItem2 = new ObjectItem(itemMap2);
        assertEquals(objectItem2, objectType.get("requiredObjectWithDefault").getDefaultValue().getItem());
        assertTrue(objectType.get("uniqueObject").isUnique());
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }

    @Test
    public void testAnnotate() {
        TYSONObject tysonObject = (TYSONObject) schemaItem.annotate(fileItem);
        assertTrue(tysonObject.containsKey("objects"));
        TYSONArray tysonArray = (TYSONArray) tysonObject.get("objects");
        for (TYSONItem item : tysonArray) {
            TYSONObject object = (TYSONObject) item;
            assertEquals("objectType", object.getTypeName());

            assertTrue(object.containsKey("requiredObject"));
            assertEquals("myObject", (object.get("requiredObject")).getTypeName());
            assertTrue(
                ((TYSONValue) (((TYSONObject) object.get("requiredObject")).get("stringField"))).getItemValue()
                    .isStringItem()
            );
            assertTrue(
                ((TYSONValue) (((TYSONObject) object.get("requiredObject")).get("integerField"))).getItemValue()
                    .isIntegerItem()
            );

            assertTrue(object.containsKey("objectWithDefault"));
            assertEquals("myObject", (object.get("objectWithDefault")).getTypeName());
            assertTrue(
                ((TYSONValue) (((TYSONObject) object.get("objectWithDefault")).get("stringField"))).getItemValue()
                    .isStringItem()
            );
            assertTrue(
                ((TYSONValue) (((TYSONObject) object.get("objectWithDefault")).get("integerField"))).getItemValue()
                    .isIntegerItem()
            );

            assertTrue(object.containsKey("requiredObjectWithDefault"));
            assertEquals("myObject", (object.get("requiredObjectWithDefault")).getTypeName());
            assertTrue(
                ((TYSONValue) (((TYSONObject) object.get("requiredObjectWithDefault")).get("stringField")))
                    .getItemValue()
                    .isStringItem()
            );
            assertTrue(
                ((TYSONValue) (((TYSONObject) object.get("requiredObjectWithDefault")).get("integerField")))
                    .getItemValue()
                    .isIntegerItem()
            );
        }

        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(1)).get("nullableObject"))).getItemValue().isNullItem()
        );
        assertEquals(
            "gpu",
            ((TYSONValue) ((TYSONObject) ((TYSONObject) tysonArray.get(2)).get("nullableObject")).get("stringField"))
                .getItemValue()
                .getStringValue()
        );
        assertEquals(
            "2",
            ((TYSONValue) ((TYSONObject) ((TYSONObject) tysonArray.get(2)).get("nullableObject")).get("integerField"))
                .getItemValue()
                .getStringValue()
        );

        assertEquals(
            "hello",
            ((TYSONValue) ((TYSONObject) ((TYSONObject) tysonArray.get(3)).get("requiredObjectWithDefault")).get(
                "stringField"
            )).getItemValue().getStringValue()
        );
        assertEquals(
            "1",
            ((TYSONValue) ((TYSONObject) ((TYSONObject) tysonArray.get(3)).get("requiredObjectWithDefault")).get(
                "integerField"
            )).getItemValue().getStringValue()
        );

        assertEquals(
            "ram",
            ((TYSONValue) ((TYSONObject) ((TYSONObject) tysonArray.get(4)).get("requiredObjectWithDefault")).get(
                "stringField"
            )).getItemValue().getStringValue()
        );
        assertEquals(
            "16",
            ((TYSONValue) ((TYSONObject) ((TYSONObject) tysonArray.get(4)).get("requiredObjectWithDefault")).get(
                "integerField"
            )).getItemValue().getStringValue()
        );

        assertEquals(
            "myObjectType",
            (((TYSONObject) tysonArray.get(5)).get("anotherObject")).getTypeName()
        );
        assertNotEquals(
            (((TYSONObject) tysonArray.get(6)).get("uniqueObject")),
            (((TYSONObject) tysonArray.get(7)).get("uniqueObject"))
        );

    }
}
