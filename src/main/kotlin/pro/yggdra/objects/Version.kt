package pro.yggdra.objects

import com.google.gson.annotations.SerializedName

class Version(
    @SerializedName("project_id")
    val projectType: ProjectType,
    @SerializedName("project_name")
    val projectName: String,
    val version: String,
    @SerializedName("builds")
    val buildIds: Array<Int>
)
