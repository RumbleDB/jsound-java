package org.tyson;

import org.jsound.api.AtomicItem;

public class TYSONValue implements TysonItem {

    private String typeName;
    private AtomicItem item;

    public TYSONValue(String typeName, AtomicItem item) {
        this.typeName = typeName;
        this.item = item;
    }

    private static String toTYSONString(TYSONValue value) {
        return value == null ? "null" : "(\"" + value.typeName + "\") " + value.item.getAnnotationString();
    }

    public String toTYSONString() {
        return toTYSONString(this);
    }
}
