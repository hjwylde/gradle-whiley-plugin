package com.hjwylde.gradle.api.internal.tasks

import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.file.DefaultSourceDirectorySet
import org.gradle.api.internal.file.FileResolver
import org.gradle.util.ConfigureUtil

/**
 * Provides a default implementation of a {@link WhileySourceSet}. Simply creates the whiley
 * configurations as {@link SourceDirectorySet}s and initialises them with the necessary filters.
 *
 * @author Henry J. Wylde
 * @since 0.1.0
 */
class DefaultWhileySourceSet implements WhileySourceSet {

    /**
     * The source set for whiley files.
     */
    final SourceDirectorySet whiley
    /**
     * The source set for all whiley files.
     */
    final SourceDirectorySet allWhiley

    /**
     * Initialises the {@link SourceDirectorySet}s using the given name and file resolver.
     *
     * @param name the name for the source directory sets.
     * @param fileResolver the file resolver for the source directory sets.
     */
    DefaultWhileySourceSet(String name, FileResolver fileResolver) {
        whiley = new DefaultSourceDirectorySet(name, fileResolver)
        whiley.filter.include '**/*.whiley'

        allWhiley = new DefaultSourceDirectorySet(name, fileResolver)
        allWhiley.source whiley
        allWhiley.filter.include '**/*.whiley'
    }

    /**
     * {@inheritDoc}
     */
    @Override
    WhileySourceSet whiley(Closure closure) {
        // TODO: Figure out the groovy way to do this...
        ConfigureUtil.configure(closure, whiley)
        this
    }
}

