plugins {
    id("java")
    id("application")
}

dependencies {
    implementation(project(":bogan-bootstrap"))
    implementation(project(":bogan-di"))
    implementation(project(":bogan-scan"))
}

application {
    mainClass.set("com.example.MyApp")
}
