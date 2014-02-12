package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.file.SourceDirectorySet

/**
 * Provides the <code>whiley</code> and <code>allWhiley</code> properties for addition to existing
 * source sets.
 *
 * @author Henry J. Wylde
 *
 * @since 1.0.0, 12/02/2014
 */
interface WhileySourceSet {

    /**
     * Gets the {@link SourceDirectorySet} containing the whiley source files.
     * @return the whiley source.
     */
    SourceDirectorySet getWhiley()

    /**
     * Applies the given closure to the whiley {@link SourceDirectorySet}.
     *
     * @return this.
     */
    WhileySourceSet whiley(Closure closure)

    /**
     * Gets the {@link SourceDirectorySet} containing all the whiley source files.
     *
     * @return all the whiley source.
     */
    SourceDirectorySet getAllWhiley()
}

