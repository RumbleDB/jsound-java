package extendedSchemas.atomicTypes.string.enumeration;

import base.BaseTest;
import jsound.atomicItems.StringItem;
import org.api.Item;
import org.api.ItemWrapper;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumerationTest extends BaseTest {
    String filePath = "atomicTypes/string/enumeration/stringEnumeration.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/string/enumerationSchema.json",
                "targetType",
                false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("stringType").isStringType());
        assertTrue(schema.get("stringObj").isObjectType());
        assertTrue(
            schema.get("stringObj")
                .getFacets()
                .getObjectContent()
                .get("myString")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isStringType()
        );
    }

    @Test
    public void testEnumeration() {
        List<StringItem> values = Arrays.asList(
            new StringItem("first"),
            new StringItem("second"),
            new StringItem("third"),
            new StringItem("fourth")
        );
        List<Item> enumValues = schema.get("stringType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("stringType").getFacets().getEnumeration().size(), values.size());
        for (StringItem value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("strings").getItem().getItems())
            assertTrue(values.contains((StringItem) itemWrapper.getItem().getItemMap().get("myString").getItem()));
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
