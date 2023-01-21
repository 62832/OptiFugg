pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://maven.minecraftforge.net/") }
    }

    resolutionStrategy.eachPlugin {
        if (requested.id.toString() == "org.spongepowered.mixin") {
            useModule("org.spongepowered:mixingradle:${requested.version}")
        }
    }
}