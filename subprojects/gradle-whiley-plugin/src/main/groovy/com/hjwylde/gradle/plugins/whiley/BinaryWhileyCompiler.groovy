package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.internal.tasks.compile.Compiler
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.WorkResult

/**
 * An implementation of a whiley compiler that executes the binaries provided by the installed
 * Whiley Development Kit.
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

        compileFix(spec)

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

    private String getWhileyHome() {
        //assert System.env[WHILEY_HOME_ENV_NAME], "System environment does not contain the '$WHILEY_HOME_ENV_NAME' variable"

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

        args + (spec.source as List)
    }
}

