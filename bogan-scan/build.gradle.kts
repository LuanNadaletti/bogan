plugins { id("java") }

dependencies {
    implementation(project(":bogan-di"))
    implementation(project(":bogan-bootstrap"))
    implementation("io.github.classgraph:classgraph:4.8.181")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}
