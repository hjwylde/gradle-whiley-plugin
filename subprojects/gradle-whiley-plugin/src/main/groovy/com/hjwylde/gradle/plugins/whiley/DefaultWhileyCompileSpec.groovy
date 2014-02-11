package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.file.FileCollection

class DefaultWhileyCompileSpec implements WhileyCompileSpec {

    WhileyCompileOptions whileyCompileOptions

    File destinationDir
    FileCollection source
    FileCollection classpath
    FileCollection bootpath
}

