plugins {
    application
}

application {
    mainClass.set("org.toffan.tool.GenerateAst")
}

sourceSets {
    main {
        java {
            srcDir(".")
        }
    }
}

task("generate", JavaExec::class) {
    classpath = sourceSets["main"].runtimeClasspath

    mainClass.set("org.toffan.tool.GenerateAst")
    args("../lox")
}
