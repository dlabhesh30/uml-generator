package codeparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;

public class ClassGrammarGenerator {
	HashMap<String, Boolean> inClassMapper;
    HashMap<String, String> classConnMap;
    ArrayList<CompilationUnit> compUnitArray;
    final String inputPath;
    final String outputPath;
    String grammar;
    ClassGrammarGenerator(HashMap<String, Boolean> inClassMapper, HashMap<String, String> classConnMap, ArrayList<CompilationUnit> compUnitArray,String inputPath,String outputPath){
    	this.inClassMapper=inClassMapper;
    	this.classConnMap=classConnMap;
    	this.compUnitArray=compUnitArray;
    	this.inputPath=inputPath;
    	this.outputPath=outputPath;
    }
    public String parseAdditions() {
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
	    }
    
    public String codeRedundancyRemover(String code) {
        String[] codeLines = code.split(",");
        String[] uniqueCodeLines = new LinkedHashSet<String>(
                Arrays.asList(codeLines)).toArray(new String[0]);
        String result = String.join(",", uniqueCodeLines);
        return result;
    }
}
