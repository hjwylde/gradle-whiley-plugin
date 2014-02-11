package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.tasks.OutputDirectory

class WhileyCompileOptions {

    boolean verbose

    boolean verify

    @OutputDirectory
    File wyildir
    @OutputDirectory
    File wyaldir
    @OutputDirectory
    File wycsdir

    // TODO: Add in ability to choose the pipeline options
}

