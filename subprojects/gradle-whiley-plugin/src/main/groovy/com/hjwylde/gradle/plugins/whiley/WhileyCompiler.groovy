package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.internal.tasks.SimpleWorkResult
import org.gradle.api.internal.tasks.compile.Compiler
import org.gradle.api.tasks.WorkResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class WhileyCompiler implements Compiler<WhileyCompileSpec> {

    private static final Logger logger = LoggerFactory.getLogger(WhileyCompiler)

    protected final Project project

    WhileyCompiler(Project project) {
        this.project = project
    }

    WorkResult execute(WhileyCompileSpec spec) {
        checkWhileyCompileSpec(spec)

        // TODO: Move this to using the wyjc.Wyjc class
        def commandLine = ["$System.env.WHILEY_HOME/bin/wyjc"]

        commandLine += ['-cd', spec.destinationDir]
        commandLine += ['-wp', spec.classpath.asPath]
        commandLine += ['-bp', spec.bootpath.asPath]

        def options = spec.whileyCompileOptions

        if (options.verbose) {
            commandLine += ['-verbose']
        }

        if (options.verify) {
            commandLine += ['-verify']
        }

        if (options.wyildir) {
            commandLine += ['-wyildir', options.wyildir]
        }

        if (options.wyaldir) {
            commandLine += ['-wyaldir', options.wyaldir]
        }

        if (options.wycsdir) {
            commandLine += ['-wycsdir', options.wycsdir]
        }

        List source = spec.source as List
        commandLine += source

        execute(commandLine)

        compileFix(spec)

        new SimpleWorkResult(true)
    }

    protected void execute(List<String> commandLine) {
        logger.info "Executing '{}'", commandLine

        // Problem here with how the whiley compiler places the files in the build directory:
        // Currently they are placed in their entire path relative from the working directory,
        // unlike Java where it places it in a path based only on the package delcaration
        def proc = commandLine.execute()
        proc.in.eachLine { if (it) logger.info it }
        proc.waitFor()

        logger.info "wyjc result: {}", proc.exitValue()

        if (proc.exitValue() != 0) {
            def sb = new StringBuffer()
            proc.consumeProcessErrorStream(sb)

            throw new InvalidUserDataException(sb.toString())
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

    private void compileFix(WhileyCompileSpec spec) {
        // Hacky fix for the problem explained above...
        // This forces all source files to be located within a path matching 'src/*/whiley'
        // and for no file to have a top level package declaration of 'src'
        def result = project.copy {
            from("$spec.destinationDir/src/$spec.destinationDir.name/whiley") {
                include '**/*.class'
            }
            into spec.destinationDir
        }

        if (!result) {
            throw new InvalidUserDataException("Unable to move source files from " +
                    "'${project.relativePath(spec.destinationDir)}/src/*/whiley' to " +
                    "'${project.relativePath(spec.destinationDir)}'")
        }

        // Delete the src directory
        result = project.delete("$spec.destinationDir/src")

        if (!result) {
            logger.warn "Unable to delete directory '{}/src'", project.relativePath(spec.destinationDir)
        }
    }
}

