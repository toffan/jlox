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
