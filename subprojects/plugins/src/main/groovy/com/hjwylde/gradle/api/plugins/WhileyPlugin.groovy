package com.hjwylde.gradle.api.plugins

import static org.gradle.api.tasks.SourceSet.MAIN_SOURCE_SET_NAME

import com.hjwylde.gradle.api.internal.tasks.DefaultWhileySourceSet
import com.hjwylde.gradle.api.tasks.WhileyRuntime
import com.hjwylde.gradle.api.tasks.WhileySourceSet
import com.hjwylde.gradle.api.tasks.compile.WhileyCompile

import org.gradle.api.InvalidUserDataException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.plugins.JavaPlugin

import javax.inject.Inject


/**
 * The plugin for the Whiley language. This plugin extends the {@link JavaPlugin} and adds in some
 * {@link WhileyCompile} tasks to each source set.
 *
 * @author Henry J. Wylde
 * @since 0.1.0
 */
class WhileyPlugin implements Plugin<Project> {

    protected Project project

    protected final FileResolver fileResolver

    /**
     * Creates a new project with the injected file resolver.
     *
     * @param fileResolver the file resolver.
     */
    @Inject
    WhileyPlugin(FileResolver fileResolver) {
        assert fileResolver, 'fileResolver cannot be null'

        this.fileResolver = fileResolver
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void apply(Project project) {
        assert project, 'project cannot be null'

        this.project = project

        project.plugins.apply(JavaPlugin)

        configureSourceSets()
    }

    /**
     * Configures the source sets provided by the {@link JavaPlugin}.
     * <p>
     * This method does the following:
     * <ul>
     * <li>adds the {@link WhileySourceSet} properties to all source sets</li>
     * <li>adds a compile task to all source sets for the whiley source</li>
     * </ul>
     */
    private void configureSourceSets() {
        def whileyRuntime = new WhileyRuntime(project)

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
            project.task(compileTaskName, type: WhileyCompile) {
                description "Compiles Whiley source '$set.name:whiley'."

                source whileySourceSet.whiley
                destinationDir = set.output.classesDir
                classpath = set.compileClasspath
                bootpath = whileyRuntime.inferWhileyBootpath(set.runtimeClasspath)
                whileyClasspath = whileyRuntime.inferWhileyClasspath(set.compileClasspath)

                whileyOptions.whileydir = project.file("src/$set.name/whiley")
            }

            project.tasks.getByName(set.classesTaskName).dependsOn compileTaskName
        }
    }
}

