package codeparser;

public class bparser {
	for (BodyDeclaration bd : ((TypeDeclaration) node).getMembers()) {
        if (bd instanceof MethodDeclaration) {
            MethodDeclaration md = ((MethodDeclaration) bd);
            // Get only public methods
          toString();
                            String paramName = paramCast.getChildrenNodes()
                                    .get(0).toString();
                            methods += paramName + " : " + paramClass;
                            if (map.containsKey(paramClass)
                                    && !map.get(classShortName)) {
                                additions += "[" + classShortName
            y[] = gcn.toString().split(" ");
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
