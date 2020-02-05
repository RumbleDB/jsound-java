# JSound 2.0 in Java

Implementation of a JSound 2.0 validation and annotation engine in Java.

### Getting started

- #### Build:
    
    `mvn clean compile assembly:single`

- #### Run:

    You can validate and annotate a JSON instance against a JSound 2.0 schema by simply executing commands with the corresponding keywords:
    - `validate`: boolean, optional. Indicates whether you want to validate the candidate instance file against the schema.
    - `annotate`: boolean, optional. Indicates whether you want to annotate the candidate instance file with the types defined the schema. Annotation is preceded by the validation procedure, with no need to explicitly specifying it.
        
        In order to perform these operations, some shell arguments have to be provided in the format `--argName argValue`. Following are the allowed arguments:
    - `schema`: string, required. The path to the schema file.
    - `file`: string, required. The path to the instance file.
    - `targetType`: string, required. The type name in the schema file you want to validate the instance file against.
    - `compact`: boolean, required. Indicates whether the [compact](http://www.jsound-spec.org/publish/en-US/JSound-C/2.0/html-single/JSound-C/index.html) or [extended](http://www.jsound-spec.org/publish/en-US/JSound/2.0/html-single/JSound/index.html) JSound 2.0 syntax is used to define the schema.
    - `output`: string, required if annotating. The path to the file where the annotated instance file will be written to. 
    The file does not need to exist beforehand; if it does, its content will be overwritten by the annotated instance. 
    
    ##### Examples:
    
    Validation:
    
        java -jar target/JSound-1.0-jar-with-dependencies.jar validate --schema src/main/resources/compactSchemas/object/objectSchema.json --file src/main/resources/instanceFiles/object/objectFile.json --targetType rootType --compact true
    
    Annotation:
        
        java -jar target/JSound-1.0-jar-with-dependencies.jar annotate --schema src/main/resources/extendedSchemas/generalSchema.json --file src/main/resources/instanceFiles/generalFile.json --targetType rootType --compact false --output annotatedFile.json

- #### Test

    `mvn test`
