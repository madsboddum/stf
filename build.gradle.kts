plugins {
	id("java")
	id("application")
}

group = "dk.madsboddum"

repositories {
	mavenCentral()
}

dependencies {
	// https://mvnrepository.com/artifact/commons-cli/commons-cli
	implementation("commons-cli:commons-cli:1.4")
	
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.1.1")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.1.1")
}

application {
	mainClass.set("dk.madsboddum.stf.Application")
}

tasks.withType<Jar> {
	manifest {
		attributes["Implementation-Version"] = project.version.toString()
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
