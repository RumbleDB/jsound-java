package jsound.typedescriptors.object;

import jsound.exceptions.LessRestrictiveFacetException;
import jsound.typedescriptors.TypeOrReference;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;

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
        if (this.getTypeOrReference().getTypeDescriptor().isUnionType())
            this.getTypeOrReference()
                .getTypeDescriptor()
                .checkAgainstTypeDescriptor(
                    baseTypeDescriptor.getFacets()
                        .getObjectContent()
                        .get(this.getName())
                        .getTypeOrReference()
                        .getTypeDescriptor()
                );
        else
            checkAgainstTypeDescriptor(baseTypeDescriptor.getFacets()
                    .getObjectContent()
                    .get(this.getName())
                    .getTypeOrReference()
                    .getTypeDescriptor());
    }

    private void checkAgainstTypeDescriptor(TypeDescriptor baseTypeDescriptor) {
        boolean foundMatch;
        if (baseTypeDescriptor.isUnionType()) {
            foundMatch = false;
            for (TypeOrReference typeOrReference : baseTypeDescriptor.getFacets().getUnionContent().getTypes()) {
                if (
                    this.getTypeOrReference().getTypeDescriptor().hasCompatibleType(typeOrReference.getTypeDescriptor())
                ) {
                    this.getTypeOrReference()
                        .getTypeDescriptor()
                        .checkAgainstTypeDescriptor(typeOrReference.getTypeDescriptor());
                    foundMatch = true;
                    break;
                }
            }
            if (!foundMatch)
                throw new LessRestrictiveFacetException(
                        this.getTypeOrReference().getTypeDescriptor().getName()
                            + " is not less restrictive than "
                            + baseTypeDescriptor.getName()
                );
        } else if (this.getTypeOrReference().getTypeDescriptor().hasCompatibleType(baseTypeDescriptor)) {
            this.getTypeOrReference().getTypeDescriptor().checkAgainstTypeDescriptor(baseTypeDescriptor);
        } else
            throw new LessRestrictiveFacetException(
                    this.getTypeOrReference().getTypeDescriptor().getName()
                        + " is not less restrictive than "
                        + baseTypeDescriptor.getName()
            );
    }
}
