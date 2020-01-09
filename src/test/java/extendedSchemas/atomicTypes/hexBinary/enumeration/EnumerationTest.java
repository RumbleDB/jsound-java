package extendedSchemas.atomicTypes.hexBinary.enumeration;

import base.BaseTest;
import jsound.atomicItems.AnyURIItem;
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
            "extendedSchemas/atomicTypes/anyURI/enumerationSchema.json",
            "atomicTypes/anyURI/enumeration/anyURIEnumeration.json",
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
        List<String> values = Arrays.asList(
            "http://datypic.com",
            "../prod.html#shirt",
            "../arinaldi.html",
            "https://gitlab.inf.ethz.ch/gfourny/jsound-20-java"
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
        for (String value : values) {
            assertTrue(enumValues.contains(new AnyURIItem(value, URI.create(value))));
        }

        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("anyURIs").getItem().getItems())
            assertTrue(values.contains(itemWrapper.getItem().getItemMap().get("myAnyURI").getItem().getStringValue()));
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
