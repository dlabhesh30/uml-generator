package codeparser;

public class bparser {
	for (BodyDeclaration bd : ((TypeDeclaration) node).getMembers()) {
        if (bd instanceof MethodDeclaration) {
            MethodDeclaration md = ((MethodDeclaration) bd);
            // Get only public methods
          
                        methods += ";";
                    methods += "+ " + md.getName() + "(";
                    for (Object gcn : md.getChildrenNodes()) {
                        if (gcn instanceof Parameter) {
                            Parameter paramCast = (Parameter) gcn;
                            String paramClass = paramCast.getType()
                                    .toString();
                            String paramName = paramCast.getChildrenNodes()
                                    .get(0).toString();
                            methods += paramName + " : " + paramClass;
                            if (map.containsKey(paramClass)
                                    && !map.get(classShortName)) {
                                additions += "[" + classShortName
                                        + "] uses -.->";
                                if (map.get(paramClass))
                                    additions += "[<<interface>>;"
                                            + paramClass + "]";
                                else
                                    additions += "[" + paramClass + "]";
                            }
                            additions += ",";
                        } else {
                            String methodBody[] = gcn.toString().split(" ");
                            for (String foo : methodBody) {
                                if (map.containsKey(foo)
                                        && !map.get(classShortName)) {
                                    additions += "[" + classShortName
                                            + "] uses -.->";
                                    if (map.get(foo))
                                        additions += "[<<interface>>;" + foo
                                                + "]";
                                    else
                                        additions += "[" + foo + "]";
                                    additions += ",";
                                }
                            }
                        }
                    }
                    methods += ") : " + md.getType();
                    nextParam = true;
                }
            }
        }
    }
}
