package pro.yggdra.objects

import com.google.gson.annotations.SerializedName
import pro.yggdra.JavaPaper

class Project(
    @SerializedName("project_id")
    val projectType: ProjectType,
    @SerializedName("project_name")
    val projectName: String,
    @SerializedName("version_groups")
    val versionGroups: Array<String>,
    val versions: Array<String>
) {
    suspend fun version(version: String): Version {
        return JavaPaper.get().version(projectType, version)
    }
}