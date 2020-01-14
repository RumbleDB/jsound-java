package extendedSchemas.atomicTypes.doubleType.facets;

import base.BaseTest;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertFalse;

public class InvalidBoundariesFacetsTest extends BaseTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
                "extendedSchemas/atomicTypes/double/facets/doubleBoundariesSchema.json",
                "atomicTypes/double/facets/boundariesFacetsError.json",
                false
        );
    }


    @Test
    public void testInvalidValues() {
        TypeDescriptor doubleObj = schema.get("doubleObj");
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("doubles").getItem().getItems()) {
            if(
                    doubleObj.validate(itemWrapper, false)
            )
                System.out.println("hello");
        }
    }
}
