package org.jsound.type;

import org.jsound.api.ItemType;
import org.jsound.json.JsonParser;

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
            _types.add(JsonParser.parseType(type));
        }
    }

    public List<ItemType> getTypes() {
        return _types;
    }

    @Override
    public boolean isUnionType() {
        return true;
    }
}
