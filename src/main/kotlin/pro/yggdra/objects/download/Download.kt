package pro.yggdra.objects.download

import io.ktor.util.date.*
import kotlinx.coroutines.runBlocking
import pro.yggdra.JavaPaper
import pro.yggdra.objects.ProjectType
import pro.yggdra.objects.builder.DownloadAction
import pro.yggdra.util.Messenger
import java.io.File
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.timer
import kotlin.io.path.absolutePathString
import kotlin.io.path.fileSize

class Download (
    val name: String,
    val sha256: String,
    val build: Int,
    val projectType: ProjectType,
    val projectName: String,
    val version: String
) {
    private val downloadTemplate = "https://api.papermc.io/%s/projects/%s/versions/%s/builds/%s/downloads/%s"
    val downloadLink = downloadTemplate.format(JavaPaper.get().apiVersion, projectType.id(), version, build, name)

    fun downloadAutoName(folder: File) = downloadAutoName(folder, {_,_->}, {})
    fun downloadAutoName(folder: File, after: Runnable) = downloadAutoName(folder, {_,_->}, after)
    fun downloadAutoName(folder: File, percentage: (percent: Long, speed: Float) -> Unit) = downloadAutoName(folder, percentage) {}
    fun downloadAutoName(folder: File, percentage: (percent: Long, speed: Float) -> Unit, after: Runnable): DownloadAction {
        val destination = Path.of(folder.absolutePath + File.separator + name)
        destination.toFile().mkdirs()
        return download(destination, percentage, after)
    }

    fun downloadAutoName(folder: Path) = downloadAutoName(folder, {_,_->}, {})
    fun downloadAutoName(folder: Path, after: Runnable) = downloadAutoName(folder, {_,_->}, after)
    fun downloadAutoName(folder: Path, percentage: (percent: Long, speed: Float) -> Unit) = downloadAutoName(folder, percentage) {}
    fun downloadAutoName(folder: Path, percentage: (percent: Long, speed: Float) -> Unit, after: Runnable): DownloadAction {
        val destination = Path.of(folder.absolutePathString() + File.separator + name)
        destination.toFile().mkdirs()
        return download(destination, percentage, after)
    }

    fun download(file: File) = download(file, {_,_->}, {})
    fun download(file: File, after: Runnable) = download(file, {_,_->}, after)
    fun download(file: File, percentage: (percent: Long, speed: Float) -> Unit) = download(file, percentage) {}
    fun download(file: File, percentage: (percent: Long, speed: Float) -> Unit, after: Runnable) = download(file.toPath(), percentage, after)

    fun download(file: Path) = download(file, {_,_->}, {})
    fun download(file: Path, after: Runnable) = download(file, {_,_->}, after)
    fun download(file: Path, percentage: (percent: Long, speed: Float) -> Unit) = download(file, percentage) {}
    fun download(file: Path, percentage: (percent: Long, speed: Float) -> Unit, after: Runnable): DownloadAction {
        file.toFile().createNewFile()
        val start = getTimeMillis()
        Messenger.info("Downloading $name from build $build, please wait...")
        val url = URI.create(downloadLink).toURL()
        val connection = url.openConnection()
        val fileSize = connection.contentLengthLong
        val previousSize = AtomicLong(file.fileSize())
        val downloaded = fileSize == file.fileSize()
        val action = DownloadAction(url, file, percentage, after) { action ->
            runBlocking {
                if (!downloaded) {
                    timer("downloading", false, period = 1000) {
                        val newSize = file.fileSize()
                        val percent = newSize * 100 / fileSize
                        if (percent < 100) {
                            val speed = (newSize - previousSize.get()) * 0.00001f
                            val speedStr = String.format(Locale.ROOT, "%.1f mb/s", speed)
                            action.percentage(percent, speed)
                            Messenger.info("Downloaded $percent%... ($speedStr)")
                            previousSize.set(newSize)
                        } else this.cancel()
                    }
                    Files.copy(connection.getInputStream(), file, StandardCopyOption.REPLACE_EXISTING)
                }
                val end = getTimeMillis()
                val s = String.format(Locale.ROOT, "%.3fs", (end - start) / 1000f)
                Messenger.info("Downloaded successfully! (Took $s)")
                action.after.run()
            }
        }
        return action
    }

    override fun toString(): String {
        return "Download(\n" +
                "\tname='$name', \n" +
                "\tsha256='$sha256', \n" +
                "\tbuild=$build, \n" +
                "\tprojectType=$projectType, \n" +
                "\tprojectName='$projectName', \n" +
                "\tversion='$version', \n" +
                "\tdownloadLink='$downloadLink'\n" +
                ")"
    }

}