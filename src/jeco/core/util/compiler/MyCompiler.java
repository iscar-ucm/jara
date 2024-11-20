/*
 * Copyright (C) 2010 José Luis Risco Martín <jlrisco@ucm.es>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *  - José Luis Risco Martín
 *  - J. M. Colmenar
 */
package jeco.core.util.compiler;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * Compiles source and also makes sure that reloading a compiled class
 * does not "caches" the first compiled class.
 */
public class MyCompiler {

    /**
     * Console output of the compiler
     */
    protected StringBuffer console;
    /**
     * Working directory
     */
    protected String workDir;
    /**
     * Class path separator
     */
    protected String classPathSeparator;

    /**
     * Constructor
     *
     * @param workDir Working directory
     * @param classPathSeparator Class path separator
     */
    public MyCompiler(String workDir, String classPathSeparator) {
        console = new StringBuffer();
        this.workDir = workDir;
        this.classPathSeparator = classPathSeparator;
    }

    /**
     * Compiles a collection of files
     *
     * @param filePaths Collection of file paths
     * @return True if the compilation was successful
     * @throws Exception
     */
    public boolean compile(Collection<String> filePaths) throws Exception {
        console.delete(0, console.length());
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(filePaths);
        String jars = ".";
        File dir = new File(workDir);
        String[] children = dir.list();
        for (String childrenI : children) {
            if (childrenI.indexOf(".jar") >= 0) {
                File file = new File(workDir + File.separator + childrenI);
                jars += classPathSeparator + file.getAbsolutePath();
            }
        }
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, Arrays.asList("-classpath", jars, "-d", workDir), null, compilationUnits);
        boolean success = task.call();
        for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
            console.append("Code: ");
            console.append(diagnostic.getCode());
            console.append("\n");
            console.append("Kind: ");
            console.append(diagnostic.getKind());
            console.append("\n");
            console.append("Position: ");
            console.append(diagnostic.getPosition());
            console.append("\n");
            console.append("Start Position: ");
            console.append(diagnostic.getStartPosition());
            console.append("\n");
            console.append("End Position: ");
            console.append(diagnostic.getEndPosition());
            console.append("\n");
            console.append("Source: ");
            console.append(diagnostic.getSource());
            console.append("\n");
            console.append("Message: ");
            console.append(diagnostic.getMessage(null));
            console.append("\n");
            console.append("Success: ").append(success).append("\n");
        }
        fileManager.close();
        return success;
    }

    /**
     * Gets the console output
     * @return Console output
     */
    public String getOutput() {
        return console.toString();
    }
    
    /**
     * Gets the working directory
     * @return Working directory
     */
    public String getWorkDir() {
        return workDir;
    }
    
    /**
     * Gets the class path separator
     * @return Class path separator
     */
    public String getClassPathSeparator() {
        return classPathSeparator;
    }
}
