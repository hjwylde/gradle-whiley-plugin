package com.hjwylde.gradle.plugins.whiley.compile

import org.gradle.api.Project
import org.gradle.api.internal.tasks.compile.Compiler

/**
 * A default implementation of a compiler factory. When requested to create a compiler, this
 * factory will analyse the options to determine the type of compiler to create.
 *
 * @author Henry J. Wylde
 * @since 0.2.0
 */
class DefaultWhileyCompilerFactory implements WhileyCompilerFactory {

    protected final Project project

    /**
     * Creates a new compiler factory for the given project.
     *
     * @project the project.
     */
    DefaultWhileyCompilerFactory(Project project) {
        assert project, 'project cannot be null'

        this.project = project;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Compiler<WhileyCompileSpec> create(WhileyCompileOptions options) {
        new ApiWhileyCompiler(project)
    }
}

