package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.file.DefaultSourceDirectorySet
import org.gradle.api.internal.file.FileResolver
import org.gradle.util.ConfigureUtil

class DefaultWhileySourceSet implements WhileySourceSet {

    final SourceDirectorySet whiley
    final SourceDirectorySet allWhiley

    DefaultWhileySourceSet(String name, FileResolver fileResolver) {
        whiley = new DefaultSourceDirectorySet(name, fileResolver)
        whiley.filter.include '**/*.whiley'

        allWhiley = new DefaultSourceDirectorySet(name, fileResolver)
        allWhiley.source whiley
        allWhiley.filter.include '**/*.whiley'
    }

    WhileySourceSet whiley(Closure closure) {
        // TODO: Figure out the groovy way to do this...
        ConfigureUtil.configure(closure, whiley)
        this
    }
}

