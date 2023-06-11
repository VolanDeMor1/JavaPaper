package pro.yggdra.objects

import com.google.gson.annotations.SerializedName
import pro.yggdra.objects.download.Downloads

class Build (
    @SerializedName("project_id")
    private var projectType: ProjectType?,
    @SerializedName("project_name")
    private var projectName: String?,
    @SerializedName("version")
    private var version: String?,
    val build: Int,
    val time: String,
    val channel: String,
    val promoted: Boolean,
    val changes: Array<Change>,
    val downloads: Downloads
)