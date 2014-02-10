package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.InvalidUserDataException
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction

class WhileyCompile extends SourceTask {

    @OutputDirectory
    File destinationDir

    @InputFiles
    FileCollection classpath

    @TaskAction
    protected void compile() {
        List files = source as List

        logger.info "Executing '{}'", (["${getWhileyBinDir()}/wyjc", '-cd', destinationDir] + files)

        def proc = (["${getWhileyBinDir()}/wyjc", '-cd', destinationDir] + files).execute()
        proc.in.eachLine { if (it) logger.info it }
        proc.waitFor()

        logger.info "wyjc result: {}", proc.exitValue()

        if (proc.exitValue() != 0) {
            def sb = new StringBuffer()
            proc.consumeProcessErrorStream(sb)

            throw new InvalidUserDataException(sb.toString())
        }

        didWork = proc.exitValue() == 0
    }

    protected String getWhileyBinDir() {
        "$System.env.WHILEY_HOME/bin"
    }
}

