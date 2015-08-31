package com.hjwylde.gradle.api.internal.tasks.compile

import com.hjwylde.gradle.api.tasks.compile.WhileyCompileOptions

import org.gradle.api.file.FileCollection
import org.gradle.api.internal.tasks.compile.JvmLanguageCompileSpec

/**
 * A compile spec for the whiley language and compiler. Provides the required methods for compiling
 * whiley source files.
 *
 * @author Henry J. Wylde
 * @since 0.1.0
 */
interface WhileyCompileSpec extends JvmLanguageCompileSpec {

    /**
     * Gets the optional whiley compile options.
     * <p>
     * May return null.
     *
     * @return the whiley compile options or null.
     */
    WhileyCompileOptions getWhileyCompileOptions()

    /**
     * Gets the bootpath to use during compilation. The bootpath should contain the whiley runtime
     * files, e.g. a {@code whiley-all} or {@code wyrt} artifact.
     *
     * @return the compilation bootpath.
     */
    FileCollection getBootpath()

    /**
     * Sets the bootpath to use during compilation. The bootpath should contain the whiley runtime
     * files, e.g. a {@code whiley-all} or {@code wyrt} artifact.
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
     Iterable<File> getWhileyClasspath()

     /**
      * Sets the Whiley classpath to use during compilation. The Whiley classpath is the path that
      * the compiler is loaded from. It should include a {@code whiley-all} artifact.
      *
      * @param whileyClasspath the Whiley classpath.
      */
     void setWhileyClasspath(Iterable<? extends File> whileyClasspath)
}

