package org.jsound.type;

import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.RequiredSertBackToFalseException;
import org.jsound.item.Item;

public class FieldDescriptor {
    public String name;
    private TypeOrReference type;
    private Boolean required = false;
    private Item defaultValue;
    private Boolean unique = false;
    public boolean requiredIsChecked = false;
    private boolean requiredIsSet = false;

    public void setName(String name) {
        this.name = name;
    }

    public void setType(TypeOrReference type) {
        this.type = type;
    }

    public void setRequired(Boolean required) {
        this.required = required;
        requiredIsSet = true;
    }

    public void setDefaultValue(Item defaultValue) {
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

    public Item getDefaultValue() {
        return defaultValue;
    }

    public void isMoreRestrictive(ObjectTypeDescriptor baseTypeDescriptor) {
        this.getTypeOrReference().getTypeDescriptor().checkBaseType(
        );
        if (!this.isRequired() && this.requiredIsSet)
            checkRequiredField(baseTypeDescriptor);
    }

    private void checkRequiredField(ObjectTypeDescriptor baseTypeDescriptor) {
        if (!this.requiredIsChecked) {
            FieldDescriptor fieldDescriptor = baseTypeDescriptor.getFacets().getObjectContent().getOrDefault(this.getName(), null);
            if (fieldDescriptor == null)
                return;
            if (fieldDescriptor.isRequired())
                throw new RequiredSertBackToFalseException(
                    "Field "
                            + this.getName()
                            + " cannot be set back to false. It is set to true in baseType "
                            + baseTypeDescriptor.getName()
                            + "."
                );
            if (baseTypeDescriptor.baseType != null)
                fieldDescriptor.checkRequiredField((ObjectTypeDescriptor) baseTypeDescriptor.baseType.getTypeDescriptor());
        }
        this.requiredIsChecked = true;
    }

    public void validateDefaultValue() {
        if (this.getDefaultValue() != null) {
            if (!this.getTypeOrReference()
                    .getTypeDescriptor()
                    .validate(this.getDefaultValue(), false))
                throw new InvalidSchemaException("The default value for field " + this.getName() + " is not valid against its type.");
        }
    }
}
