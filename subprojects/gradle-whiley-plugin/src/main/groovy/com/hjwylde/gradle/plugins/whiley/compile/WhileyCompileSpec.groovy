package com.hjwylde.gradle.plugins.whiley.compile

import org.gradle.api.internal.tasks.compile.CompileSpec
import org.gradle.api.file.FileCollection

/**
 * A compile spec for the whiley language and compiler. Provides the required methods for compiling
 * whiley source files.
 *
 * @author Henry J. Wylde
 * @since 0.1.0
 */
interface WhileyCompileSpec extends CompileSpec {

    /**
     * Gets the optional whiley compile options.
     * <p>
     * May return null.
     *
     * @return the whiley compile options or null.
     */
    WhileyCompileOptions getWhileyCompileOptions()

    /**
     * Gets the whiley source files to compile.
     *
     * @return the whiley source files.
     */
    FileCollection getSource()

    /**
     * Sets the whiley source files for compilation.
     *
     * @param source the whiley source files.
     */
    void setSource(FileCollection source)

    /**
     * Gets the destination directory of the compiled source files.
     *
     * @return the destination directory of the compiled source files.
     */
    File getDestinationDir()

    /**
     * Sets the destination directory of the compiled source files.
     *
     * @param destinationDir the destination directory.
     */
    void setDestinationDir(File destinationDir)

    /**
     * Gets the classpath to use during compilation.
     *
     * @return the compilation classpath.
     */
    FileCollection getClasspath()

    /**
     * Sets the classpath to use during compilation.
     *
     * @param classpath the classpath.
     */
    void setClasspath(FileCollection classpath)

    /**
     * Gets the bootpath to use during compilation. The bootpath should contain the whiley runtime
     * files, i.e. a {@code whiley-all} or {@code wyrt} artifact.
     *
     * @return the compilation bootpath.
     */
    FileCollection getBootpath()

    /**
     * Sets the bootpath to use during compilation. The bootpath should contain the whiley runtime
     * files, i.e. a {@code whiley-all} or {@code wyrt} artifact.
     *
     * @param bootpath the bootpath.
     */
    void setBootpath(FileCollection bootpath)

     /**
      * Gets the Whiley classpath to use during compilation. The Whiley classpath is the path that
      * the compiler is loaded from. It should include a {@code whiley-all} artifact.
      *
      * @return the Whiley classpath.
      */
     FileCollection getWhileyClasspath()

     /**
      * Sets the Whiley classpath to use during compilation. The Whiley classpath is the path that
      * the compiler is loaded from. It should include a {@code whiley-all} artifact.
      *
      * @param whileyClasspath the Whiley classpath.
      */
     void setWhileyClasspath(FileCollection whileyClasspath)
}

