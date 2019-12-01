package org.jsound.type;

import org.jsound.api.ItemType;

public class UserDefinedType extends ItemType {
    private String name;
    private ItemType type;

    UserDefinedType(String name, ItemType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }
}
