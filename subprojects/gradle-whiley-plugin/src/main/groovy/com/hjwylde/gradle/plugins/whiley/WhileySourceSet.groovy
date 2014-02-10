package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.file.SourceDirectorySet

interface WhileySourceSet {

    SourceDirectorySet getWhiley()

    WhileySourceSet whiley(Closure closure)

    SourceDirectorySet getAllWhiley()
}

