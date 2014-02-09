package com.hjwylde.gradle.plugins.whiley

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
        this.project = project

        project.plugins.apply(JavaPlugin.class)

        configureSourceSetDefaults()
    }

    void configureSourceSetDefaults() {
        def task = project.task('compileWhiley', type: WhileyCompile) {
            description = "Compiles Whiley source 'main:whiley'."
        }

        project.tasks.getByName('classes').dependsOn task
    }
}

