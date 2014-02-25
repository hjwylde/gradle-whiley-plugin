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
 * The plugin for the Whiley language. This plugin extends the {@link JavaPlugin} and adds in some
 * {@link WhileyCompile} tasks to each source set.
 *
 * @author Henry J. Wylde
 *
 * @since 1.0.0, 05/02/2014
 */
class WhileyPlugin implements Plugin<Project> {

    /**
     * A list of the whiley jars that are provided within the Whiley Development Kit that are
     * required for the compiler classpath.
     */
    public static final List<String> WHILEY_CLASSPATH_LIBS = ['jasm', 'wybs', 'wycc', 'wycs', 'wyc',
            'wyil', 'wyjc', 'wyrl', 'wyrt', 'whiley-all']
    /**
     * A list of the whiley jars that are provided wtihin the Whiley Development Kit that are
     * required for the compiler bootpath.
     */
    public static final List<String> WHILEY_BOOTPATH_LIBS = ['wyrt']

    protected Project project

    protected final FileResolver fileResolver

    /**
     * Creates a new project with the injected file resolver.
     *
     * @param fileResolver the file resolver.
     */
    @Inject
    WhileyPlugin(FileResolver fileResolver) {
        this.fileResolver = fileResolver
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void apply(Project project) {
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
                bootpath = inferWhileyBootpath(set.compileClasspath)

                whileyOptions.whileydir = project.file("src/$set.name/whiley")
            }

            project.tasks.getByName(set.classesTaskName).dependsOn compileTaskName
        }
    }

    /**
     * Attempts to infer the bootpath for compilation using the given classpath list.
     *
     * @param classpath the classpath to use to infer the bootpath.
     * @return the inferred bootpath.
     */
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

