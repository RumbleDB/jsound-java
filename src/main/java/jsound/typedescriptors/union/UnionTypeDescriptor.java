package jsound.typedescriptors.union;

import jsound.exceptions.InvalidSchemaException;
import jsound.facets.FacetTypes;
import jsound.facets.UnionFacets;
import jsound.typedescriptors.TypeOrReference;
import jsound.types.ItemTypes;
import jsound.tyson.TysonItem;
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
    private Map<ItemWrapper, TypeDescriptor> validatingTypes = new HashMap<>();

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
                validatingTypes.put(itemWrapper, typeOrReference.getTypeDescriptor());
                return true;
            }
        }
        return false;
    }

    @Override
    public TysonItem annotate(ItemWrapper itemWrapper) {
        return validatingTypes.get(itemWrapper).annotate(itemWrapper);
    }

    @Override
    public UnionFacets getFacets() {
        return facets;
    }

    @Override
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isUnionType();
    }
}
