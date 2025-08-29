plugins { id("java") }
allprojects {
    group = "br.com.bogan"
    version = "0.1.0-SNAPSHOT"
    repositories { mavenCentral() }
}
subprojects {
    apply(plugin = "java")
    java {
        toolchain { languageVersion.set(JavaLanguageVersion.of(21)) }
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    tasks.withType<JavaCompile>().configureEach { options.encoding = "UTF-8" }
    tasks.withType<Test>().configureEach { useJUnitPlatform() }
}
