package codeparser;

import java.io.*;
import java.util.*;
import java.lang.*;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class ClassDiagramParser {    
    HashMap<String, Boolean> inClassMapper;
    HashMap<String, String> classConnMap;
    ArrayList<CompilationUnit> compUnitArray;
    final String inputPath;
    final String outputPath;
    String grammar;
    

    ClassDiagramParser(String inputPath, String outputFile) {
        this.inputPath = inputPath;
        this.outputPath = inputPath + "\\" + outputFile + ".png";
        inClassMapper = new HashMap<String, Boolean>();
        classConnMap = new HashMap<String, String>();
        grammar = "";
    }
    
    //calling all functions of this class and generating png.   
    public void init() throws Exception {
        compUnitArray = getCompUnitArray(inputPath);
        buildClassOrInterfaceMap(compUnitArray);
        for (CompilationUnit cu : compUnitArray)
            grammar += parseFile(cu);
        ClassGrammarGenerator obj=new ClassGrammarGenerator(inClassMapper,classConnMap,compUnitArray,inputPath,outputPath);
        grammar += obj.parseAdditions();
        grammar = obj.codeRedundancyRemover(grammar);
        System.out.println("Unique Code: " + grammar);
        GenerateUMLDiagram.generateImage(grammar, outputPath);
    }

   

   /* private String parseAdditions() {
        String result = "";
        Set<String> keys = classConnMap.keySet(); // get all keys
        for (String i : keys) {
            String[] classes = i.split("-");
            if (inClassMapper.get(classes[0]))
                result += "[<<interface>>;" + classes[0] + "]";
            else
                result += "[" + classes[0] + "]";
            result += classConnMap.get(i); // Add connection
            if (inClassMapper.get(classes[1]))
                result += "[<<interface>>;" + classes[1] + "]";
            else
                result += "[" + classes[1] + "]";
            result += ",";
        }
        return result;
    }*/

    private String parseFile(CompilationUnit cu) {
    	String className = "";
        String shortClassName = "";
    	String finalGrammar = "";        
        String functions = "";
        String variables = "";
        String fillers = ",";
        ArrayList<String> publicFields = new ArrayList<String>();
        
        List<TypeDeclaration> ltd = cu.getTypes();
        //no nesting of classes will get us the first class
        Node node = ltd.get(0);

        // Get class or interface name
        ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration) node;
        if (coi.isInterface()) {
            className = "[" + "<<interface>>;";
        } else {
            className = "[";
        }
        className += coi.getName();
        shortClassName = coi.getName();

        // Parsing Methods
        boolean moreParameters = false;
        for (BodyDeclaration bd : ((TypeDeclaration) node).getMembers()) {
            // Get Methods
            if (bd instanceof ConstructorDeclaration) {
                ConstructorDeclaration cd = ((ConstructorDeclaration) bd);
                if (cd.getDeclarationAsString().startsWith("public")
                        && !coi.isInterface()) {
                    if (moreParameters)
                        functions += ";";
                    functions += "+ " + cd.getName() + "(";
                    for (Object gcn : cd.getChildrenNodes()) {
                        if (gcn instanceof Parameter) {
                            Parameter parameterCast = (Parameter) gcn;
                            String parameterClass = parameterCast.getType().toString();
                            String parameterName = parameterCast.getChildrenNodes()
                                    .get(0).toString();
                            functions += parameterName + " : " + parameterClass;
                            if (inClassMapper.containsKey(parameterClass)
                                    && !inClassMapper.get(shortClassName)) {
                                fillers += "[" + shortClassName
                                        + "] uses -.->";
                                if (inClassMapper.get(parameterClass))
                                    fillers += "[<<interface>>;" + parameterClass
                                            + "]";
                                else
                                    fillers += "[" + parameterClass + "]";
                            }
                            fillers += ",";
                        }
                    }
                    functions += ")";
                    moreParameters = true;
                }
            }
        }
        for (BodyDeclaration bodyDec : ((TypeDeclaration) node).getMembers()) {
            if (bodyDec instanceof MethodDeclaration) {
                MethodDeclaration methodDec = ((MethodDeclaration) bodyDec);
                // Get only public methods
                if (methodDec.getDeclarationAsString().startsWith("public")
                        && !coi.isInterface()) {
                    // Identify Setters and Getters
                    if (methodDec.getName().startsWith("set")
                            || methodDec.getName().startsWith("get")) {
                        String varName = methodDec.getName().substring(3);
                        publicFields.add(varName.toLowerCase());
                    } else {
                        if (moreParameters)
                            functions += ";";
                        functions += "+ " + methodDec.getName() + "(";
                        for (Object gcn : methodDec.getChildrenNodes()) {
                            if (gcn instanceof Parameter) {
                                Parameter parameterCast = (Parameter) gcn;
                                String parameterClass = parameterCast.getType()
                                        .toString();
                                String parameterName = parameterCast.getChildrenNodes()
                                        .get(0).toString();
                                functions += parameterName + " : " + parameterClass;
                                if (inClassMapper.containsKey(parameterClass)
                                        && !inClassMapper.get(shortClassName)) {
                                    fillers += "[" + shortClassName
                                            + "] uses -.->";
                                    if (inClassMapper.get(parameterClass))
                                        fillers += "[<<interface>>;"
                                                + parameterClass + "]";
                                    else
                                        fillers += "[" + parameterClass + "]";
                                }
                                fillers += ",";
                            } else {
                                String functionBody[] = gcn.toString().split(" ");
                                for (String obj : functionBody) {
                                    if (inClassMapper.containsKey(obj)
                                            && !inClassMapper.get(shortClassName)) {
                                        fillers += "[" + shortClassName
                                                + "] uses -.->";
                                        if (inClassMapper.get(obj))
                                            fillers += "[<<interface>>;" + obj
                                                    + "]";
                                        else
                                            fillers += "[" + obj + "]";
                                        fillers += ",";
                                    }
                                }
                            }
                        }
                        functions += ") : " + methodDec.getType();
                        moreParameters = true;
                    }
                }
            }
        }
        // Parsing Variables
        boolean moreVariables = false;
        for (BodyDeclaration bodyDec : ((TypeDeclaration) node).getMembers()) {
            if (bodyDec instanceof FieldDeclaration) {
                FieldDeclaration variableDec = ((FieldDeclaration) bodyDec);
                String varScope = getScope(
                        bodyDec.toStringWithoutComments().substring(0,
                                bodyDec.toStringWithoutComments().indexOf(" ")));
                String varClass = replaceBrackets(variableDec.getType().toString());
                String varName = variableDec.getChildrenNodes().get(1).toString();
                if (varName.contains("="))
                    varName = variableDec.getChildrenNodes().get(1).toString()
                            .substring(0, variableDec.getChildrenNodes().get(1)
                                    .toString().indexOf("=") - 1);
                // Change scope of getter, setters
                if (varScope.equals("-")
                        && publicFields.contains(varName.toLowerCase())) {
                    varScope = "+";
                }
                String getFunctionParams = "";
                boolean getMultipleFunctionParams = false;
                if (varClass.contains("(")) {
                    getFunctionParams = varClass.substring(varClass.indexOf("(") + 1,
                            varClass.indexOf(")"));                    
                    getMultipleFunctionParams = true;
                } else if (inClassMapper.containsKey(varClass)) {
                    getFunctionParams = varClass;
                }
                if (getFunctionParams.length() > 0 && inClassMapper.containsKey(getFunctionParams)) {
                    String connection = "-";

                    if (classConnMap
                            .containsKey(getFunctionParams + "-" + shortClassName)) {
                        connection = classConnMap
                                .get(getFunctionParams + "-" + shortClassName);
                        if (getMultipleFunctionParams)
                            connection = "*" + connection;
                        classConnMap.put(getFunctionParams + "-" + shortClassName,
                                connection);
                    } else {
                        if (getMultipleFunctionParams)
                            connection += "*";
                        classConnMap.put(shortClassName + "-" + getFunctionParams,
                                connection);
                    }
                }
                if (varScope == "+" || varScope == "-") {
                    if (moreVariables)
                        variables += "; ";
                    variables += varScope + " " + varName + " : " + varClass;
                    moreVariables = true;
                }
            }

        }
       
        
        // Check extends / implements condition
        
        if (coi.getExtends() != null) {
            fillers += "[" + shortClassName + "] " + "-^ " + coi.getExtends();
            fillers += ",";
        }
        
        if (coi.getImplements() != null) {
            List<ClassOrInterfaceType> interfaceList = (List<ClassOrInterfaceType>) coi
                    .getImplements();
            for (ClassOrInterfaceType intface : interfaceList) {
                fillers += "[" + shortClassName + "] " + "-.-^ " + "["
                        + "<<interface>>;" + intface + "]";
                fillers += ",";
            }
        }
        // Combine className, methods and fields
        finalGrammar += className;
        if (!variables.isEmpty()) {
            finalGrammar += "|" + replaceBrackets(variables);
        }
        if (!functions.isEmpty()) {
            finalGrammar += "|" + replaceBrackets(functions);
        }
        finalGrammar += "]";
        finalGrammar += fillers;
        return finalGrammar;
    }

    private String getScope(String stringScope) {
        switch (stringScope) {
        case "private":
            return "-";
        case "public":
            return "+";
        default:
            return "";
        }
    }
    
    private String replaceBrackets(String obj) {
        obj = obj.replace("[", "(");
        obj = obj.replace("]", ")");
        obj = obj.replace("<", "(");
        obj = obj.replace(">", ")");
        return obj;
    }

    private void buildClassOrInterfaceMap(ArrayList<CompilationUnit> cuArray) {
        for (CompilationUnit cu : cuArray) {
            List<TypeDeclaration> cl = cu.getTypes();
            for (Node n : cl) {
                ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration) n;
                inClassMapper.put(coi.getName(), coi.isInterface()); // false is class,
                                                           // true is interface
            }
        }
    }

    @SuppressWarnings("unused")
    private void printFinalClassMaps() {
        System.out.println("Map:");
        Set<String> keys = classConnMap.keySet(); // get all keys
        for (String i : keys) {
            System.out.println(i + "->" + classConnMap.get(i));
        }
        System.out.println("---");
    }

    private ArrayList<CompilationUnit> getCompUnitArray(String inPath)
            throws Exception {
        File folder = new File(inPath);
        ArrayList<CompilationUnit> cuArray = new ArrayList<CompilationUnit>();
        for (final File f : folder.listFiles()) {
            if (f.isFile() && f.getName().endsWith(".java")) {
                FileInputStream in = new FileInputStream(f);
                CompilationUnit cu;
                try {
                    cu = JavaParser.parse(in);
                    cuArray.add(cu);
                } finally {
                    in.close();
                }
            }
        }
        return cuArray;
    }
    
    /*private String codeRedundancyRemover(String code) {
        String[] codeLines = code.split(",");
        String[] uniqueCodeLines = new LinkedHashSet<String>(
                Arrays.asList(codeLines)).toArray(new String[0]);
        String result = String.join(",", uniqueCodeLines);
        return result;
    }*/

}
