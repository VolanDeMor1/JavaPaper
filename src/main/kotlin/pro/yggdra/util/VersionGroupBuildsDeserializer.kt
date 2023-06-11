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
        val projectType = ProjectType.valueOf(jsonObject["project_id"].asString.uppercase())
        val projectName = jsonObject["project_name"].asString
        val versionGroup = jsonObject["version_group"].asString
        val versions = jsonObject.getAsJsonArray("versions").map { it.asString }.toTypedArray()

        val builds = jsonObject.getAsJsonArray("builds").map {
            val buildObject = it.asJsonObject
            val version = buildObject["version"].asString
            val build = buildObject["build"].asInt
            val time = buildObject["time"].asString
            val channel = buildObject["channel"].asString
            val promoted = buildObject["promoted"].asBoolean
            val changes = context.deserialize<Array<Change>>(buildObject.getAsJsonArray("changes"), Array<Change>::class.java)

            val downloadsJson = buildObject.getAsJsonObject("downloads")
            val downloads = Downloads(
                mapDownload(build, projectType, projectName, version, downloadsJson["application"].asJsonObject),
                mapDownload(build, projectType, projectName, version, downloadsJson["mojang-mappings"].asJsonObject)
            )

            Build(projectType, projectName, version, build, time, channel, promoted, changes, downloads)
        }.toTypedArray()

        return VersionGroupBuilds(projectType, projectName, versionGroup, versions, builds)
    }

    private fun mapDownload(build: Int, projectType: ProjectType, projectName: String, version: String, json: JsonObject): Download {
        val name = json["name"].asString
        val sha256 = json["sha256"].asString

        return Download(name, sha256, build, projectType, projectName, version)
    }
}
