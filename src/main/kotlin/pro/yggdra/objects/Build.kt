package pro.yggdra.objects

import com.google.gson.annotations.SerializedName
import pro.yggdra.objects.builder.DownloadAction
import pro.yggdra.objects.download.Downloads
import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString

class Build (
    @SerializedName("project_id")
    private var projectType: ProjectType?,
    @SerializedName("project_name")
    private var projectName: String?,
    @SerializedName("version")
    private var version: String?,
    @SerializedName("build")
    val id: Int,
    val time: String,
    val channel: String,
    val promoted: Boolean,
    val changes: Array<Change>,
    val downloads: Downloads
) {

    fun downloadAutoName(folder: File) = downloads.application.downloadAutoName(folder)
    fun downloadAutoName(folder: File, after: Runnable) = downloads.application.downloadAutoName(folder, after)
    fun downloadAutoName(folder: File, percentage: (percent: Long, speed: Float) -> Unit)
        = downloads.application.downloadAutoName(folder, percentage)
    fun downloadAutoName(folder: File, percentage: (percent: Long, speed: Float) -> Unit, after: Runnable)
        = downloads.application.downloadAutoName(folder, percentage, after)

    fun downloadAutoName(folder: Path) = downloads.application.downloadAutoName(folder)
    fun downloadAutoName(folder: Path, after: Runnable) = downloads.application.downloadAutoName(folder, after)
    fun downloadAutoName(folder: Path, percentage: (percent: Long, speed: Float) -> Unit)
            = downloads.application.downloadAutoName(folder, percentage)
    fun downloadAutoName(folder: Path, percentage: (percent: Long, speed: Float) -> Unit, after: Runnable)
            = downloads.application.downloadAutoName(folder, percentage, after)

    fun download(file: File) = downloads.application.download(file)
    fun download(file: File, after: Runnable) = downloads.application.download(file, after)
    fun download(file: File, percentage: (percent: Long, speed: Float) -> Unit)
            = downloads.application.download(file, percentage)
    fun download(file: File, percentage: (percent: Long, speed: Float) -> Unit, after: Runnable)
            = downloads.application.download(file, percentage, after)

    fun download(file: Path) = downloads.application.download(file)
    fun download(file: Path, after: Runnable) = downloads.application.download(file, after)
    fun download(file: Path, percentage: (percent: Long, speed: Float) -> Unit)
            = downloads.application.download(file, percentage)
    fun download(file: Path, percentage: (percent: Long, speed: Float) -> Unit, after: Runnable)
            = downloads.application.download(file, percentage, after)

    override fun toString(): String {
        return "Build(\n" +
                "\tprojectType=$projectType, \n" +
                "\tprojectName='$projectName', \n" +
                "\tversion='$version', \n" +
                "\tbuild=$id, \n" +
                "\ttime='$time', \n" +
                "\tchannel='$channel', \n" +
                "\tpromoted=$promoted, \n" +
                "\tchanges=${changes.contentToString()}, \n" +
                "\tdownloads=$downloads\n" +
                ")"
    }
}