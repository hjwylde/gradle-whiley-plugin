package com.hjwylde.gradle.api.internal.tasks.compile

import org.gradle.api.internal.tasks.compile.Compiler
import org.gradle.api.tasks.WorkResult

/**
 * A Whiley compiler that delegates the task to a factory. The factory will create a delegate
 * compiler at execution time.
 *
 * @author Henry J. Wylde
 * @since 0.1.0
 */
class DelegatingWhileyCompiler implements Compiler<WhileyCompileSpec> {

    protected final WhileyCompilerFactory delegate

    /**
     * Creates a new compiler with the given delegate factory.
     *
     * @param delegate the factory to delegate to.
     */
    DelegatingWhileyCompiler(WhileyCompilerFactory delegate) {
        assert delegate, 'delegate cannot be null'

        this.delegate = delegate
    }

    /**
     * {@inheritDoc}
     */
    @Override
    WorkResult execute(WhileyCompileSpec spec) {
        Compiler<WhileyCompileSpec> compiler = delegate.create(spec.whileyCompileOptions)

        compiler.execute(spec)
    }
}

