package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.InvalidUserDataException
import org.gradle.api.internal.tasks.SimpleWorkResult
import org.gradle.api.internal.tasks.compile.Compiler
import org.gradle.api.tasks.WorkResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class WhileyCompiler implements Compiler<WhileyCompileSpec> {

    private static final Logger logger = LoggerFactory.getLogger(WhileyCompiler)

    WorkResult execute(WhileyCompileSpec spec) {
        // TODO: Move this to using the wyjc.Wyjc class
        def commandLine = ["$System.env.WHILEY_HOME/bin/wyjc"]

        // TODO: Move the check spec into this class
        assert spec.destinationDir
        commandLine += ['-cd', spec.destinationDir]

        assert spec.classpath
        commandLine += ['-wp', spec.classpath.asPath]

        assert spec.bootpath
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
    }

    protected WorkResult execute(List<String> commandLine) {
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

        new SimpleWorkResult(true)
    }
}

