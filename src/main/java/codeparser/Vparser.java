package codeparser;

public class Vparser {
	 final String inPath;
	    final String outPath;
	    HashMap<String, Boolean> map;
	    HashMap<String, String> mapClassConn;
	    String yumlCode;
	    ArrayList<CompilationUnit> cuArray;

	    ParseEngine(String inPath, String outFile) {
	        this.inPath = inPath;
	        this.outPath = inPath + "\\" + outFile + ".png";
	        map = new HashMap<String, Boolean>();
	        mapClassConn = new HashMap<String, String>();
	        yumlCode = "";
	    }

	    public void start() throws Exception {
	        cuArray = getCuArray(inPath);
	        buildMap(cuArray);
	        for (CompilationUnit cu : cuArray)
	            yumlCode += parser(cu);
	        yumlCode += parseAdditions();
	        yumlCode = yumlCodeUniquer(yumlCode);
	        System.out.println("Unique Code: " + yumlCode);
	        GenerateDiagram.generatePNG(yumlCode, outPath);
	    }

	    private String yumlCodeUniquer(String code) {
	        String[] codeLines = code.split(",");
	        String[] uniqueCodeLines = new LinkedHashSet<String>(
	                Arrays.asList(codeLines)).toArray(new String[0]);
	        String result = String.join(",", uniqueCodeLines);
	        return result;
	    }

	    private String parseAdditions() {
	        String result = "";
	        Set<String> keys = mapClassConn.keySet(); // get all keys
	        for (String i : keys) {
	            String[] classes = i.split("-");
	            if (map.get(classes[0]))
	                result += "[<<interface>>;" + classes[0] + "]";
	            else
	                result += "[" + classes[0] + "]";
	            result += mapClassConn.get(i); // Add connection
	            if (map.get(classes[1]))
	                result += "[<<interface>>;" + classes[1] + "]";
	            else
	               
	        }
	        return result;
	    }

	    private String parser(CompilationUnit cu) {
	        String result = "";
	        String className = "";
	        String classShortName = "";
	        String methods = "";
	        String fields = "";
	        String additions = ",";

	        ArrayList<String> makeFieldPublic = new ArrayList<String>();
	        List<TypeDeclaration> ltd = cu.getTypes();
	        Node node = ltd.get(0); // assuming no nested classes

	        // Get className
	        ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration) node;
	        if (coi.isInterface()) {
	            className = "[" + "<<interface>>;";
	        } else {
	            className = "[";
	        }
	        className += coi.getName();
	        classShortName = coi.getName();

	        // Parsing Methods
	        boolean nextParam = false;
	        for (BodyDeclaration bd : ((TypeDeclaration) node).getMembers()) {
	            // Get Methods
	            if (bd instanceof ConstructorDeclaration) {
	                ConstructorDeclaration cd = ((ConstructorDeclaration) bd);
	                if (cd.getDeclarationAsString().startsWith("public")
	                        && !coi.isInterface()) {
	                    if (nextParam)
	                        methods += ";";
	                    methods += "+ " + cd.getName() + "(";
	                    for (Object gcn : cd.getChildrenNodes()) {
	                        if (gcn instanceof Parameter) {
	                            Parameter paramCast = (Parameter) gcn;
	                            String paramClass = paramCast.getType().toString();
	                            String paramName = paramCast.getChildrenNodes()
	                                    .get(0).toString();
	                            methods += paramName + " : " + paramClass;
	                            if (map.containsKey(paramClass)
	                                    && !map.get(classShortName)) {
	                                additions += "[" + classShortName
	                                        + "] uses -.->";
	                                if (map.get(paramClass))
	                                    additions += "[<<interface>>;" + paramClass
	                                            + "]";
	                                else
	                                    additions += "[" + paramClass + "]";
	                            }
	                            additions += ",";
	                        }
	                    }
	                    methods += ")";
	                    nextParam = true;
	                }
	            }
	        }
}
