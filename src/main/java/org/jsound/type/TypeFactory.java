package org.jsound.type;


import org.jsound.api.ItemType;
import org.jsound.facets.Facets;

import java.util.Map;

public class TypeFactory {

    private static TypeFactory _instance;

    public static TypeFactory getInstance() {
        if (_instance == null) {
            _instance = new TypeFactory();
        }
        return _instance;
    }

    public ItemType createStringType(String typeString) {
        return new StringType(typeString);
    }

    public ItemType createIntegerType(String typeString) {
        return new IntegerType(typeString);
    }

    public ItemType createDecimalType(String typeString) {
        return new DecimalType(typeString);
    }

    public ItemType createDoubleType(String typeString) {
        return new DoubleType(typeString);
    }

    public ItemType createBooleanType(String typeString) {
        return new BooleanType(typeString);
    }

    public ItemType createDurationType(String typeString) {
        return new DurationType(typeString);
    }

    public ItemType createYearMonthDurationType(String typeString) {
        return new YearMonthDurationType(typeString);
    }

    public ItemType createDayTimeDurationType(String typeString) {
        return new DayTimeDurationType(typeString);
    }

    public ItemType createDateTimeType(String typeString) {
        return new DateTimeType(typeString);
    }

    public ItemType createDateType(String typeString) {
        return new DateType(typeString);
    }

    public ItemType createTimeType(String typeString) {
        return new TimeType(typeString);
    }

    public ItemType createHexBinaryType(String typeString) {
        return new HexBinaryType(typeString);
    }

    public ItemType createBase64BinaryType(String typeString) {
        return new Base64BinaryType(typeString);
    }

    public ItemType createNullType() {
        return new NullType();
    }

    public ItemType createObjectType(Map<ObjectKey, ItemType> typeMap) {
        return new ObjectType(typeMap);
    }

    public ItemType createArrayType(ItemType arrayItemsType) {
        return new ArrayType(arrayItemsType);
    }

    public ItemType createUserDefinedItemType(String name, ItemType type) {
        return new UserDefinedType(name, type);
    }

    public ItemType createUnionType(String typeString) {
        return new UnionType(typeString);
    }

    public ItemType createAnyURIType(String name) {
        return new AnyURIType(name);
    }

    public ItemType createStringType(String typeString, Facets facets) {
        return new StringType(typeString, facets);
    }

    public ItemType createIntegerType(String name, Facets facets) {
        return new IntegerType(name, facets);
    }

    public ItemType createDecimalType(String name, Facets facets) {
        return new DecimalType(name, facets);
    }

    public ItemType createDoubleType(String name, Facets facets) {
        return new DoubleType(name, facets);
    }

    public ItemType createHexBinaryType(String name, Facets facets) {
        return new HexBinaryType(name, facets);
    }

    public ItemType createBase64BinaryType(String name, Facets facets) {
        return new Base64BinaryType(name, facets);
    }

    public ItemType createAnyURIType(String name, Facets facets) {
        return new AnyURIType(name, facets);
    }
}
