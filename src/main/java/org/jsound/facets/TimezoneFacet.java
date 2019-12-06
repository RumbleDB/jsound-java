package org.jsound.facets;

public enum TimezoneFacet {
    REQUIRED("required"), PROHIBITED("prohibited"), OPTIONAL("optional");

    private String typeName;

    TimezoneFacet(String typeName) {
        this.typeName = typeName;
    }
}
