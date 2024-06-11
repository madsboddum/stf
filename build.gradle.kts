plugins {
	id("java")
	id("application")
	id("org.beryx.jlink") version "3.0.1"
}

group = "dk.madsboddum"

repositories {
	mavenCentral()
}

dependencies {
	// https://mvnrepository.com/artifact/commons-cli/commons-cli
	implementation("commons-cli:commons-cli:1.8.0")
	
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

application {
	mainClass.set("dk.madsboddum.stf.Application")
	mainModule.set("dk.madsboddum.stf.cli")
}

tasks.withType<Jar> {
	manifest {
		attributes["Implementation-Version"] = project.version.toString()
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
