package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory

class WhileyCompileOptions {

    boolean verbose

    @Input
    boolean verify

    @Optional
    @OutputDirectory
    File wyildir
    @Optional
    @OutputDirectory
    File wyaldir
    @Optional
    @OutputDirectory
    File wycsdir

    // TODO: Add in ability to choose the pipeline options
}

