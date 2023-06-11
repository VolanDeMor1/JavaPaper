package pro.yggdra.objects

import com.google.gson.annotations.SerializedName

class Builds(
    @SerializedName("project_id")
    val projectType: ProjectType,
    @SerializedName("project_name")
    val projectName: String,
    val version: String,
    val builds: Array<Build>,
)