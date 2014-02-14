package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.internal.tasks.compile.Compiler
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.WorkResult

/**
 * An implementation of a whiley compiler that executes the binaries provided by the installed
 * Whiley Development Kit. The compiler will attempt to source them by looking at the environment
 * variable 'WHILEY_HOME'. If 'WHILEY_HOME' is not set then it will just try executing the command,
 * assuming that it will be on the system path.
 *
 * @author Henry J. Wylde
 *
 * @since 1.0.0, 12/02/2014
 */
class BinaryWhileyCompiler implements Compiler<WhileyCompileSpec> {

    public static final String WHILEY_HOME_ENV_NAME = 'WHILEY_HOME'

    private static final Logger logger = Logging.getLogger(BinaryWhileyCompiler)

    protected final Project project

    /**
     * Creates a new compiler for the given project.
     */
    BinaryWhileyCompiler(Project project) {
        assert project, 'project cannot be null'

        this.project = project
    }

    /**
     * {@inheritDoc}
     */
    @Override
    WorkResult execute(WhileyCompileSpec spec) {
        execute([inferWyjcBinary()] + generateCompilerArgs(spec), spec.verbose)

        //compileFix(spec)

        true as WorkResult
    }

    protected void execute(List<String> commandLine, boolean verbose) {
        logger.info "Executing '{}'", commandLine

        // Problem here with how the whiley compiler places the files in the build directory:
        // Currently they are placed in their entire path relative from the working directory,
        // unlike Java where it places it in a path based only on the package delcaration
        def proc = commandLine.execute()
        proc.in.eachLine {
            if (it) {
                if (verbose) {
                    logger.lifecycle it
                } else {
                    logger.info it
                }
            }
        }
        proc.waitFor()

        logger.info "wyjc result: {}", proc.exitValue()

        if (proc.exitValue() != 0) {
            def sb = new StringBuffer()
            proc.consumeProcessErrorStream(sb)

            throw new InvalidUserDataException(sb.toString())
        }
    }

    private String getWhileyHome() {
        System.env[WHILEY_HOME_ENV_NAME]
    }

    private String inferWyjcBinary() {
        getWhileyHome() ? getWhileyHome() + '/bin/wyjc' : 'wyjc'
    }

    private List<String> generateCompilerArgs(WhileyCompileSpec spec) {
        def args = ['-cd', spec.destinationDir]
        args += ['-wp', spec.classpath.asPath]
        args += ['-bp', spec.bootpath.asPath]

        def options = spec.whileyCompileOptions

        if (options?.verbose) {
            args += ['-verbose']
        }
        if (options?.verify) {
            args += ['-verify']
        }
        if (options?.wyildir) {
            args += ['-wyildir', options.wyildir]
        }
        if (options?.wyaldir) {
            args += ['-wyaldir', options.wyaldir]
        }
        if (options?.wycsdir) {
            args += ['-wycsdir', options.wycsdir]
        }
        if (options?.whileydir) {
            args += ['-whileydir', options.whileydir]
        }

        args + (spec.source as List)
    }
}

