package com.hjwylde.gradle.plugins.whiley

import static org.gradle.api.tasks.SourceSet.MAIN_SOURCE_SET_NAME

import org.gradle.api.InvalidUserDataException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

/**
 * TODO: Documentation
 *
 * @author Henry J. Wylde
 *
 * @since 1.0.0, 05/02/2014
 */
class WhileyPlugin implements Plugin<Project> {

    private Project project

    /**
     * {@inheritDoc}
     */
    @Override
    void apply(Project project) {
        checkEnvironment()

        this.project = project

        project.plugins.apply(JavaPlugin.class)

        configureTasks()
    }

    private void checkEnvironment() {
        if (!System.env.WHILEY_HOME)
            throw new InvalidUserDataException('Environment variable WHILEY_HOME is not set')
    }

    private void configureTasks() {
        project.sourceSets.all { set ->
            def name = set.name == MAIN_SOURCE_SET_NAME ? '' : set.name
            def taskName = 'compile' + name.capitalize() + 'Whiley'

            def task = project.task(taskName, type: WhileyCompile) {
                description "Compiles Whiley source '$set.name:whiley'."

                sourceSet = set
            }

            project.tasks.getByName(set.classesTaskName).dependsOn task
        }
    }
}

