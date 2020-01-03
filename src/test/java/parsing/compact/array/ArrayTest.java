package parsing.compact.array;

import jsound.atomicItems.IntegerItem;
import jsound.atomicItems.StringItem;
import jsound.item.ArrayItem;
import jsound.typedescriptors.object.FieldDescriptor;
import jsound.typedescriptors.union.UnionTypeDescriptor;
import jsound.tyson.TYSONArray;
import jsound.tyson.TYSONObject;
import jsound.tyson.TYSONValue;
import jsound.tyson.TysonItem;
import org.api.ItemWrapper;
import org.junit.BeforeClass;
import org.junit.Test;
import parsing.BaseTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ArrayTest extends BaseTest {
    static String filePath = "src/main/resources/compact/array/arrayFile.json";
    static String schemaPath = "src/main/resources/compact/array/arraySchema.json";
    static String rootType = "rootType";
    public static boolean compact = true;

    private static Map<String, FieldDescriptor> arrayObject;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
        arrayObject = schema.get("arrayObj").getFacets().getObjectContent();
    }

    @Test
    public void testGeneral() {
        assertTrue(schema.get("arrayOfStrings").isArrayType());
        assertTrue(
            schema.get("arrayOfStrings").getFacets().getArrayContent().getType().getTypeDescriptor().isStringType()
        );
        assertTrue(schema.get("arrayOfIntegers").isArrayType());
        assertTrue(
            schema.get("arrayOfIntegers").getFacets().getArrayContent().getType().getTypeDescriptor().isIntegerType()
        );
        assertTrue(schema.get("arrayOfBinaries").isArrayType());
        assertTrue(
            schema.get("arrayOfBinaries").getFacets().getArrayContent().getType().getTypeDescriptor().isUnionType()
        );
        assertTrue(
            schema.get("arrayOfBinaries")
                .getFacets()
                .getArrayContent()
                .getType()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getTypeDescriptor()
                .isHexBinaryType()
        );
        assertTrue(
            schema.get("arrayOfBinaries")
                .getFacets()
                .getArrayContent()
                .getType()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getTypeDescriptor()
                .isBase64BinaryType()
        );
        assertTrue(schema.get("recArrayOfStrings").isArrayType());
        assertTrue(
            schema.get("recArrayOfStrings").getFacets().getArrayContent().getType().getTypeDescriptor().isStringType()
        );
        assertTrue(schema.get("arrayObj").isObjectType());
        assertTrue(schema.get("arrayOfArrays").isArrayType());
    }

    @Test
    public void testArrayObj() {
        assertTrue(arrayObject.get("myArrayOfStrings").getTypeOrReference().getTypeDescriptor().isArrayType());
        assertTrue(arrayObject.get("requiredArrayOfIntegers").getTypeOrReference().getTypeDescriptor().isArrayType());
        assertTrue(arrayObject.get("requiredArrayOfIntegers").isRequired());
        assertTrue(arrayObject.get("nullableArrayOfStrings").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(
            arrayObject.get("nullableArrayOfStrings")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getType()
                .isArrayType()
        );
        assertTrue(
            arrayObject.get("nullableArrayOfStrings")
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
            arrayObject.get("arrayOfIntegersWithDefault").getTypeOrReference().getTypeDescriptor().isArrayType()
        );
        assertTrue(arrayObject.get("arrayOfIntegersWithDefault").getDefaultValue().isArrayItem());

        List<ItemWrapper> items = new ArrayList<>();
        for (int i = 1; i <= 3; i++)
            items.add(new ItemWrapper(new IntegerItem(i)));
        assertEquals(new ArrayItem(items), arrayObject.get("arrayOfIntegersWithDefault").getDefaultValue().getItem());
        assertTrue(
            arrayObject.get("requiredArrayOfStringsWithDefault").getTypeOrReference().getTypeDescriptor().isArrayType()
        );
        assertTrue(arrayObject.get("requiredArrayOfStringsWithDefault").isRequired());
        assertTrue(arrayObject.get("requiredArrayOfStringsWithDefault").getDefaultValue().isArrayItem());

        List<ItemWrapper> items2 = new ArrayList<>();
        items2.add(new ItemWrapper(new StringItem("hello")));
        items2.add(new ItemWrapper(new StringItem("world!")));
        assertEquals(
            new ArrayItem(items2),
            arrayObject.get("requiredArrayOfStringsWithDefault").getDefaultValue().getItem()
        );
        assertTrue(arrayObject.get("uniqueArrayOfBinaries").isUnique());
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }

    @Test
    public void testAnnotate() {
        TYSONObject tysonObject = (TYSONObject) schemaItem.annotate(fileItem);
        assertTrue(tysonObject.containsKey("arrays"));
        TYSONArray tysonArray = (TYSONArray) tysonObject.get("arrays");
        for (TysonItem item : tysonArray) {
            TYSONObject object = (TYSONObject) item;
            assertEquals("arrayObj", object.getTypeName());

            assertTrue(object.containsKey("requiredArrayOfIntegers"));
            assertEquals("arrayOfIntegers", (object.get("requiredArrayOfIntegers")).getTypeName());
            assertTrue(object.get("requiredArrayOfIntegers") instanceof TYSONArray);

            assertTrue(object.containsKey("arrayOfIntegersWithDefault"));
            assertEquals("arrayOfIntegers", (object.get("arrayOfIntegersWithDefault")).getTypeName());
            assertTrue(object.get("arrayOfIntegersWithDefault") instanceof TYSONArray);

            assertTrue(object.containsKey("requiredArrayOfStringsWithDefault"));
            assertEquals("arrayOfStrings", (object.get("requiredArrayOfStringsWithDefault")).getTypeName());
            assertTrue(object.get("requiredArrayOfStringsWithDefault") instanceof TYSONArray);

        }

        assertTrue(
            ((TYSONValue) (((TYSONObject) tysonArray.get(1)).get("nullableArrayOfStrings"))).getItemValue().isNullItem()
        );
        assertEquals(
            "ciao",
            ((TYSONValue) ((TYSONArray) ((TYSONObject) tysonArray.get(2)).get("nullableArrayOfStrings")).get(0))
                .getItemValue()
                .getStringValue()
        );
        assertEquals(
            "hello",
            ((TYSONValue) ((TYSONArray) ((TYSONObject) tysonArray.get(2)).get("nullableArrayOfStrings")).get(1))
                .getItemValue()
                .getStringValue()
        );
        assertEquals(
            "hola",
            ((TYSONValue) ((TYSONArray) ((TYSONObject) tysonArray.get(2)).get("nullableArrayOfStrings")).get(2))
                .getItemValue()
                .getStringValue()
        );

        assertEquals(
            "1000",
            ((TYSONValue) ((TYSONArray) ((TYSONObject) tysonArray.get(3)).get("arrayOfIntegersWithDefault")).get(0))
                .getItemValue()
                .getStringValue()
        );
        assertEquals(
            "1",
            ((TYSONValue) ((TYSONArray) ((TYSONObject) tysonArray.get(3)).get("arrayOfIntegersWithDefault")).get(1))
                .getItemValue()
                .getStringValue()
        );
        assertEquals(
            "1200",
            ((TYSONValue) ((TYSONArray) ((TYSONObject) tysonArray.get(3)).get("arrayOfIntegersWithDefault")).get(2))
                .getItemValue()
                .getStringValue()
        );


        assertEquals(
            "sunny",
            ((TYSONValue) ((TYSONArray) ((TYSONObject) tysonArray.get(4)).get("requiredArrayOfStringsWithDefault")).get(
                0
            )).getItemValue().getStringValue()
        );
        assertEquals(
            "rainy",
            ((TYSONValue) ((TYSONArray) ((TYSONObject) tysonArray.get(4)).get("requiredArrayOfStringsWithDefault")).get(
                1
            )).getItemValue().getStringValue()
        );
        assertEquals(
            "foggy",
            ((TYSONValue) ((TYSONArray) ((TYSONObject) tysonArray.get(4)).get("requiredArrayOfStringsWithDefault")).get(
                2
            )).getItemValue().getStringValue()
        );


        assertEquals(
            "arrayOfStrings",
            (((TYSONObject) tysonArray.get(5)).get("recursiveArrayOfStrings")).getTypeName()
        );
        assertNotEquals(
            ((TYSONObject) tysonArray.get(6)).get("uniqueArrayOfBinaries"),
            ((TYSONObject) tysonArray.get(7)).get("uniqueArrayOfBinaries")
        );

    }
}
