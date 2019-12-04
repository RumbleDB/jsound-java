package org.tyson;


public class TYSONValue implements TysonItem {

    private String typeName;
    private String itemValue;

    public TYSONValue(String typeName, String itemValue) {
        this.typeName = typeName;
        this.itemValue = itemValue;
    }

    private static String toTYSONString(TYSONValue tysonValue) {
        return tysonValue == null
            ? "null"
            : (tysonValue.typeName == null ? "" : "(\"" + tysonValue.typeName + "\") ") + tysonValue.itemValue;
    }

    public String toTYSONString() {
        return toTYSONString(this);
    }
}
