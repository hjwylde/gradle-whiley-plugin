package com.hjwylde.gradle.api.internal.tasks.compile

import com.hjwylde.gradle.api.tasks.compile.WhileyCompileOptions

import org.gradle.api.file.FileCollection
import org.gradle.api.internal.tasks.compile.DefaultJvmLanguageCopmileSpec

/**
 * Provides a default implementation of the {@link WhileyCompileSpec}. This forces Groovy
 * to create the fields as properties and the necessary getters and setters.
 * <p>
 * Every field is initialised to null and should be overwritten.
 *
 * @author Henry J. Wylde
 * @since 0.1.0
 */
class DefaultWhileyCompileSpec extends DefaultJvmLanguageCompileSpec implements WhileyCompileSpec {

    /**
     * The optional whiley compile options.
     */
    WhileyCompileOptions whileyCompileOptions

    /**
     * The bootpath to use when compiling the source files. The bootpath should contain the whiley
     * runtime files, e.g. a {@code whiley-all} or {@code wyrt} artifact.
     */
    FileCollection bootpath
    /**
     * The whiley classpath to use. The whiley classpath is the path that contains the whiley
     * compiler files, e.g. a {@code whiley-all} artifact.
     */
    FileCollection whileyClasspath

    /**
     * Sets the whiley compile options to the given options.
     *
     * @param whileyCompileOptions the new options.
     */
    void setWhileyCompileOptions(WhileyCompileOptions whileyCompileOptions) {
        assert whileyCompileOptions, 'whileyCompileOptions cannot be null'

        this.whileyCompileOptions = whileyCompileOptions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setBootpath(FileCollection bootpath) {
        assert bootpath != null, 'bootpath cannot be null'

        this.bootpath = bootpath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setWhileyClasspath(FileCollection whileyClasspath) {
        assert whileyClasspath != null, 'whileyClasspath cannot be null'

        this.whileyClasspath = whileyClasspath;
    }
}

