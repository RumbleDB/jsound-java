package extendedSchemas.atomicTypes.base64Binary.enumeration;

import base.BaseTest;
import jsound.atomicItems.Base64BinaryItem;
import org.api.Item;
import org.api.ItemWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumerationTest extends BaseTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/atomicTypes/base64Binary/enumerationSchema.json",
            "atomicTypes/base64Binary/enumeration/base64BinaryEnumeration.json",
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

        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("base64Binaries").getItem().getItems())
            assertTrue(
                values.contains((Base64BinaryItem) itemWrapper.getItem().getItemMap().get("myBase64Binary").getItem())
            );
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
