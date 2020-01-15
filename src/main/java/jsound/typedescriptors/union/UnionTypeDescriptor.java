package jsound.typedescriptors.union;

import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.LessRestrictiveFacetException;
import jsound.facets.FacetTypes;
import jsound.facets.UnionFacets;
import jsound.typedescriptors.TypeOrReference;
import jsound.types.ItemTypes;
import jsound.tyson.TysonItem;
import org.api.Item;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static jsound.facets.FacetTypes.CONTENT;

public class UnionTypeDescriptor extends TypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Collections.singletonList(CONTENT));
    private final UnionFacets facets;
    private Map<Item, TypeDescriptor> validatingTypes = new HashMap<>();

    public UnionTypeDescriptor(String name, UnionFacets facets) {
        super(ItemTypes.VALUE, name);
        this.facets = facets;
        if (facets.getUnionContent() == null)
            throw new InvalidSchemaException(
                    "Union type " + this.getName() + " must have the \"content\" facet defined."
            );
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isUnionType() {
        return true;
    }

    @Override
    public boolean validate(ItemWrapper itemWrapper, boolean isEnumValue) {
        for (FacetTypes facetType : this.getFacets().getDefinedFacets()) {
            switch (facetType) {
                case CONTENT:
                    if (!validateContentFacet(itemWrapper))
                        return false;
                    break;
                case ENUMERATION:
                    if (!isEnumValue)
                        this.validateContentFacet(itemWrapper);
                    if (!validateEnumeration(itemWrapper.getItem(), isEnumValue))
                        return false;
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    private boolean validateContentFacet(ItemWrapper itemWrapper) {
        for (TypeOrReference typeOrReference : this.getFacets().getUnionContent().getTypes()) {
            if (typeOrReference.getTypeDescriptor().validate(itemWrapper, false)) {
                validatingTypes.put(itemWrapper.getItem(), typeOrReference.getTypeDescriptor());
                return true;
            }
        }
        return false;
    }

    @Override
    public TysonItem annotate(ItemWrapper itemWrapper) {
        return validatingTypes.get(itemWrapper.getItem()).annotate(itemWrapper);
    }

    @Override
    public UnionFacets getFacets() {
        return facets;
    }

    @Override
    public boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isUnionType();
    }

    @Override
    public void checkAgainstTypeDescriptor(TypeDescriptor typeDescriptor) {
        boolean foundMatch;
        for (TypeOrReference typeOrReference : this.getFacets().getUnionContent().getTypes()) {
            if (typeDescriptor.isUnionType()) {
                foundMatch = false;
                for (TypeOrReference typeOrReference1 : typeDescriptor.getFacets().getUnionContent().getTypes()) {
                    if (typeOrReference.getTypeDescriptor().hasCompatibleType(typeOrReference1.getTypeDescriptor())) {
                        typeOrReference.getTypeDescriptor()
                            .checkAgainstTypeDescriptor(typeOrReference1.getTypeDescriptor());
                        foundMatch = true;
                        break;
                    }
                }
                if (!foundMatch)
                    throw new LessRestrictiveFacetException(
                            typeOrReference.getTypeDescriptor().getName()
                                + " is not less restrictive than "
                                + typeDescriptor.getName()
                    );
            } else if (typeOrReference.getTypeDescriptor().hasCompatibleType(typeDescriptor)) {
                typeOrReference.getTypeDescriptor().checkAgainstTypeDescriptor(typeDescriptor);
            } else
                throw new LessRestrictiveFacetException(
                        typeOrReference.getTypeDescriptor().getName()
                            + " is not less restrictive than "
                            + typeDescriptor.getName()
                );
        }
    }
}
