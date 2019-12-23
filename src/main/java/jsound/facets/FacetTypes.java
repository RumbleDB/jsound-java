package jsound.facets;

public enum FacetTypes {
    LENGTH("length"),
    MIN_LENGTH("minLength"),
    MAX_LENGTH("maxLength"),
    MIN_INCLUSIVE("minInclusive"),
    MAX_INCLUSIVE("maxInclusive"),
    MIN_EXCLUSIVE("minExclusive"),
    MAX_EXCLUSIVE("maxExclusive"),
    TOTAL_DIGITS("totalDigits"),
    FRACTION_DIGITS("fractionDigits"),
    EXPLICIT_TIMEZONE("explicitTimezone"),

    CONTENT("content"),
    CLOSED("closed"),

    ENUMERATION("enumeration"),
    METADATA("metadata"),
    CONSTRAINTS("constraints");

    private String facetName;

    FacetTypes(String facetName) {
        this.facetName = facetName;
    }

    public String getTypeName() {
        return facetName;
    }
}
