package pro.yggdra

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pro.yggdra.objects.*
import pro.yggdra.util.BuildsDeserializer
import pro.yggdra.util.Messenger
import pro.yggdra.util.VersionGroupBuildsDeserializer
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.util.jar.JarFile

class JavaPaper(
    val apiVersion: String? = "v2",
    debug: Boolean = false
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
        Messenger.allowed = debug
    }

    companion object {
        private val javaPaper = JavaPaper()
        fun get(): JavaPaper = javaPaper
    }

    suspend fun latestBuild(projectType: ProjectType, version: String): Build
            = builds(projectType, version).builds.last()

    suspend fun buildFrom(projectType: ProjectType, jar: String): Build? = withContext(Dispatchers.IO) {
        val hash = Files.newInputStream(Path.of(jar)).use { inputStream ->
            MessageDigest.getInstance("SHA-256").digest(inputStream.readAllBytes()).toHex()
        }

        JarFile(jar).use { jarFile ->
            val entry = jarFile.getJarEntry("META-INF/versions.list")
            if (entry != null && !entry.isDirectory) {
                val inputStream = jarFile.getInputStream(entry)
                val data = inputStream.bufferedReader().use { reader ->
                    reader.readText().split("\t")
                }
                builds(projectType, data[1]).builds.firstOrNull { it.downloads.application.sha256 == hash }
            } else {
                null
            }
        }
    }

    private fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }

    suspend fun projects(): Projects {
        val response = client.get("https://api.papermc.io/$apiVersion/projects")
        return response.body<Projects>()
    }

    suspend fun project(projectType: ProjectType): Project {
        val response = client.get("https://api.papermc.io/$apiVersion/projects/${projectType.id()}")
        return response.body<Project>()
    }

    suspend fun version(projectType: ProjectType, version: String): Version {
        val response = client.get("https://api.papermc.io/$apiVersion/projects/${projectType.id()}/versions/$version")
        return response.body<Version>()
    }

    suspend fun builds(projectType: ProjectType, version: String): Builds {
        val response = client.get("https://api.papermc.io/$apiVersion/projects/${projectType.id()}/versions/$version/builds")
        return response.body<Builds>()
    }

    suspend fun versionGroup(projectType: ProjectType, family: String): VersionGroup {
        val response = client.get("https://api.papermc.io/$apiVersion/projects/${projectType.id()}/version_group/$family")
        return response.body<VersionGroup>()
    }

    suspend fun versionGroupBuilds(projectType: ProjectType, family: String): VersionGroupBuilds {
        val response = client.get("https://api.papermc.io/$apiVersion/projects/${projectType.id()}/version_group/$family/builds")
        return response.body<VersionGroupBuilds>()
    }

    fun close() {
        client.close()
    }

}