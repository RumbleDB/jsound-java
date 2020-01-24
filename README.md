# JSound 2.0 in Java

Implementation of a JSound 2.0 validation and annotation engine in Java.

###Getting started

- ####Build:
    
    `mvn clean compile assembly:single`

- ####Run:

    You can validate and annotate a JSON instance against a JSound 2.0 schema by simply executing this tool providing a few 
    shell arguments in the format `--argName argValue`. Following are the allowed arguments:
    
    - `validate`: boolean, optional. Indicats whether you want to validate the instance file against the schema.
    - `annotate`: boolean, optional. Indicats whether you want to validate the instance file against the schema. 
        
        At least one between the `validate` and `annotate` parameters should be `true`. In order
    to annotate an instance file with a JSound 2.0 schema, you do not need to set the `validate` parameter to be `true`.
    The annotation procedure will check that the instance file is valid against the schema beforehand, nonetheless.
    - `schema`: string, required. The path to the schema file.
    - `file`: string, required. The path to the instance file.
    - `root`: string, required. The type name in the schema file you want to validate the instance file against.
    - `compact`: boolean, required. Indicates whether the [compact](http://www.jsound-spec.org/publish/en-US/JSound-C/2.0/html-single/JSound-C/index.html) or [extended](http://www.jsound-spec.org/publish/en-US/JSound/2.0/html-single/JSound/index.html) JSound 2.0 syntax is used to define the schema.
    - `output`: string, required if annotating. The path to the file where the annotated instance file will be written to. 
    The file does not need to exist beforehand; if it does, its content will be overwritten by the annotated instance. 
    
    #####Examples:
    Validation:
    
        java -jar target/JSound-1.0-jar-with-dependencies.jar --validate true --schema src/main/resources/compactSchemas/object/objectSchema.json --file src/main/resources/instanceFiles/object/objectFile.json --root rootType --compact true
    
    Annotation:
        
        java -jar target/JSound-1.0-jar-with-dependencies.jar --annotate true --schema src/main/resources/extendedSchemas/generalSchema.json --file src/main/resources/instanceFiles/generalFile.json --root rootType --compact false --output annotatedFile.json

- ####Test

    `mvn test`
