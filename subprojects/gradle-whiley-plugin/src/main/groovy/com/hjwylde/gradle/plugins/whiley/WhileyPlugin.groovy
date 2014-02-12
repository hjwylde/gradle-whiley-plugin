package com.hjwylde.gradle.plugins.whiley

import static org.gradle.api.tasks.SourceSet.MAIN_SOURCE_SET_NAME

import org.gradle.api.InvalidUserDataException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.file.FileCollection
import org.gradle.api.plugins.JavaPlugin

import javax.inject.Inject

/**
 * TODO: Documentation
 *
 * @author Henry J. Wylde
 *
 * @since 1.0.0, 05/02/2014
 */
class WhileyPlugin implements Plugin<Project> {

    public static final String WHILEY_EXTENSION_NAME = 'whiley'
    public static final String WHILEY_COMPILER_EXTENSION_NAME = 'compiler'

    //private static final List<String> WHILEY_CLASSPATH_LIBS = ['jasm', 'wybs', 'wycc', 'wycs', 'wyc',
    //        'wyil', 'wyjc', 'wyrl', 'wyrt']
    private static final List<String> WHILEY_BOOTPATH_LIBS = ['wyrt']

    protected Project project

    protected final FileResolver fileResolver

    @Inject
    WhileyPlugin(FileResolver fileResolver) {
        this.fileResolver = fileResolver
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void apply(Project project) {
        checkEnvironment()

        this.project = project

        project.plugins.apply(JavaPlugin.class)

        configureExtensions()
        configureSourceSets()
        configureCompileDefaults()
    }

    private void checkEnvironment() {
        if (!System.env.WHILEY_HOME)
            throw new InvalidUserDataException('Environment variable WHILEY_HOME is not set')
    }

    private void configureExtensions() {
        def whileyExtension = project.extensions.create(WHILEY_EXTENSION_NAME, WhileyExtension)
        whileyExtension.extensions.create(WHILEY_COMPILER_EXTENSION_NAME, WhileyCompilerExtension)
    }

    private void configureSourceSets() {
        project.sourceSets.all { set ->
            // Set up the default Whiley source set and add to the default source sets
            WhileySourceSet whileySourceSet = new DefaultWhileySourceSet(
                    "$set.name Whiley source", fileResolver)
            set.convention.plugins.whiley = whileySourceSet

            whileySourceSet.whiley.srcDir "src/$set.name/whiley"
            set.resources.filter.exclude {
                whileySourceSet.whiley.contains it.file
            }
            set.allSource.source whileySourceSet.whiley

            // Create the compile task for this source set
            def compileTaskName = set.getCompileTaskName('whiley')
            project.task(compileTaskName, type: WhileyJavaCompile) {
                description "Compiles Whiley source '$set.whiley:whiley'."

                source whileySourceSet.whiley
                destinationDir = set.output.classesDir
                classpath = set.compileClasspath
            }

            project.tasks.getByName(set.classesTaskName).dependsOn compileTaskName
        }
    }

    private void configureCompileDefaults() {
        project.tasks.withType(WhileyJavaCompile) {
            it.bootpath = inferWhileyBootpath(it.classpath)
        }

        // TODO: There must be a better, 'groovy', way to do this...
        // It was the only way I knew for now how to execute the closure after the configuration phase
        // of Gradle, as the extension customisation from the user wasn't being loaded properly
        project.afterEvaluate { project ->
            project.tasks.withType(WhileyJavaCompile) {
                def whileyCompilerExtension = project.extensions.findByName(WHILEY_EXTENSION_NAME).
                        extensions.findByName(WHILEY_COMPILER_EXTENSION_NAME)

                // TODO: This overwrites user set options...
                it.whileyOptions = [
                    verbose: whileyCompilerExtension.verbose,

                    verify: whileyCompilerExtension.verbose,

                    wyildir: whileyCompilerExtension.wyildir,
                    wyaldir: whileyCompilerExtension.wyaldir,
                    wycsdir: whileyCompilerExtension.wycsdir
                ] as WhileyCompileOptions
            }
        }
    }

    private FileCollection inferWhileyBootpath(FileCollection classpath) {
        // TODO: Fix me... This doesn't actually filter the file collection
        project.files(classpath) {
            filter { path ->
                WHILEY_BOOTPATH_LIBS.any {
                    path.name.startsWith it
                }
            }
        }
    }
}

