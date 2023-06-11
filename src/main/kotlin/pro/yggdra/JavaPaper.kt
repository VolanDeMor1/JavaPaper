package pro.yggdra

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*
import pro.yggdra.objects.*
import pro.yggdra.util.BuildsDeserializer
import pro.yggdra.util.Messenger
import pro.yggdra.util.VersionGroupBuildsDeserializer

class JavaPaper(
    val apiVersion: String? = "v2",
    info: Boolean = true
) {
    private var client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson {
                registerTypeAdapter(Builds::class.java, BuildsDeserializer())
                registerTypeAdapter(VersionGroupBuilds::class.java, VersionGroupBuildsDeserializer())
            }
        }
    }

    init {
        Messenger.allowed = info
    }

    companion object {
        private val javaPaper = JavaPaper()
        fun get(): JavaPaper = javaPaper
    }

    suspend fun projects(): Projects {
        val response = client.get("https://api.papermc.io/$apiVersion/projects")
        return response.body<Projects>()
    }

    suspend fun project(projectType: ProjectType) = project(projectType.name.lowercase())

    suspend fun project(projectType: String): Project {
        val response = client.get("https://api.papermc.io/$apiVersion/projects/$projectType")
        return response.body<Project>()
    }

    suspend fun version(projectType: ProjectType, version: String)
        = version(projectType.name.lowercase(), version)

    suspend fun version(projectType: String, version: String): Version {
        val response = client.get("https://api.papermc.io/$apiVersion/projects/$projectType/versions/$version")
        return response.body<Version>()
    }

    suspend fun builds(projectType: ProjectType, version: String)
            = builds(projectType.name.lowercase(), version)

    suspend fun builds(projectType: String, version: String): Builds {
        val response = client.get("https://api.papermc.io/$apiVersion/projects/$projectType/versions/$version/builds")
        return response.body<Builds>()
    }

    suspend fun versionGroup(projectType: ProjectType, family: String)
            = versionGroup(projectType.name.lowercase(), family)

    suspend fun versionGroup(projectType: String, family: String): VersionGroup {
        val response = client.get("https://api.papermc.io/$apiVersion/projects/$projectType/version_group/$family")
        return response.body<VersionGroup>()
    }

    suspend fun versionGroupBuilds(projectType: ProjectType, family: String)
            = versionGroupBuilds(projectType.name.lowercase(), family)

    suspend fun versionGroupBuilds(projectType: String, family: String): VersionGroupBuilds {
        val response = client.get("https://api.papermc.io/$apiVersion/projects/$projectType/version_group/$family/builds")
        return response.body<VersionGroupBuilds>()
    }

    fun close() {
        client.close()
    }

}