package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.InvalidUserDataException
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
                source: source,
                destinationDir: destinationDir,
                classpath: classpath,
                bootpath: bootpath)

        checkWhileyCompileSpec(spec)

        compiler.execute(spec)
    }

    private void checkWhileyCompileSpec(WhileyCompileSpec spec) {
        if (!spec.source)
            throw new InvalidUserDataException("${name}.source cannot be empty or null")
        if (!spec.destinationDir)
            throw new InvalidUserDataException("${name}.destinationDir cannot be null")
        if (spec.classpath == null)
            throw new InvalidUserDataException("${name}.classpath cannot be null")
        if (!spec.bootpath)
            throw new InvalidUserDataException("${name}.bootpath cannot be empty or null")
    }
}

