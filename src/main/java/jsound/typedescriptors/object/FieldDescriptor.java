package jsound.typedescriptors.object;

import jsound.typedescriptors.TypeOrReference;
import org.api.ItemWrapper;

public class FieldDescriptor {
    public String name;
    private TypeOrReference type;
    private boolean required = false;
    private ItemWrapper defaultValue = null;
    private boolean unique = false;
    private boolean requiredIsSet = false;
    public boolean defaultIsChecked = false;

    public void setName(String name) {
        this.name = name;
    }

    public void setType(TypeOrReference type) {
        this.type = type;
    }

    public void setRequired(Boolean required) {
        this.requiredIsSet = true;
        this.required = required;
    }

    public void setDefaultValue(ItemWrapper defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    public String getName() {
        return name;
    }

    public TypeOrReference getTypeOrReference() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public Boolean isUnique() {
        return unique;
    }

    public ItemWrapper getDefaultValue() {
        return defaultValue;
    }

    public boolean requiredIsSet() {
        return requiredIsSet;
    }

    public void isMoreRestrictive(ObjectTypeDescriptor baseTypeDescriptor) {
        this.getTypeOrReference().getTypeDescriptor().checkBaseType();
        this.getTypeOrReference()
            .getTypeDescriptor()
            .checkAgainstTypeDescriptor(
                baseTypeDescriptor.getFacets()
                    .getObjectContent()
                    .get(this.getName())
                    .getTypeOrReference()
                    .getTypeDescriptor()
            );
    }
}
