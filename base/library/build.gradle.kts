val junitVersion = "5.5.0-M1"
val hamkrestVersion = "1.7.0.0"
val mockkVersion="1.9.3.kotlin12"
val guavaVersion="11.0.2"
dependencies {
    compile("il.ac.technion.cs.softwaredesign", "primitive-storage-layer", "1.1")
    testCompile("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testCompile("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testCompile("com.natpryce:hamkrest:$hamkrestVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    compile( "com.google.guava:guava: version=$guavaVersion")
}