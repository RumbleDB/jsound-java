package extendedSchemas.atomicTypes.base64Binary.enumeration;

import base.BaseTest;
import jsound.atomicItems.Base64BinaryItem;
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
    String filePath = "atomicTypes/base64Binary/enumeration/base64BinaryEnumeration.json";
    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/base64Binary/enumerationSchema.json",
                "targetType",
                false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("base64BinaryType").isBase64BinaryType());
        assertTrue(schema.get("base64BinaryObj").isObjectType());
        assertTrue(
            schema.get("base64BinaryObj")
                .getFacets()
                .getObjectContent()
                .get("myBase64Binary")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isBase64BinaryType()
        );
    }

    @Test
    public void testEnumeration() {
        List<Base64BinaryItem> values = Arrays.asList(
            new Base64BinaryItem(Base64BinaryItem.parseBase64BinaryString("ZW FzdX JlLg =="), "ZW FzdX JlLg =="),
            new Base64BinaryItem(Base64BinaryItem.parseBase64BinaryString("0F+40A=="), "0F+40A=="),
            new Base64BinaryItem(Base64BinaryItem.parseBase64BinaryString("0123456789abcdef"), "0123456789abcdef")
        );

        List<Item> enumValues = schema.get("base64BinaryType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("base64BinaryType").getFacets().getEnumeration().size(), values.size());
        for (Base64BinaryItem value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("base64Binaries").getItem().getItems())
            assertTrue(
                values.contains((Base64BinaryItem) itemWrapper.getItem().getItemMap().get("myBase64Binary").getItem())
            );
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
