package org.jsound.type;

import jsound.exceptions.UnexpectedTypeException;
import org.jsound.api.ItemType;
import org.jsound.api.ItemTypes;
import org.jsound.cli.JSoundValidateExecutor;

public class UserDefinedType extends ItemType {
    private String name;
    private ItemType itemType;

    UserDefinedType(String name, ItemType itemType) {
        super(ItemTypes.USERDEFINED);
        this.type.setTypeName(name);
        this.name = name;
        this.itemType = itemType;
    }

    @Override
    public ItemType getItemType() {
        if (itemType == null) {
            itemType = JSoundValidateExecutor.getUserDefinedItemType(name);
            if (itemType == null) {
                throw new UnexpectedTypeException("This itemType is not defined " + name);
            }
            itemType = itemType.getItemType();
        }
        return itemType;
    }

    @Override
    public String getTypeName() {
        return name;
    }

    @Override
    public boolean isUserDefinedType() {
        return true;
    }
}
