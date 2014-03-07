package com.hjwylde.gradle.plugins.whiley.compile

import org.gradle.api.InvalidUserDataException
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.tasks.compile.Compiler
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction

/**
 * Compiles whiley source files into class files. By default the compile method will use the
 * compiler specified in the compile time dependencies of the project.
 *
 * @author Henry J. Wylde
 * @since 0.1.0
 */
class WhileyCompile extends SourceTask {

    /**
     * The compiler to use for the source code.
     */
    protected final Compiler<WhileyCompileSpec> compiler

    /**
     * The destination directory for the compiled source code.
     */
    @OutputDirectory
    File destinationDir

    /**
     * The classpath to use during compilation.
     */
    @InputFiles
    FileCollection classpath

    /**
     * The bootpath to use during compilation.
     */
    FileCollection bootpath

    /**
     * The whiley classpath to use during compilation.
     */
    FileCollection whileyClasspath

    /**
     * The optional whiley compile options to configure to the compiler.
     */
    @Nested
    WhileyCompileOptions whileyOptions = new WhileyCompileOptions()

    /**
     * Creates a new whiley compile task, intialising the compiler.
     */
    WhileyCompile() {
        def whileyCompilerFactory = new DefaultWhileyCompilerFactory(project)

        compiler = new DelegatingWhileyCompiler(whileyCompilerFactory)
    }

    /**
     * Compiles the whiley source code.
     */
    @TaskAction
    protected void compile() {
        if (!bootpath) {
            throw new InvalidUserDataException("${name}.bootpath cannot be null or empty; the " +
                    'bootpath is implicitely implied from the runtime classpath, please make ' +
                    'sure a whiley-all or wyrt artifact is included in the runtime classpath')
        }
        if (!whileyClasspath) {
            throw new InvalidUserDataException("${name}.whileyClasspath cannot be null or " +
                    'empty; the whileyClasspath is implicitely implied from the compile ' +
                    'classpath, please make sure a whiley-all artifact is included in the ' +
                    'compile classpath')
        }

        def spec = new EmptyWhileyCompileSpec(
                whileyCompileOptions: whileyOptions,
                source: source,
                destinationDir: destinationDir,
                classpath: classpath,
                bootpath: bootpath,
                whileyClasspath: whileyClasspath)

        compiler.execute(spec)
    }
}

