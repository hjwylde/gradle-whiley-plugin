package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.GradleScriptException
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.internal.tasks.compile.Compiler
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.WorkResult

import wybs.lang.SyntaxError
import wyjc.util.WyjcBuildTask

/**
 * An implementation of a whiley compiler that executes the process using the WDK dependency
 * provided.
 *
 * @author Henry J. Wylde
 *
 * @since 1.0.0, 12/02/2014
 */
class WhileyCompiler implements Compiler<WhileyCompileSpec> {

    private static final Logger logger = Logging.getLogger(WhileyCompiler)

    protected final Project project

    /**
     * Creates a new compiler for the given project.
     */
    WhileyCompiler(Project project) {
        assert project, 'project cannot be null'

        this.project = project
    }

    /**
     * {@inheritDoc}
     */
    @Override
    WorkResult execute(WhileyCompileSpec spec) {
        def buildTask = new WyjcBuildTask()

        buildTask.whileyDir = project.projectDir
        buildTask.classDir = spec.destinationDir
        buildTask.whileyPath = spec.classpath as List
        buildTask.bootPath = spec.bootpath as List

        def options = spec.whileyCompileOptions

        if (options?.verbose) {
            buildTask.verbose = true
        }
        if (options?.verify) {
            buildTask.verification = true
        }
        if (options?.wyildir) {
            buildTask.wyilDir = options.wyildir
        }
        if (options?.wyaldir) {
            buildTask.wyalDir = options.wyaldir
        }
        if (options?.wycsdir) {
            buildTask.wycsDir = options.wycsdir
        }

        spec.source.files.each {
            if (!it.exists()) {
                throw new InvalidUserDataException("Source file does not exist: ${project.relativePath(it)}")
            }
        }

        execute(buildTask, spec.source.files as List)

        compileFix(spec)

        true as WorkResult
    }

    protected void execute(WyjcBuildTask buildTask, List<File> files) {
        try {
            buildTask.build(files)
        } catch (SyntaxError e) {
            e.outputSourceError(System.err)
            System.err.println()

            throw new GradleScriptException('Compilation failed; see the lifecycle log output for details', e)
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
}

