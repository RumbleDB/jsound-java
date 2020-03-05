package extendedSchemas.atomicTypes.anyURI.enumeration;

import base.BaseTest;
import jsound.atomicItems.AnyURIItem;
import org.api.Item;
import org.api.ItemWrapper;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumerationTest extends BaseTest {
    String filePath = "atomicTypes/anyURI/enumeration/anyURIEnumeration.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/anyURI/enumerationSchema.json",
                "targetType",
                false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("anyURIType").isAnyURIType());
        assertTrue(schema.get("anyURIObj").isObjectType());
        assertTrue(
            schema.get("anyURIObj")
                .getFacets()
                .getObjectContent()
                .get("myAnyURI")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isAnyURIType()
        );
    }

    @Test
    public void testEnumeration() {
        List<AnyURIItem> values = Arrays.asList(
            new AnyURIItem("http://datypic.com", URI.create("http://datypic.com")),
            new AnyURIItem("../prod.html#shirt", URI.create("../prod.html#shirt")),
            new AnyURIItem("../arinaldi.html", URI.create("../arinaldi.html")),
            new AnyURIItem(
                    "https://gitlab.inf.ethz.ch/gfourny/jsound-20-java",
                    URI.create("https://gitlab.inf.ethz.ch/gfourny/jsound-20-java")
            )
        );
        List<Item> enumValues = schema.get("anyURIType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("anyURIType").getFacets().getEnumeration().size(), values.size());
        for (AnyURIItem value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("anyURIs").getItem().getItems())
            assertTrue(values.contains((AnyURIItem) itemWrapper.getItem().getItemMap().get("myAnyURI").getItem()));
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
