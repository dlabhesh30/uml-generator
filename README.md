This UML-parser is a software designed to convert any java program given to it into a UML class diagram.

The tools used for this program include:
    1.	Java uml-parser - Java parser is used to tokenize the given input program and produe grammar that can be understood by plant uml.
    2.	yuml - yuml will take the input grammar and draw a corresponding class diagram by calling the API of yuml.
    3.	Plant-uml - Plant-uml will take the input grammar generated by the java-parser module and will draw the corresponding sequence             diagram.

The programming language used for this project is Java.
To execute use the following commands :
      1. For Class diagram :
         To generate a class diagram, we have to provide the path of the jar file, the folder where the java files exist and the name for          the new image to be generated. This program generates a class diagram in the input java files folder.   
      
         java -jar uml-parser.jar class "C:\Path to the folder where files are located" image_name
      
         This will generate the class diagram in the input folder where your java files are present.
      
      2. For Sequence diagram: 
         This program generates a sequence diagram from the given input java class file. Unlike the class diagram             generation            the program expects two additional parameters namely, the class name and the function name inside the given class                          name from where the sequence diagram should start.
          To generate a sequence diagram use the following command,
          
          java -jar uml-parser.jar sequence "C:\Path to the folder where files are located" class_name function_name image_name
          
          This will generate a sequence diagram starting from the given "class_name" and the start "function_name" in the given input               folder.
