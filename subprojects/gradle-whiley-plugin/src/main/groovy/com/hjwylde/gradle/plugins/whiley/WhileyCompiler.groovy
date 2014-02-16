package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.GradleScriptException
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.internal.tasks.compile.Compiler
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.WorkResult

import wycc.lang.SyntaxError
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
        if (options?.whileydir) {
            buildTask.whileyDir = options.whileydir
        }

        spec.source.files.each {
            if (!it.exists()) {
                throw new InvalidUserDataException("Source file does not exist: ${project.relativePath(it)}")
            }
        }

        execute(buildTask, spec.source.files as List, options?.brief ?: false)

        //compileFix(spec)

        true as WorkResult
    }

    protected void execute(WyjcBuildTask buildTask, List<File> files, boolean brief) {
        try {
            buildTask.build(files)
        } catch (SyntaxError e) {
            e.outputSourceError(System.err, brief)
            System.err.println()

            throw new GradleScriptException('Compilation failed; see the compiler error output for details', e)
        }
    }
}

