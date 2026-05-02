dependencies {
    implementation(project(":api"))
}

tasks.named("sourcesJar") {
    mustRunAfter(":api:jar")
}
