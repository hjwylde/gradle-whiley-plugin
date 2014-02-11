package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.InvalidUserDataException
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
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
    WhileyCompileOptions whileyCompileOptions

    @TaskAction
    protected void compile() {
        checkBootpathNonEmpty()

        WhileyCompileSpec spec = [
            whileyCompileOptions: whileyCompileOptions,

            destinationDir: destinationDir,
            source: source,
            classpath: classpath,
            bootpath: bootpath
        ] as WhileyCompileSpec

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

    private void checkBootpathNonEmpty() {
        if (!bootpath) {
            throw new InvalidUserDataException("${name}.bootpath cannot be empty, if a WDK is " +
                    "added to the compile classpath then the bootpath will be automatically inferred")
        }
    }
}

