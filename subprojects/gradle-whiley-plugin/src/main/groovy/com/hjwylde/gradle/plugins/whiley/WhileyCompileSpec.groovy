package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.internal.tasks.compile.CompileSpec
import org.gradle.api.file.FileCollection

interface WhileyCompileSpec extends CompileSpec {

    WhileyCompileOptions getWhileyCompileOptions()

    File getDestinationDir()

    void setDestinationDir(File destinationDir)

    FileCollection getSource()

    void setSource(FileCollection source)

    FileCollection getClasspath()

    void setClasspath(FileCollection classpath)

    FileCollection getBootpath()

    void setBootpath(FileCollection bootpath)
}

