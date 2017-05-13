package codeparser;

import java.io.*;
import java.util.*;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;

import net.sourceforge.plantuml.SourceStringReader;

public class SequenceDiagramParser {
    final String inputFunctionName;
    final String inputClassName;	
    final String inputPath;
    final String outputPath;
    String plantUmlGrammar;
    HashMap<String, String> mapFunctionClass;
    ArrayList<CompilationUnit> compUnitArray;
    HashMap<String, ArrayList<MethodCallExpr>> mapFunctionCalls;

    SequenceDiagramParser(String inputPath, String inputClassName, String inputFuncName,
            String outputFile) {
        this.inputPath = inputPath;
        this.outputPath = inputPath + "\\" + outputFile + ".png";
        this.inputClassName = inputClassName;
        this.inputFunctionName = inputFuncName;
        mapFunctionClass = new HashMap<String, String>();
        mapFunctionCalls = new HashMap<String, ArrayList<MethodCallExpr>>();
        plantUmlGrammar = "@startuml\n";
    }

    //calling all methods of this class and generating sequence diagram
    
    public void init() throws Exception {
        compUnitArray = getCompUnitArray(inputPath);
        buildParsedMaps();
        plantUmlGrammar += "actor user #black\n";
        plantUmlGrammar += "user" + " -> " + inputClassName + " : " + inputFunctionName + "\n";
        plantUmlGrammar += "activate " + mapFunctionClass.get(inputFunctionName) + "\n";
        sequenceParser(inputFunctionName);
        plantUmlGrammar += "@enduml";
        generateImage(plantUmlGrammar);
        System.out.println("Plant UML Code:\n" + plantUmlGrammar);
    }

    private void sequenceParser(String callerFunc) {

        for (MethodCallExpr functionCall : mapFunctionCalls.get(callerFunc)) {
            String callerClass = mapFunctionClass.get(callerFunc);
            String calleeFunc = functionCall.getName();
            String calleeClass = mapFunctionClass.get(calleeFunc);
            if (mapFunctionClass.containsKey(calleeFunc)) {
                plantUmlGrammar += callerClass + " -> " + calleeClass + " : "
                        + functionCall.toStringWithoutComments() + "\n";
                plantUmlGrammar += "activate " + calleeClass + "\n";
                sequenceParser(calleeFunc);
                plantUmlGrammar += calleeClass + " -->> " + callerClass + "\n";
                plantUmlGrammar += "deactivate " + calleeClass + "\n";
            }
        }
    }
    private void buildParsedMaps() {
        for (CompilationUnit compUnit : compUnitArray) {
            String className = "";
            List<TypeDeclaration> typeDec = compUnit.getTypes();
            for (Node n : typeDec) {
                ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration) n;
                className = coi.getName();
                for (BodyDeclaration bodyDec : ((TypeDeclaration) coi)
                        .getMembers()) {
                    if (bodyDec instanceof MethodDeclaration) {
                        MethodDeclaration methodDec = (MethodDeclaration) bodyDec;
                        ArrayList<MethodCallExpr> methodCallExp = new ArrayList<MethodCallExpr>();
                        for (Object bs : methodDec.getChildrenNodes()) {
                            if (bs instanceof BlockStmt) {
                                for (Object es : ((Node) bs)
                                        .getChildrenNodes()) {
                                    if (es instanceof ExpressionStmt) {
                                        if (((ExpressionStmt) (es))
                                                .getExpression() instanceof MethodCallExpr) {
                                            methodCallExp.add(
                                                    (MethodCallExpr) (((ExpressionStmt) (es))
                                                            .getExpression()));
                                        }
                                    }
                                }
                            }
                        }
                        mapFunctionCalls.put(methodDec.getName(), methodCallExp);
                        mapFunctionClass.put(methodDec.getName(), className);
                    }
                }
            }
        }
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

   
  
    @SuppressWarnings("unused")
    private void printFinalClassMaps() {
        System.out.println("mapMethodCalls:");
        Set<String> keys = mapFunctionCalls.keySet(); // get all keys
        for (String i : keys) {
            System.out.println(i + "->" + mapFunctionCalls.get(i));
        }
        System.out.println("---");
        keys = null;

        System.out.println("mapMethodClass:");
        keys = mapFunctionClass.keySet(); // get all keys
        for (String i : keys) {
            System.out.println(i + "->" + mapFunctionClass.get(i));
        }
        System.out.println("---");
    }
    
    private String generateImage(String source) throws IOException {

        OutputStream png = new FileOutputStream(outputPath);
        SourceStringReader reader = new SourceStringReader(source);
        String desc = reader.generateImage(png);
        return desc;

    }

}
