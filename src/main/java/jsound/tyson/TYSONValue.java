package jsound.tyson;


import org.api.Item;

public class TYSONValue implements TysonItem {

    private String typeName;
    private Item itemValue;

    public TYSONValue(String typeName, Item itemValue) {
        this.typeName = typeName;
        this.itemValue = itemValue;
    }

    private static String toTYSONString(TYSONValue tysonValue) {
        return tysonValue == null
            ? "null"
            : (tysonValue.typeName == null ? "" : "(\"" + tysonValue.typeName + "\") ")
                + tysonValue.itemValue.getStringAnnotation();
    }

    public String toTYSONString() {
        return toTYSONString(this);
    }

    public String getTypeName() {
        return typeName;
    }

    public Item getItemValue() {
        return itemValue;
    }
}
