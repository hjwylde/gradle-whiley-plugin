package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.file.FileCollection

/**
 * Provides a default implementation of the {@link WhileyCompileSpec}. This forces Groovy
 * to create the fields as properties and the necessary getters and setters.
 * <p>
 * Every field is initialised to null and should be overwritten.
 *
 * @author Henry J. Wylde
 *
 * @since 1.0.0, 12/02/2014
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
     * The classpath to use when compiling the source files.
     */
    FileCollection classpath
    /**
     * The bootpath to use with the compiler. (Should contain the wyrt-v*.jar file.)
     */
    FileCollection bootpath

    void setWhileyCompileOptions(WhileyCompileOptions whileyCompileOptions) {
        assert whileyCompileOptions != null, 'whileyCompileOptions cannot be null'

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
        assert destinationDir != null, 'destinationDir cannot be null'

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
}

