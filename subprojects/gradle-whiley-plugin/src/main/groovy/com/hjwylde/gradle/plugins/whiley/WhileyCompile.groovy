package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.InvalidUserDataException
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction

class WhileyCompile extends SourceTask {

    @OutputDirectory
    File destinationDir

    @InputFiles
    FileCollection classpath

    @TaskAction
    protected void compile() {
        List files = source as List

        logger.info "Executing '{}'", (["${getWhileyBinDir()}/wyjc", '-cd', destinationDir] + files)

        // Problem here with how the whiley compiler places the files in the build directory:
        // Currently they are placed in their entire path relative from the working directory,
        // unlike Java where it places it in a path based only on the package delcaration
        def proc = (["${getWhileyBinDir()}/wyjc", '-cd', destinationDir] + files).execute()
        proc.in.eachLine { if (it) logger.info it }
        proc.waitFor()

        logger.info "wyjc result: {}", proc.exitValue()

        if (proc.exitValue() != 0) {
            def sb = new StringBuffer()
            proc.consumeProcessErrorStream(sb)

            throw new InvalidUserDataException(sb.toString())
        }

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

        didWork = !source.isEmpty()
    }

    protected String getWhileyDir() {
        System.env.WHILEY_HOME
    }

    protected String getWhileyBinDir() {
        "${getWhileyDir()}/bin"
    }
}

