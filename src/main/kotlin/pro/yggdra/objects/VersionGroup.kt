package pro.yggdra.objects

import com.google.gson.annotations.SerializedName

open class VersionGroup (
    @SerializedName("project_id")
    val projectType: ProjectType,
    @SerializedName("project_name")
    val projectName: String,
    @SerializedName("version_group")
    val versionGroups: String,
    val versions: Array<String>
)