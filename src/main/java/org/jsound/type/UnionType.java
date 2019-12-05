package org.jsound.type;

import org.jsound.api.Item;
import org.jsound.api.ItemType;
import org.jsound.json.CompactSchemaFileJsonParser;
import org.tyson.TysonItem;

import java.util.ArrayList;
import java.util.List;

public class UnionType extends ItemType {

    private List<ItemType> _types = new ArrayList<>();

    UnionType(String typeString) {
        parseUnionType(typeString);
    }

    private void parseUnionType(String typeString) {
        String[] typesString = typeString.split("\\|");
        for (String type : typesString) {
            _types.add(CompactSchemaFileJsonParser.parseType(type));
        }
    }

    @Override
    public boolean isUnionType() {
        return true;
    }

    public boolean validate(Item item) {
        for (ItemType type : _types) {
            if (item.isValidAgainst(type))
                return true;
        }
        return false;
    }

    public TysonItem annotate(Item item) {
        for (ItemType type : _types) {
            if (item.isValidAgainst(type)) {
                return item.annotateWith(type);
            }
        }
        return null;
    }
}
