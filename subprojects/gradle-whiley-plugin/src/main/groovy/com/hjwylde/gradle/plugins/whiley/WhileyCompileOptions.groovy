package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory

/**
 * A set of compile options for the whiley compiler. The compile options are all the extra
 * (optional) options that a compiler needs as well as the {@link WhileyCompileSpec} to compile
 * the source code.
 * <p>
 * For a full list of the compile options, check the 'wyc' binary included in the Whiley
 * development kit.
 *
 * @author Henry J. Wylde
 *
 * @since 1.0.0, 12/02/2014
 */
class WhileyCompileOptions {

    /**
     * Determines whether the compiler should be more verbose during compilation.
     * <p>
     * Default: <code>false</code>.
     */
    boolean verbose

    /**
     * Determines whether the compiler should attempt to verify the source file constraints
     * using the constraint solver.
     * <p>
     * Default: <code>false</code>.
     */
    @Input
    boolean verify

    /**
     * The output directory for the <code>wyil</code> intermediary files.
     * <p>
     * Default: <code>null</code>.
     */
    @Optional
    @OutputDirectory
    File wyildir
    /**
     * The output directory for the <code>wyal</code> intermediary files. Note that these files
     * are only generated if {@link #verify} is set to <code>true</code>.
     * <p>
     * Default: <code>null</code>.
     */
    @Optional
    @OutputDirectory
    File wyaldir
    /**
     * The output directory for the <code>wycs</code> intermediary files. Note that these files
     * are only generated if {@link #verify} is set to <code>true</code>.
     * <p>
     * Default: <code>null</code>.
     */
    @Optional
    @OutputDirectory
    File wycsdir

    // TODO: Add in ability to choose the pipeline options
}

