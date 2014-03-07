package com.hjwylde.gradle.plugins.whiley.config

import org.gradle.api.Project
import org.gradle.api.internal.file.collections.LazilyInitializedFileCollection
import org.gradle.api.file.FileCollection

/**
 * A helper class for runtime generation of items required for compilation at runtime.
 *
 * @author Henry J. Wylde
 * @since 0.2.0
 */
final class WhileyRuntime {

    /**
     * A list of the whiley jars that are provided by the Whiley Development Kit that are required
     * for the compiler bootpath.
     */
    public static final List<String> WHILEY_BOOTPATH_LIBS = ['whiley-all', 'wyrt']
    /**
     * A list of the whiley jars that are provided by the Whiley Development Kit that are required
     * for the compiler classpath.
     */
    public static final List<String> WHILEY_CLASSPATH_LIBS = ['jasm', 'whiley-all', 'wybs', 'wyc', 'wycs', 'wyil', 'wyjc', 'wyrl', 'wyrt']

    private final Project project

    /**
     * Creates a new whiley runtime for the given project.
     *
     * @param project the project.
     */
    WhileyRuntime(Project project) {
        assert project, 'project cannot be null'

        this.project = project
    }

    /**
     * Attempts to infer the whiley bootpath using the given classpath list. The classpath should
     * contain a library starting with one of the values from {@value #WHILEY_BOOTPATH_LIBS}.
     *
     * @param classpath the classpath to use to infer the bootpath.
     * @return the inferred bootpath.
     */
    final FileCollection inferWhileyBootpath(FileCollection classpath) {
        return [
            createDelegate: { project.files(
                    classpath.findAll { path ->
                        WHILEY_BOOTPATH_LIBS.any {
                            path.name.startsWith it
                        }
                    })
            }
        ] as LazilyInitializedFileCollection
    }

    /**
     * Attempts to infer the whiley classpath using the given classpath list. The classpath should
     * contain a library starting with one of the values from {@value #WHILEY_CLASSPATH_LIBS}.
     *
     * @param classpath the classpath to use to infer the classpath.
     * @return the inferred classpath.
     */
    final FileCollection inferWhileyClasspath(FileCollection classpath) {
        return [
            createDelegate: { project.files(
                    classpath.findAll { path ->
                        WHILEY_CLASSPATH_LIBS.any {
                            path.name.startsWith it
                        }
                    })
            }
        ] as LazilyInitializedFileCollection
    }
}

