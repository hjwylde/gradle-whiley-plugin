package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.file.FileCollection

class DefaultWhileyCompileSpec implements WhileyCompileSpec {

    WhileyCompileOptions whileyCompileOptions

    FileCollection source
    File destinationDir
    FileCollection classpath
    FileCollection bootpath
}
