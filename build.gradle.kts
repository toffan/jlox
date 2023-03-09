/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn more about Gradle by exploring our samples at https://docs.gradle.org/7.5.1/samples
 * This project uses @Incubating APIs which are subject to change.
 */
plugins {
    application
}

application {
    mainClass.set("org.toffan.lox.Lox")
}

sourceSets {
    main {
        java {
            srcDir("src")
        }
    }
}

task("generate", JavaExec::class) {
    classpath = sourceSets["main"].runtimeClasspath

    mainClass.set("org.toffan.tool.GenerateAst")
    args("src/org/toffan/lox")
}
