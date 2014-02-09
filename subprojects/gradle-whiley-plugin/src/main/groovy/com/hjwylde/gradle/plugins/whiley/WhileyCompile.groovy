package com.hjwylde.gradle.plugins.whiley

import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction

/**
 * TODO: Documentation
 *
 * @author Henry J. Wylde
 *
 * @since 1.0.0, 05/02/2014
 */
class WhileyCompile extends DefaultTask {

    public static final String BASE_WYIL_DIR = 'wyil'

    private SourceSet sourceSet

    @TaskAction
    protected void compile() {
        if (!sourceSet)
            throw new InvalidUserDataException("'${name}.sourceSet' must not be empty")

        // sourceSets.main.java.getAsPath()

        // Create the build directories
        sourceSet.output.classesDir.mkdirs()
        wyilDir.mkdirs()

        // Source whiley files
        def tree = project.fileTree(dir: sourceDir, include: '**/*.whiley')
        def source = tree.files.collect { it.path - sourceDir.path - '/' }.join(' ')

        logger.info "Executing 'wyjc -cd \"{}\" -od \"{}\" \"{}\"'", sourceSet.output.classesDir, wyilDir, source

        def proc = ['wyjc', '-cd', sourceSet.output.classesDir, '-od', wyilDir, source].execute([], project.file(sourceDir))
        proc.in.eachLine { if (it) logger.info it }
        proc.waitFor()

        logger.info "wyjc result: {}", proc.exitValue()

        if (proc.exitValue() != 0) {
            def sb = new StringBuffer()
            proc.consumeProcessErrorStream(sb)

            throw new InvalidUserDataException(sb.toString())
        }
    }

    SourceSet getSourceSet() {
        sourceSet
    }

    void setSourceSet(SourceSet sourceSet) {
        this.sourceSet = sourceSet
    }

    File getWyilDir() {
        new File(project.buildDir, "$BASE_WYIL_DIR/$sourceSet.name/")
    }

    File getSourceDir() {
        project.file("src/$sourceSet.name/whiley")
    }
}

