package pro.yggdra.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import pro.yggdra.objects.*
import pro.yggdra.objects.download.Download
import pro.yggdra.objects.download.Downloads
import java.lang.reflect.Type

class VersionGroupBuildsDeserializer : JsonDeserializer<VersionGroupBuilds> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): VersionGroupBuilds {
        val jsonObject = json.asJsonObject
        val projectType = ProjectType.valueOf(jsonObject.get("project_id").asString.uppercase())
        val projectName = jsonObject.get("project_name").asString
        val versionGroup = jsonObject.get("version_group").asString
        val versions = jsonObject.getAsJsonArray("versions").map { it.asString }.toTypedArray()

        val buildsArray = jsonObject.getAsJsonArray("builds")
        val builds = buildsArray.map {
            val buildObject = it.asJsonObject
            val version = buildObject.get("version").asString
            val build = buildObject.get("build").asInt
            val time = buildObject.get("time").asString
            val channel = buildObject.get("channel").asString
            val promoted = buildObject.get("promoted").asBoolean
            val changesArray = buildObject.getAsJsonArray("changes")
            val changes = context.deserialize<Array<Change>>(changesArray, Array<Change>::class.java)
            val downloadsJson = buildObject.getAsJsonObject("downloads")

            val downloads = Downloads(
                application = mapDownload(build, projectType, projectName, version, downloadsJson.getAsJsonObject("application")),
                mojangMappings = mapDownload(build, projectType, projectName, version, downloadsJson.getAsJsonObject("mojang-mappings"))
            )

            return@map Build(projectType, projectName, version, build, time, channel, promoted, changes, downloads)
        }.toTypedArray()

        return VersionGroupBuilds(projectType, projectName, versionGroup, versions, builds)
    }

    private fun mapDownload(build: Int, projectType: ProjectType, projectName: String, version: String, json: JsonObject): Download {
        val name = json.get("name").asString
        val sha256 = json.get("sha256").asString

        return Download(name, sha256, build, projectType, projectName, version)
    }
}