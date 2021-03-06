package compactSchemas;

import base.BaseTest;
import jsound.typedescriptors.object.FieldDescriptor;
import jsound.types.AtomicTypes;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GeneralTest extends BaseTest {
    private static final String filePath = "generalFile.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> person;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "generalSchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        person = schema.get("person").getFacets().getObjectContent();
    }

    @Test
    public void testSchema() {
        String[] keys = new String[] { "targetType", "persons", "person" };
        Set<String> keySet = new HashSet<>(Arrays.asList(keys));

        for (AtomicTypes type : AtomicTypes.values()) {
            assertTrue(schema.containsKey(type.getTypeName()));
        }
        assertTrue(schema.keySet().containsAll(keySet));

        assertTrue(schema.get("targetType").isObjectType());
        assertTrue(schema.get("persons").isArrayType());
        assertTrue(schema.get("person").isObjectType());

        Map<String, FieldDescriptor> targetType = schema.get("targetType").getFacets().getObjectContent();
        assertTrue(targetType.containsKey("people"));
        assertTrue(targetType.get("people").getTypeOrReference().getTypeDescriptor().isArrayType());
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
        assertTrue(person.get("birthDate").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(
            person.get("birthDate")
                .getTypeOrReference()
                .getTypeDescriptor()
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getTypeDescriptor()
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
                .getTypeDescriptor()
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
