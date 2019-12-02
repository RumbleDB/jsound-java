package org.jsound.type;

import jsound.exceptions.UnexpectedTypeException;
import org.jsound.api.ItemType;
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

    public ItemType getType() {
        if (type == null) {
            type = JSoundValidateExecutor.getInstance().getUserDefinedItemType(name);
            if (type == null) {
                throw new UnexpectedTypeException("This type is not defined " + name);
            }
        }
        return type;
    }

    @Override
    public boolean isUserDefinedType() {
        return true;
    }
}
