# gradle-whiley-plugin

[![Project Status: Unsupported - The project has reached a stable, usable state but the author(s) have ceased all work on it. A new maintainer may be desired.](http://www.repostatus.org/badges/1.0.0/unsupported.svg)](http://www.repostatus.org/#unsupported)

This project provides a simple gradle plugin for the [Whiley](http://whiley.org/ "Whiley") language.
It works similar to other plugins that compile to the JVM class specification (e.g. Groovy).

## Usage

To use the Whiley plugin, include in your build script:

```groovy
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
```

The Whiley plugin extends the Java plugin, so you should not apply the Java plugin in the build script.

### Example usage

Two example projects are included in the repository.

* [Example 01 - Verifiable](https://github.com/hjwylde/gradle-whiley-plugin/tree/master/subprojects/example-verifiable "Example 01 - Verifiable")
* [Example 02 - Unverifiable](https://github.com/hjwylde/gradle-whiley-plugin/tree/master/subprojects/example-unverifiable "Example 01 - Unverifiable")

These examples show the exact build script needed to use the plugin and a few ways of customising the compile options.

### Running the examples

Because the Whiley language compiles to Java Bytecode, the plugin can be combined with other Java compatible plugins like [application](http://www.gradle.org/docs/current/userguide/application_plugin.html "The Application Plugin").
Simply use the plugin like you would and call the _run_ task from Gradle (execute _gradle run_ in the terminal, or _gradlew :example-verifiable:run_ from the root project if all you're doing is cloning this repository).

## Tasks

The Whiley plugin adds the following tasks to the project:

Task name|Depends on|Type|Description
---------|----------|----|-----------
compileWhiley|-|WhileyCompile|Compiles production Whiley source files.
compileTestWhiley|-|WhileyCompile|Compiles test Whiley source files.
compile<i>SourceSet</i>Whiley|-|WhileyCompile|Compiles the given source set's Whiley source files.

As well as adding in the tasks, the plugin also adds in the following dependencies:

Task name|Depends on
---------|----------
classes|compileWhiley
testClasses|compileTestWhiley
<i>sourceSet</i>Classes|compile<i>SourceSet</i>Whiley

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

```groovy
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
```

*IMPORTANT*:
Currently due to the way the Whiley compiler works, changing the project layout for the Whiley source files will break the compilation script as some strings are hardcoded in.
See [issue #3](https://github.com/hjwylde/gradle-whiley-plugin/issues/3 "Source Set Directories").

## Dependency management

The plugin depends on the Whiley Development Kit (WDK) files in order to be able to compile the source files.
It will attempt to infer both the bootpath (whiley runtime files) and the whiley classpath (classpath to search for the compiler) based on the compile time dependencies.
The inference is done by searching for an artifact with the name of _whiley-all_.
It is a requirement that neither of these paths are empty, so it is recommended to add it in as described in the following section.

This plugin has been tested with the following WDK versions:
* v0.3.22
* v0.3.23
* v0.3.24
* v0.3.25

### Adding the WDK dependency

All Whiley projects need to be compiled with the Whiley Runtime Environment and require the Whiley Java Compiler files to run.
The compiler requires the whole Whiley Development Kit, which includes the runtime environment and Java compiler.

To add the Whiley Development Kit (whiley-all) as a dependency to the project:

```groovy
repositories {
    maven { url 'https://github.com/hjwylde/maven-repository/raw/master/repository' }
}

dependencies {
    compile 'whiley:whiley-all:0.3.25'
}
```

*NOTE:*
Currently the WDK files aren't in a central maven repository, so I have included them in my [personal one](https://github.com/hjwylde/maven-repository "hjwylde/maven-repository").

## Source set properties

The Whiley plugin adds the following convention properties to each source set in the project:

Property name|Type|Default value|Description
-------------|----|-------------|-----------
whiley|SourceDirectorySet (read-only)|Not null|The Whiley source files of this source set. Contains all .whiley files found in the Whiley source directories
whiley srcDirs|Set<File>|[projectDir/src/name/whiley]|The source directories containing the Whiley source files of this source set
allWhiley|SourceDirectorySet (read-only)|Not null|All Whiley source files of this source set. Contains all .whiley files found in the Whiley source directories

The Whiley plugin also modifies some source set properties:

Property name|Change
-------------|------
allSource|Adds all source files found in the Whiley source directories

## WhileyCompile task

TODO

