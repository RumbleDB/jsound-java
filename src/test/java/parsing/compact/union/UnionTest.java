package parsing.compact.union;

import jsound.atomicItems.IntegerItem;
import jsound.atomicItems.StringItem;
import jsound.atomicTypes.DayTimeDurationType;
import jsound.item.ArrayItem;
import jsound.typedescriptors.TypeOrReference;
import jsound.typedescriptors.object.FieldDescriptor;
import jsound.types.ItemTypes;
import jsound.tyson.TYSONArray;
import jsound.tyson.TYSONObject;
import jsound.tyson.TYSONValue;
import jsound.tyson.TysonItem;
import org.api.ItemWrapper;
import org.junit.BeforeClass;
import org.junit.Test;
import parsing.BaseTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class UnionTest extends BaseTest {
    static String filePath = "src/main/resources/compact/union/unionFile.json";
    static String schemaPath = "src/main/resources/compact/union/unionSchema.json";
    static String rootType = "rootType";
    public static boolean compact = true;

    private static Map<String, FieldDescriptor> unionObject;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
        unionObject = schema.get("unionObj").getFacets().getObjectContent();
    }

    @Test
    public void testGeneral() {
        assertTrue(schema.get("durations").isUnionType());
        assertTrue(schema.get("dateTimes").isUnionType());
        assertTrue(schema.get("binaries").isUnionType());
        assertTrue(schema.get("numbers").isUnionType());
        assertTrue(schema.get("recursiveBinaries").isUnionType());

        assertEquals(schema.get("durations")
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getTypeDescriptor()
                .getType(), ItemTypes.DAYTIMEDURATION);
        assertEquals(schema.get("durations")
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getTypeDescriptor()
                .getType(), ItemTypes.YEARMONTHDURATION);
        assertEquals(schema.get("durations")
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(2)
                .getTypeDescriptor()
                .getType(), ItemTypes.DURATION);

        assertEquals(schema.get("dateTimes")
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getTypeDescriptor()
                .getType(), ItemTypes.DATE);
        assertEquals(schema.get("dateTimes")
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getTypeDescriptor()
                .getType(), ItemTypes.TIME);
        assertEquals(schema.get("dateTimes")
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(2)
                .getTypeDescriptor()
                .getType(), ItemTypes.DATETIME);

        assertEquals(schema.get("binaries")
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getTypeDescriptor()
                .getType(), ItemTypes.HEXBINARY);
        assertEquals(schema.get("binaries")
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getTypeDescriptor()
                .getType(), ItemTypes.BASE64BINARY);

        assertEquals(schema.get("numbers")
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(0)
                .getTypeDescriptor()
                .getType(), ItemTypes.DOUBLE);
        assertEquals(schema.get("numbers")
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(1)
                .getTypeDescriptor()
                .getType(), ItemTypes.DECIMAL);
        assertEquals(schema.get("numbers")
                .getFacets()
                .getUnionContent()
                .getTypes()
                .get(2)
                .getTypeDescriptor()
                .getType(), ItemTypes.INTEGER);
        assertEquals(schema.get("recursiveBinaries"), schema.get("binaries"));
    }

    @Test
    public void testArrayObj() {
        assertTrue(unionObject.get("myDurations").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(unionObject.get("requiredNumbers").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(unionObject.get("requiredNumbers").isRequired());
        assertTrue(unionObject.get("nullableDateTimes").getTypeOrReference().getTypeDescriptor().isUnionType());
        assertTrue(
                unionObject.get("nullableDateTimes")
                        .getTypeOrReference()
                        .getTypeDescriptor()
                        .getFacets()
                        .getUnionContent()
                        .getTypes()
                        .get(0)
                        .getType()
                        .isUnionType()
        );
        assertTrue(
                unionObject.get("nullableDateTimes")
                        .getTypeOrReference()
                        .getTypeDescriptor()
                        .getFacets()
                        .getUnionContent()
                        .getTypes()
                        .get(1)
                        .getType()
                        .isNullType()
        );
        assertTrue(
                unionObject.get("binariesWithDefault").getTypeOrReference().getTypeDescriptor().isUnionType()
        );
        assertTrue(unionObject.get("binariesWithDefault").getDefaultValue().isBase64BinaryItem());
        assertEquals("0F+40A==", unionObject.get("binariesWithDefault").getDefaultValue().getItem().getStringValue());
        assertTrue(
                unionObject.get("requiredDurationsWithDefault").getTypeOrReference().getTypeDescriptor().isUnionType()
        );
        assertTrue(unionObject.get("requiredDurationsWithDefault").isRequired());
        assertTrue(unionObject.get("requiredDurationsWithDefault").getDefaultValue().isDayTimeDurationItem());
        assertEquals(
                "P3DT2M",
                unionObject.get("requiredDurationsWithDefault").getDefaultValue().getItem().getStringValue()
        );
        assertTrue(unionObject.get("uniqueDurations").isUnique());
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }

    @Test
    public void testAnnotate() {
        TYSONObject tysonObject = (TYSONObject) schemaItem.annotate(fileItem);
        assertTrue(tysonObject.containsKey("unions"));
        TYSONArray tysonArray = (TYSONArray) tysonObject.get("unions");
        for (TysonItem item : tysonArray) {
            TYSONObject object = (TYSONObject) item;
            assertEquals("unionObj", object.getTypeName());

            assertTrue(object.containsKey("requiredNumbers"));
            assertTrue(object.get("requiredNumbers").getTypeName().equals("double")
            || object.get("requiredNumbers").getTypeName().equals("decimal")
            || object.get("requiredNumbers").getTypeName().equals("integer"));

            assertTrue(object.containsKey("binariesWithDefault"));
            assertTrue(object.get("binariesWithDefault").getTypeName().equals("hexBinary")
            || object.get("binariesWithDefault").getTypeName().equals("base64Binary"));

            assertTrue(object.containsKey("requiredDurationsWithDefault"));
            assertTrue(object.get("requiredDurationsWithDefault").getTypeName().equals("dayTimeDuration")
                    || object.get("requiredDurationsWithDefault").getTypeName().equals("yearMonthDuration")
                    || object.get("requiredDurationsWithDefault").getTypeName().equals("duration"));
        }

        assertTrue(
                ((TYSONValue) (((TYSONObject) tysonArray.get(1)).get("nullableDateTimes"))).getItemValue().isNullItem()
        );

        assertEquals(
                "2004-04-12T13:20:00+14:00",
                ((TYSONValue) ((TYSONObject) tysonArray.get(2)).get("nullableDateTimes"))
                        .getItemValue()
                        .getStringValue()
        );

        assertEquals(
                "ZW FzdX JlLg ==",
                ((TYSONValue) ((TYSONObject) tysonArray.get(3)).get("binariesWithDefault"))
                        .getItemValue()
                        .getStringValue()
        );

        assertEquals(
                "-P33Y3M",
                ((TYSONValue) ((TYSONObject) tysonArray.get(4)).get("requiredDurationsWithDefault"))
                        .getItemValue()
                        .getStringValue()
        );


        assertEquals(
                "hexBinary",
                (((TYSONObject) tysonArray.get(5)).get("recursiveBinaryField")).getTypeName()
        );
        assertNotEquals(
                ((TYSONObject) tysonArray.get(6)).get("uniqueDurations"),
                ((TYSONObject) tysonArray.get(7)).get("uniqueDurations")
        );

    }
}
