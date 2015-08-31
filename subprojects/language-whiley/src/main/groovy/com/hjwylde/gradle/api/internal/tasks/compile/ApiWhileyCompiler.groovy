package com.hjwylde.gradle.api.internal.tasks.compile

import org.gradle.api.GradleScriptException
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.tasks.compile.Compiler
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.WorkResult

/**
 * An implementation of a whiley compiler that executes the process using the WDK dependency
 * provided.
 *
 * @author Henry J. Wylde
 * @since 0.1.0
 */
class ApiWhileyCompiler implements Compiler<WhileyCompileSpec> {

    private static final Logger logger = Logging.getLogger(ApiWhileyCompiler)

    protected final Project project

    /**
     * Creates a new compiler for the given project.
     */
    ApiWhileyCompiler(Project project) {
        assert project, 'project cannot be null'

        this.project = project
    }

    /**
     * {@inheritDoc}
     */
    @Override
    WorkResult execute(WhileyCompileSpec spec) {
        def cl = createClassLoader(spec.whileyClasspath)

        try {
            // Load the classes from the whiley classpath
            def wyjcMainClass = cl.loadClass('wyjc.WyjcMain')
            def wyjcBuildTaskClass = cl.loadClass('wyjc.util.WyjcBuildTask')
            def optArgClass = Class.forName('[Lwycc.util.OptArg;', false, cl)

            def args = new WhileyCompilerArgumentsBuilder(spec).build()

            execute(wyjcMainClass, wyjcBuildTaskClass, optArgClass, args)

            true as WorkResult
        } catch (ClassNotFoundException e) {
            throw new InvalidUserDataException("class not found '${e.getMessage()}', please " +
                    'ensure the whileyClasspath is set up correctly; the whileyClasspath is ' +
                    'implicitely implied from the compile classpath, make sure a whiley-all ' +
                    'artifact is included in the compile classpath')
        } catch (NoClassDefFoundError e) {
            throw new InvalidUserDataException("class not found '${e.getMessage()}', please " +
                    'ensure the whileyClasspath is set up correctly; the whileyClasspath is ' +
                    'implicitely implied from the compile classpath, make sure a whiley-all ' +
                    'artifact is included in the compile classpath')
        }
    }

    protected void execute(Class wyjcMainClass, Class wyjcBuildTaskClass, Class optArgClass,
            List<String> args) {
        try {
            // Create the constructor parameters for a WyjcMain instance
            def builder = wyjcBuildTaskClass.newInstance()
            def options = wyjcMainClass.getField('DEFAULT_OPTIONS').get(null)

            // Get the constructor by parameter types
            def wyjcMainConstructor = wyjcMainClass.getConstructor(wyjcBuildTaskClass, optArgClass)

            // Create a new instance of a WyjcMain
            def wyjcMain = wyjcMainConstructor.newInstance(builder, options)

            // Run the build task with the given arguments and get the integer result
            def result = wyjcMain.run(args.toArray(new String[0]))

            logger.debug('wyjc result: {}', result)

            switch (result) {
                case 0:
                    break;
                default:
                    throw new InvalidUserDataException('compilation failed; see the compiler error ' +
                            'output for details')
            }
        } catch (NoSuchMethodException e) {
            throw new GradleScriptException("no such method '${e.getMessage()}', please ensure " +
                    'the version of the whiley-all artifact is supported by this plugin', e)
        }
    }

    /**
     * Creates a class loader based on the given whiley classpath. The class loader will include
     * all of the files in the whiley classpath.
     *
     * @param whileyClasspath the classpath to use to create the class loader.
     * @return the class loader.
     */
    private ClassLoader createClassLoader(FileCollection whileyClasspath) {
        List<URL> urls = []

        whileyClasspath.each {
            urls += [new URL("file://$it.path")]
        }

        // Create and return a new class loader with the given whiley classpath
        new URLClassLoader(urls.toArray(new URL[0]))
    }
}

