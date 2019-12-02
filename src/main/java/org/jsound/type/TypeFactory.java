package org.jsound.type;


import org.jsound.api.ItemType;

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
}
