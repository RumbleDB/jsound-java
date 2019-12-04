package org.jsound.cli;


public abstract class JSoundValidateExecutor extends JSoundExecutor {

    static void validate(String schemaPath, String filePath, String rootType) {
        initializeApplication(schemaPath, filePath, rootType);
        System.out.println(
            fileItem.isValidAgainst(schemaItem)
                ? "Validation completed successfully! ✅"
                : "Validation failed ❌ : the file is not valid against the schema."
        );
    }
}
