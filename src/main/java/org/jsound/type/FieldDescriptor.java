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

    public void isMoreRestrictive(ObjectTypeDescriptor typeDescriptor) {
        this.getTypeOrReference().getTypeDescriptor().isSubtypeOf(
                typeDescriptor.getFacets().getObjectContent().get(this.getName()).getTypeOrReference().getTypeDescriptor());
        if (!this.isRequired() && this.requiredIsSet)
            checkRequiredField(typeDescriptor);
    }

    private void checkRequiredField(ObjectTypeDescriptor typeDescriptor) {
        if (!this.requiredIsChecked) {
            FieldDescriptor fieldDescriptor = typeDescriptor.getFacets().getObjectContent().getOrDefault(this.getName(), null);
            if (fieldDescriptor == null)
                return;
            if (fieldDescriptor.isRequired())
                throw new RequiredSertBackToFalseException(
                    "Field "
                            + this.getName()
                            + " cannot be set back to false. It is set to true in baseType "
                            + typeDescriptor.getName()
                            + "."
                );
            if (typeDescriptor.baseType != null)
                fieldDescriptor.checkRequiredField((ObjectTypeDescriptor) typeDescriptor.baseType.getTypeDescriptor());
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
