package extendedSchemas.atomicTypes.hexBinary.enumeration;

import base.BaseTest;
import jsound.atomicItems.HexBinaryItem;
import org.api.Item;
import org.api.ItemWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
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
            "extendedSchemas/atomicTypes/hexBinary/enumerationSchema.json",
            "atomicTypes/hexBinary/enumeration/hexBinaryEnumeration.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("hexBinaryType").isHexBinaryType());
        assertTrue(schema.get("hexBinaryObj").isObjectType());
        assertTrue(
            schema.get("hexBinaryObj")
                .getFacets()
                .getObjectContent()
                .get("myHexBinary")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isHexBinaryType()
        );
    }

    @Test
    public void testEnumeration() {
        List<HexBinaryItem> values = Arrays.asList(
            new HexBinaryItem(HexBinaryItem.parseHexBinaryString("0123456789abcdef"), "0123456789abcdef"),
            new HexBinaryItem(HexBinaryItem.parseHexBinaryString("aBCd12"),"aBCd12"),
            new HexBinaryItem(HexBinaryItem.parseHexBinaryString("9521"), "9521"),
            new HexBinaryItem(HexBinaryItem.parseHexBinaryString("AAAA"), "AAAA")
        );
        List<Item> enumValues = schema.get("hexBinaryType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("hexBinaryType").getFacets().getEnumeration().size(), values.size());
        for (HexBinaryItem value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("hexBinaries").getItem().getItems())
            assertTrue(values.contains((HexBinaryItem) itemWrapper.getItem().getItemMap().get("myHexBinary").getItem()));
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
