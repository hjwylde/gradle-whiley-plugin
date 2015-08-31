package com.hjwylde.gradle.api.internal.tasks.compile

/**
 * A helper class for generating the arguments to pass to a compiler from a compile specification.
 *
 * @author Henry J. Wylde
 * @since 0.1.0
 */
class WhileyCompilerArgumentsBuilder {

    private final WhileyCompileSpec spec;

    /**
     * Creates a new arguments builder for the given specification.
     */
    WhileyCompilerArgumentsBuilder(WhileyCompileSpec spec) {
        assert spec, 'spec cannot be null'

        this.spec = spec
    }

    /**
     * Builds the command line arguments using the specification and returns them as a list. The
     * arguments include the source files at the end of the list.
     *
     * @return the built list of arguments.
     */
    List<String> build() {
        def args = []

        // Add the mandatory items
        args += ['-cd', spec.destinationDir]
        args += ['-wp', spec.classpath.asPath]
        args += ['-bp', spec.bootpath.asPath]

        // Add the optional items
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
        if (options?.compilerArgs) {
            args += options.compilerArgs
        }

        // Add the source items
        args += (spec.source as List)

        // Convert each item to a string and return the result
        args.collect { it.toString() }
    }
}

