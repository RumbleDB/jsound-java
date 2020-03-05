package extendedSchemas.atomicTypes.nullType.enumeration;

import base.BaseTest;
import jsound.atomicItems.NullItem;
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
    String filePath = "atomicTypes/null/enumeration/nullEnumeration.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/null/enumerationSchema.json",
                "targetType",
                false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("nullType").isNullType());
        assertTrue(schema.get("nullObj").isObjectType());
        assertTrue(
            schema.get("nullObj")
                .getFacets()
                .getObjectContent()
                .get("myNull")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isNullType()
        );
    }

    @Test
    public void testEnumeration() {
        List<NullItem> values = Arrays.asList(
            new NullItem(),
            new NullItem()
        );
        List<Item> enumValues = schema.get("nullType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("nullType").getFacets().getEnumeration().size(), values.size());
        for (NullItem value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("nulls").getItem().getItems())
            assertTrue(values.contains((NullItem) itemWrapper.getItem().getItemMap().get("myNull").getItem()));
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
