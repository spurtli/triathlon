import org.gradle.jvm.tasks.Jar

val beamVersion = "2.24.0"
val log4jVersion = "2.13.3"

plugins {
  // Language
  kotlin("jvm") version "1.4.10"

  // IDE
  idea

  // Documentation
  id("org.jetbrains.dokka") version "1.4.10"
}

repositories {
  jcenter()
}

dependencies {
  // Language
  implementation(kotlin("stdlib"))
  implementation(kotlin("reflect"))

  // Beam
  implementation("org.apache.beam:beam-runners-direct-java:$beamVersion")
  implementation("org.apache.beam:beam-runners-google-cloud-dataflow-java:$beamVersion")

  // Logging
  implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
  implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
  implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")

  // Tests
  testImplementation("junit:junit:4.13.2")
}

tasks {
  val dokkaJar by creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(dokkaJavadoc)
  }

  val sourcesJar by creating(Jar::class) {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    description = "Creates sources artifact"
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
  }

  artifacts {
    add("archives", sourcesJar)
    add("archives", dokkaJar)
  }
}
