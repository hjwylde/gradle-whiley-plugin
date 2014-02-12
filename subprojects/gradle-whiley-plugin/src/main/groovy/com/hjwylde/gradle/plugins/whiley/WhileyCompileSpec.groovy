package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.internal.tasks.compile.CompileSpec
import org.gradle.api.file.FileCollection

interface WhileyCompileSpec extends CompileSpec {

    WhileyCompileOptions getWhileyCompileOptions()

    FileCollection getSource()

    void setSource(FileCollection source)

    File getDestinationDir()

    void setDestinationDir(File destinationDir)

    FileCollection getClasspath()

    void setClasspath(FileCollection classpath)

    FileCollection getBootpath()

    void setBootpath(FileCollection bootpath)
}

