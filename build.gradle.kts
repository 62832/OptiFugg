plugins {
    idea
    eclipse
    id("net.minecraftforge.gradle") version "5.1.+"
    id("org.spongepowered.mixin") version "0.7.+"
}

val mcVersion = "1.19.2"
val forgeVersion = "43.1.1"
val modId = "optifugg"

base.archivesName.set(modId)
version = "0.1-$mcVersion"
group = "gripe.90.$modId"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

minecraft {
    mappings("official", mcVersion)

    runs.create("client") {
        workingDirectory(project.file("run"))

        property("forge.logging.markers", "REGISTRIES")
        property("forge.logging.console.level", "debug")

        mods.create(modId) {
            source(sourceSets.getByName("main"))
        }
    }
}

dependencies {
    minecraft("net.minecraftforge:forge:$mcVersion-$forgeVersion")
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

mixin {
    add(sourceSets.getByName("main"), "optifugg.mixins.refmap.json")
    config("optifugg.mixins.json")
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            val version = project.version.toString();

            expand(
                    "version"       to version.subSequence(0, version.indexOf('-')),
                    "loaderVersion" to forgeVersion.subSequence(0, forgeVersion.indexOf('.'))
            )
        }
    }

    jar {
        manifest {
            attributes(
                    "Specification-Title"      to project.name,
                    "Specification-Version"    to project.version,
                    "Specification-Vendor"     to "90",
                    "Implementation-Title"     to project.name,
                    "Implementation-Version"   to project.version,
                    "Implementation-Vendor"    to "90"
            )
        }

        from("LICENSE") {
            rename { "${it}_${project.base.archivesName}"}
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
}