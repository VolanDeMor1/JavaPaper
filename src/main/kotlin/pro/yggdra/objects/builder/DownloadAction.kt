package pro.yggdra.objects.builder

import java.net.URL
import java.nio.file.Path

class DownloadAction(
    var url: URL,
    var destination: Path,
    var percentage: (percentage: Long, speed: Float) -> Unit,
    var after: Runnable,
    private val download: (action: DownloadAction) -> Unit
) {

    fun percentage(percentage: (percentage: Long, speed: Float) -> Unit) = apply { this.percentage = percentage }
    fun after(after: Runnable) = apply { this.after = after }
    fun start() = download(this)

    override fun toString(): String {
        return "DownloadAction {\n" +
                "destination: $destination\n" +
                "percentage: $percentage\n" +
                "after: $after\n" +
                "}"
    }
}