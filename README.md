# Gradle Whiley Plugin

This project provides a simple gradle plugin for the [Whiley](http://whiley.org/ "Whiley") language. It works similar to other plugins that compile to the JVM class specification (e.g. Groovy).

## Usage

To use the Whiley plugin, include in your build script:

    buildscript {
        repositories {
            mavenCentral()
            maven { url 'https://github.com/hjwylde/maven-repository/raw/master/repository' }
        }

        dependencies {
            classpath 'com.hjwylde.gradle.whiley.plugin:gradle-whiley-plugin:+'
        }
    }

    apply plugin: 'whiley'

The Whiley plugin extends the Java plugin, so you should not apply the Java plugin in the build script.

### Example usage

Two example projects are included in the repository.

* [Example 01 - Verifiable](https://github.com/hjwylde/gradle-whiley-plugin/tree/master/subprojects/example-verifiable "Example 01 - Verifiable")
* [Example 02 - Unverifiable](https://github.com/hjwylde/gradle-whiley-plugin/tree/master/subprojects/example-unverifiable "Example 01 - Unverifiable")

These examples show the exact build script needed to use the plugin and a few ways of customising the compile options.

### Running the examples

Because the Whiley language compiles to Java Bytecode, the plugin can be combined with other Java compatible plugins like [application](http://www.gradle.org/docs/current/userguide/application_plugin.html "The Application Plugin"). Simply use the plugin like you would and call the _run_ task from Gradle (execute _gradle run_ in the terminal, or _gradlew :example-verifiable:run_ from the root project if all you're doing is cloning this repository).

## Tasks

The Whiley plugin adds the following tasks to the project:

Task name|Depends on|Type|Description
---------|----------|----|-----------
compileWhiley|-|WhileyCompile|Compiles production Whiley source files.
compileTestWhiley|-|WhileyCompile|Compiles test Whiley source files.
compileSourceSetWhiley|-|WhileyCompile|Compiles the given source set's Whiley source files.

As well as adding in the tasks, the plugin also adds in the following dependencies:

Task name|Depends on
---------|----------
classes|compileWhiley
testClasses|compileTestWhiley
_sourceSet_Classes|compileSourceSetWhiley

## Project layout

The Whiley plugin assumes the following project layout:

Directory|Meaning
---------|-------
src/main/java|Production Java source
src/main/resources|Production resources
src/main/whiley|Production Whiley source
src/test/java|Test Java source
src/test/resources|Test resources
src/test/whiley|Test Whiley source
src/_sourceSet_/java|Java source for the given source set
src/_sourceSet_/resources|Resources for the given source set
src/_sourceSet_/whiley|Whiley source for the given source set

### Changing the project layout

Similar to the Java plugin, you can change the project layout by modifying the whiley source set container:

    sourceSets {
        main {
            whiley {
                srcDirs = ['src/whiley']
            }
        }

        foo {
            whiley {
                srcDir 'src/whiley'
            }
        }
    }

*IMPORTANT*:
Currently due to the way the Whiley compiler works, changing the project layout for the Whiley
source files will break the compilation script as some strings are hardcoded in. See [issue #3](https://github.com/hjwylde/gradle-whiley-plugin/issues/3 "Source Set Directories").

## Dependency management

The plugin depends on the Whiley Development Kit (WDK) files in order to be able to compile the
source files. It will always try to use the latest WDK version through a variable dependency.
Therefore, if you have included the WDK as a dependency and changed the Gradle transitive
dependency management settings then you can force the plugin to compile with a different WDK
version.

This plugin has been tested with the following WDK versions:
* v0.3.22

### Adding the WDK dependency

All Whiley projects need to be compiled with the Whiley Runtime Environment and require the Whiley
Java Compiler files to run.

To add these files as a dependency to the project:

    repositories {
        maven { url 'https://github.com/hjwylde/maven-repository/raw/master/repository' }
    }

    dependencies {
        compile 'whiley:wyrt:0.3.22'

        runtime 'whiley:wyjc:0.3.22'
    }

*NOTE:*
Currently the WDK files aren't in a central maven repository, so I have included them in my
personal one.

## Source set properties

The Whiley plugin adds the following convention properties to each source set in the project:

Property name|Type|Default value|Description
-------------|----|-------------|-----------
whiley|SourceDirectorySet (read-only)|Not null|The Whiley source files of this source set. Contains all .whiley files found in the Whiley source directories.
whiley srcDirs|Set<File>|[projectDir/src/name/whiley]|The source directories containing the Whiley source files of this source set.
allWhiley|SourceDirectorySet (read-only)|Not null|All Whiley source files of this source set. Contains all .whiley files found in the Whiley source directories.

The Whiley plugin also modifies some source set properties:

Property name|Change
-------------|------
allSource|Adds all source files found in the Whiley source directories

## WhileyCompile task

TODO

