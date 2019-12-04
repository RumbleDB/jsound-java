package org.jsound.type;

import jsound.exceptions.UnexpectedTypeException;
import org.jsound.api.ItemType;
import org.jsound.api.ItemTypes;
import org.jsound.cli.JSoundValidateExecutor;

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

    public ItemType getItemType() {
        if (type == null) {
            type = JSoundValidateExecutor.getUserDefinedItemType(name);
            if (type == null) {
                throw new UnexpectedTypeException("This type is not defined " + name);
            }
        }
        return type;
    }

    @Override
    public ItemTypes getType() {
        return this.getItemType().getType();
    }

    @Override
    public boolean isUserDefinedType() {
        return true;
    }
}
