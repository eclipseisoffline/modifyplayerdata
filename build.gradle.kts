import me.modmuss50.mpp.ReleaseType

plugins {
    alias(libs.plugins.multimod)
}

group = properties["maven_group"] as String
version = properties["version"] as String

multimod {
    id = properties["mod_id"] as String
    name = properties["mod_name"] as String
    description = properties["mod_description"] as String

    archivesBaseName = properties["archives_base_name"] as String

    minecraft {
        minecraft = libs.minecraft
        supported(libs.versions.minecraft.release)
    }

    neoForgeVersion = libs.versions.neoforge

    modPublishOptions {
        changelog = file("CHANGELOG.md").readText()
        type = ReleaseType.of(properties["release_type"] as String)
    }

    modrinthOptions {
        accessToken = providers.gradleProperty("MODRINTH_API_TOKEN")
        projectId = properties["modrinth_project_id"] as String
        minecraftVersions.addAll(libs.versions.minecraft.release.get().split(","))
    }

    githubOptions {
        accessToken = providers.gradleProperty("GITHUB_API_PUBLISH_TOKEN")
        repository = properties["github_repository"] as String
        commitish = properties["git_branch"] as String
    }

    publishToMaven {
        name = "eclipseisoffline"
        url = uri("https://maven.eclipseisoffline.xyz/releases")
        credentials(PasswordCredentials::class)
    }
}
