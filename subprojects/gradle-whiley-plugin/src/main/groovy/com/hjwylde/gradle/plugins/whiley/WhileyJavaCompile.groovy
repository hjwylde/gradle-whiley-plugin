package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.InvalidUserDataException
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction

class WhileyJavaCompile extends SourceTask {

    @OutputDirectory
    File destinationDir

    @InputFiles
    FileCollection classpath
    @InputFiles
    FileCollection bootpath

    @Nested
    WhileyCompileOptions whileyCompileOptions = new WhileyCompileOptions()

    public WhileyCompileOptions getWhileyOptions() {
        whileyCompileOptions
    }

    public void setWhileyOptions(WhileyCompileOptions whileyCompileOptions) {
        this.whileyCompileOptions = whileyCompileOptions
    }

    public void setWhileyOptions(Closure closure) {
        // TODO: Implement this method
        assert false, 'Not implemented!'
    }

    @TaskAction
    protected void compile() {
        WhileyCompileSpec spec = new DefaultWhileyCompileSpec(
                whileyCompileOptions: whileyOptions,
                destinationDir: destinationDir,
                source: source,
                classpath: classpath,
                bootpath: bootpath)

        checkWhileyCompileSpec(spec)

        new WhileyCompiler().execute(spec)

        compileFix()
    }

    protected void compileFix() {
        // Hacky fix for the problem explained above...
        // This forces all source files to be located within a path matching 'src/*/whiley'
        // and for no file to have a top level package declaration of 'src'
        def result = project.copy {
            from("$destinationDir/src/$destinationDir.name/whiley") {
                include '**/*.class'
            }
            into destinationDir
        }

        if (!result) {
            throw new InvalidUserDataException("Unable to move source files from " +
                    "'${project.relativePath(destinationDir)}/src/*/whiley' to " +
                    "'${project.relativePath(destinationDir)}'")
        }

        // Delete the src directory
        result = project.delete("$destinationDir/src")

        if (!result) {
            logger.warn "Unable to delete directory '{}/src'", project.relativePath(destinationDir)
        }
    }

    private void checkWhileyCompileSpec(WhileyCompileSpec spec) {
        if (!spec.whileyCompileOptions)
            throw new InvalidUserDataException("${name}.whileyCompileOptions cannot be empty or " +
                    'null')

        if (!spec.destinationDir)
            throw new InvalidUserDataException("${name}.destinationDir cannot be empty or null")
        if (!spec.source)
            throw new InvalidUserDataException("${name}.source cannot be empty or null")
        if (!spec.classpath)
            throw new InvalidUserDataException("${name}.classpath cannot be empty or null")
        if (!spec.bootpath)
            throw new InvalidUserDataException("${name}.bootpath cannot be empty or null")
    }
}

