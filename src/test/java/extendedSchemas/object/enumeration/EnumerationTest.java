package extendedSchemas.object.enumeration;

import base.BaseTest;
import jsound.atomicItems.IntegerItem;
import jsound.atomicItems.StringItem;
import jsound.item.ObjectItem;
import org.api.Item;
import org.api.ItemWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.api.executors.JSoundExecutor;
import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumerationTest extends BaseTest {
    String filePath = "object/enumeration/objectEnumeration.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/object/enumeration/enumerationSchema.json",
                "targetType",
                false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("objectType").isObjectType());
        assertTrue(schema.get("objectObj").isObjectType());
        assertTrue(
            schema.get("objectObj")
                .getFacets()
                .getObjectContent()
                .get("myObject")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isObjectType()
        );
    }

    @Test
    public void testEnumeration() {
        Map<String, ItemWrapper> firstMap = new HashMap<>();
        firstMap.put("firstKey", new ItemWrapper(new StringItem("firstValue")));
        firstMap.put("secondKey", new ItemWrapper(new IntegerItem(1)));

        Map<String, ItemWrapper> secondMap = new HashMap<>();
        secondMap.put("firstKey", new ItemWrapper(new StringItem("secondValue")));
        secondMap.put("secondKey", new ItemWrapper(new IntegerItem(2)));

        Map<String, ItemWrapper> thirdMap = new HashMap<>();
        thirdMap.put("firstKey", new ItemWrapper(new StringItem("thirdValue")));
        thirdMap.put("secondKey", new ItemWrapper(new IntegerItem(3)));

        Map<String, ItemWrapper> fourthMap = new HashMap<>();
        fourthMap.put("firstKey", new ItemWrapper(new StringItem("fourthValue")));
        fourthMap.put("secondKey", new ItemWrapper(new IntegerItem(4)));

        List<ObjectItem> values = Arrays.asList(
            new ObjectItem(firstMap),
            new ObjectItem(secondMap),
            new ObjectItem(thirdMap),
            new ObjectItem(fourthMap)
        );

        List<Item> enumValues = schema.get("objectType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("objectType").getFacets().getEnumeration().size(), values.size());
        for (ObjectItem value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("objects").getItem().getItems())
            assertTrue(values.contains((ObjectItem) itemWrapper.getItem().getItemMap().get("myObject").getItem()));
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
