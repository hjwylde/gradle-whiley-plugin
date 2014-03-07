package com.hjwylde.gradle.plugins.whiley.compile

import org.gradle.api.file.FileCollection

/**
 * Provides a default implementation of the {@link WhileyCompileSpec}. This forces Groovy
 * to create the fields as properties and the necessary getters and setters.
 * <p>
 * Every field is initialised to null and should be overwritten.
 *
 * @author Henry J. Wylde
 * @since 0.1.0
 */
class EmptyWhileyCompileSpec implements WhileyCompileSpec {

    /**
     * The optional whiley compile options.
     */
    WhileyCompileOptions whileyCompileOptions

    /**
     * The source files to compile.
     */
    FileCollection source
    /**
     * The destination directory to place compiled source files.
     */
    File destinationDir
    /**
     * The classpath to use when compiling the source files. The classpath should contain the
     * necessary referenced files by the source.
     */
    FileCollection classpath
    /**
     * The bootpath to use when compiling the source files. The bootpath should contain the whiley
     * runtime files, i.e. a {@code whiley-all} or {@code wyrt} artifact.
     */
    FileCollection bootpath
    /**
     * The whiley classpath to use. The whiley classpath is the path that contains the whiley
     * compiler files, i.e. a {@code whiley-all} artifact.
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
    void setSource(FileCollection source) {
        assert source != null, 'source cannot be null'

        this.source = source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setDestinationDir(File destinationDir) {
        assert destinationDir, 'destinationDir cannot be null'

        this.destinationDir = destinationDir;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void setClasspath(FileCollection classpath) {
        assert classpath != null, 'classpath cannot be null'

        this.classpath = classpath;
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

