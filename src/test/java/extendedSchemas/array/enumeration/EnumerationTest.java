package extendedSchemas.array.enumeration;

import base.BaseTest;
import jsound.atomicItems.StringItem;
import jsound.item.ArrayItem;
import org.api.Item;
import org.api.ItemWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
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
            "extendedSchemas/array/enumerationSchema.json",
            "array/enumeration/arrayEnumeration.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("arrayType").isArrayType());
        assertTrue(schema.get("arrayObj").isObjectType());
        assertTrue(
            schema.get("arrayObj")
                .getFacets()
                .getObjectContent()
                .get("myArray")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isArrayType()
        );
    }

    @Test
    public void testEnumeration() {
        List<ArrayItem> values = Arrays.asList(
            new ArrayItem(
                new ArrayList<>(
                    Arrays.asList(
                            new ItemWrapper(new StringItem("first first")),
                            new ItemWrapper(new StringItem("second first"))
                    )
                )
            ),
            new ArrayItem(
                    new ArrayList<>(
                            Arrays.asList(
                                    new ItemWrapper(new StringItem("first second")),
                                    new ItemWrapper(new StringItem("second second"))
                            )
                    )
            ),
            new ArrayItem(
                    new ArrayList<>(
                            Arrays.asList(
                                    new ItemWrapper(new StringItem("first third")),
                                    new ItemWrapper(new StringItem("second third"))
                            )
                    )
            )
        );
        List<Item> enumValues = schema.get("arrayType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("arrayType").getFacets().getEnumeration().size(), values.size());
        for (ArrayItem value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("arrays").getItem().getItems())
            assertTrue(values.contains((ArrayItem) itemWrapper.getItem().getItemMap().get("myArray").getItem()));
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
