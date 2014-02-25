package com.hjwylde.gradle.plugins.whiley

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
 * compiler packaged with the plugin. If a different compiler version is desired, then it can be
 * set to a {@link BinaryWhileyCompiler} which sources it from the local WDK.
 *
 * @author Henry J. Wylde
 *
 * @since 1.0.0, 12/02/2014
 */
class WhileyCompile extends SourceTask {

    /**
     * The compiler to use for the source code.
     */
    Compiler<WhileyCompileSpec> compiler = new WhileyCompiler(project)

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
     * The optional whiley compile options to configure to the compiler.
     */
    @Nested
    WhileyCompileOptions whileyOptions = new WhileyCompileOptions()

    /**
     * Compiles the whiley source code.
     */
    @TaskAction
    protected void compile() {
        if (!bootpath) {
            throw new InvalidUserDataException("${name}.bootpath cannot be null or empty; the bootpath is implicitely implied from the classpath, please make sure a wyrt-v*.jar is included in the classpath")
        }

        def spec = new EmptyWhileyCompileSpec(
                whileyCompileOptions: whileyOptions,
                source: source,
                destinationDir: destinationDir,
                classpath: classpath,
                bootpath: bootpath)

        compiler.execute(spec)
    }
}

