package com.hjwylde.gradle.api.internal.tasks.compile

import org.gradle.language.base.internal.compile.CompilerFactory

/**
 * A factory interface for creating a whiley compiler from a given whiley compile specification.
 *
 * @author Henry J. Wylde
 * @since 0.1.0
 */
interface WhileyCompilerFactory extends CompilerFactory<WhileyCompileSpec> {}

