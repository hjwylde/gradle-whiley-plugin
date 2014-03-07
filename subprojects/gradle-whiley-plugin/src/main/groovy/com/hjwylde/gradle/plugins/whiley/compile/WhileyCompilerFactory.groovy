package com.hjwylde.gradle.plugins.whiley.compile

import org.gradle.api.internal.tasks.compile.Compiler

/**
 * A factory interface for creating a whiley compiler from a given set of whiley compile options.
 *
 * @author Henry J. Wylde
 * @since 0.2.0
 */
interface WhileyCompilerFactory {

    /**
     * Creates a compiler based on the given compile options.
     *
     * @param options the compile options.
     * @return the created compiler.
     */
    Compiler<WhileyCompileSpec> create(WhileyCompileOptions options)
}

