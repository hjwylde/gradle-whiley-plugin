package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.file.FileCollection
import org.gradle.api.internal.tasks.compile.Compiler
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction

class WhileyJavaCompile extends SourceTask {

    Compiler<WhileyCompileSpec> compiler = new WhileyCompiler(project)

    @OutputDirectory
    File destinationDir

    @InputFiles
    FileCollection classpath
    @InputFiles
    FileCollection bootpath

    @Nested
    WhileyCompileOptions whileyOptions = new WhileyCompileOptions()

    @TaskAction
    protected void compile() {
        WhileyCompileSpec spec = new DefaultWhileyCompileSpec(
                whileyCompileOptions: whileyOptions,
                destinationDir: destinationDir,
                source: source,
                classpath: classpath,
                bootpath: bootpath)

        println whileyOptions.properties

        compiler.execute(spec)
    }
}

