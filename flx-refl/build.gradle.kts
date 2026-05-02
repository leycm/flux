dependencies {
    implementation(project(":api"))
    implementation(libs.leycm.init)
    compileOnly(libs.annos.jetbrains)
}

tasks.named("sourcesJar") {
    mustRunAfter(":api:jar")
}
