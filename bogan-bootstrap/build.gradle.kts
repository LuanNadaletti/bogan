plugins { id("java") }

dependencies {
    implementation(project(":bogan-di"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}
