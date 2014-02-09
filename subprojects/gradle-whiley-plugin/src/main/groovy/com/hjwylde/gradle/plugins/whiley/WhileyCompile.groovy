package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction

/**
 * TODO: Documentation
 *
 * @author Henry J. Wylde
 *
 * @since 1.0.0, 05/02/2014
 */
class WhileyCompile extends DefaultTask {

    @TaskAction
    protected void compile() {
        // Output directory for the files
        //temporaryDir.mkdirs()

        // Source whiley files
        def tree = project.fileTree(dir: 'src/main/whiley', include: '**/*.whiley')
        def source = tree.files.collect { project.relativePath(it.path) - 'src/main/whiley/' }.join(' ')

        logger.info "Executing 'wyjc -cd \"$temporaryDir\" \"$source\"'"

        project.file('build/wyil/main').mkdirs()

        project.sourceSets.main.output.classesDir.mkdirs()

        def proc = ['wyjc', '-cd', project.sourceSets.main.output.classesDir, '-od', '../../../build/wyil/main',
                source].execute([], project.file('src/main/whiley'))
        proc.in.eachLine { if (it) logger.info it }
        proc.waitFor()

        logger.info "wyjc result: {}", proc.exitValue()

        if (proc.exitValue() != 0) {
            def sb = new StringBuffer()
            proc.consumeProcessErrorStream(sb)

            throw new InvalidUserDataException(sb.toString())
        }

        // TODO: Change this to only rename the parent directory, don't need to do for each file
        //def basePath = "$temporaryDir/src/main/whiley"
        //tree = project.fileTree(dir: basePath, include: '**/*.class')
        //tree.files.collect {
            //def path = "${project.sourceSets.main.output.classesDir}/${it.path - basePath}"
            //project.file(path).parentFile.mkdirs()

            //it.renameTo(path)
        //}
    }
}

