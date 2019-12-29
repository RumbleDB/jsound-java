package parsing.compact;

import org.api.executors.JSoundExecutor;
import jsound.types.AtomicTypes;
import jsound.typedescriptors.object.FieldDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;
import parsing.BaseTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApplicationInitTest extends BaseTest {
    static String filePath = "src/main/resources/compact/peopleFile.json";
    static String schemaPath = "src/main/resources/compact/peopleSchema.json";
    static String rootType = "directory";
    public static boolean compact = true;

    private static Map<String, FieldDescriptor> person;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
        person = schema.get("person").getFacets().getObjectContent();
    }

    @Test
    public void testSchema() {
        String[] keys = new String[] { "directory", "persons", "person" };
        Set<String> keySet = new HashSet<>(Arrays.asList(keys));

        for (AtomicTypes type : AtomicTypes.values()) {
            assertTrue(schema.containsKey(type.getTypeName()));
        }
        assertTrue(schema.keySet().containsAll(keySet));

        assertTrue(schema.get("directory").isObjectType());
        assertTrue(schema.get("persons").isArrayType());
        assertTrue(schema.get("person").isObjectType());

        Map<String, FieldDescriptor> directory = schema.get("directory").getFacets().getObjectContent();
        assertTrue(directory.containsKey("people"));
        assertTrue(directory.get("people").getTypeOrReference().getTypeDescriptor().isArrayType());
    }

    @Test
    public void testRequiredField() {
        assertTrue(person.containsKey("first"));
        assertTrue(person.get("first").getTypeOrReference().getTypeDescriptor().isStringType());
        assertTrue(person.get("first").isRequired());
    }

    @Test
    public void testFieldWithDefaultValue() {
        assertTrue(person.containsKey("last"));
        assertTrue(person.get("last").getTypeOrReference().getTypeDescriptor().isStringType());
        assertTrue(person.get("last").getDefaultValue().isStringItem());
        assertEquals("N/A", person.get("last").getDefaultValue().getStringValue());
    }

    @Test
    public void testUniqueField() {
        assertTrue(person.containsKey("picture"));
        assertTrue(person.get("picture").getTypeOrReference().getTypeDescriptor().isHexBinaryType());
        assertTrue(person.get("picture").isUnique());
    }

    @Test
    public void testUnionTypeField() {
        assertTrue(person.containsKey("birthDate"));
        assertTrue(person.get("birthDate").getTypeOrReference().getType().isUnionType());
        assertTrue(
            person.get("birthDate")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getType()
                .isDateType()
        );
        assertTrue(
            person.get("birthDate")
                .getTypeOrReference()
                .getType()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getType()
                .isDateTimeType()
        );
    }

    @Test
    public void testFieldWithQuestionMark() {
        assertTrue(person.containsKey("maritalStatus"));
        assertTrue(
            person.get("maritalStatus")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getType()
                .isStringType()
        );
        assertTrue(
            person.get("maritalStatus")
                .getTypeOrReference()
                .getType()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getType()
                .isNullType()
        );
    }
}
