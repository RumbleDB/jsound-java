package org.api.executors;


import org.api.TypeDescriptor;

import java.io.IOException;

public abstract class JSoundValidateExecutor extends JSoundExecutor {

    public static boolean validate(String schemaPath, String filePath, String rootType, boolean compact) throws IOException {
        initializeApplication(schemaPath, filePath, rootType, compact);
        checkSubtypeCorrectness();
        return schemaItem.validate(fileItem, false);
    }

    private static void checkSubtypeCorrectness() {
        for (TypeDescriptor typeDescriptor : schema.values()) {
            typeDescriptor.resolveAllFacets();
            typeDescriptor.checkBaseType();
        }
    }
}
