import io.ktor.util.date.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import pro.yggdra.JavaPaper
import pro.yggdra.objects.ProjectType
import java.nio.file.Path
import kotlin.io.path.absolutePathString

class JavaPaperTest {
    private val paper = JavaPaper()

    @Test
    fun projects() {
        runBlocking {
            for (f in paper.projects().projects) {
                val project = paper.project(f)
                println(project.projectType)
                println(project.projectName)
                println(project.versionGroups.joinToString())
                println(project.versions.joinToString())
            }
        }
    }

    @Test
    fun project() {
        runBlocking {
            val project = paper.project(ProjectType.PAPER)
            println(project.version("1.19").builds.joinToString())
        }
    }

    @Test
    fun version() {
        runBlocking {
            println(paper.version(ProjectType.PAPER, "1.20").builds.joinToString())
        }
    }

    @Test
    fun builds() {
        runBlocking {
            paper.builds(ProjectType.PAPER, "1.20").builds.forEach { build ->
                println(build.build)
            }
        }
    }

    @Test
    fun buildFrom() {
        runBlocking {
            val start = getTimeMillis()
            val build = paper.buildFrom(ProjectType.PAPER, Path.of("downloads/paper-1.20-9.jar").absolutePathString())
            println("Build id: " + build?.build)
            println("Build time: " + build?.time)
            val end = getTimeMillis()
            println("Took ${end - start} ms")
        }
    }

    @Test
    fun download() {
        runBlocking {
            paper.builds(ProjectType.PAPER, "1.20")
                .builds.last().downloads.application
                .downloadAutoName(Path.of("downloads"))
                .percentage { percent, speed -> // OPTIONAL
                    println("Downloaded $percent%... (Speed: $speed mb/s)")
                }
                .after { // OPTIONAL
                    println("Downloaded successfully!")
                }
                .start()
        }
    }

    @Test
    fun versionGroup() {
        runBlocking {
            val group = paper.versionGroup(ProjectType.PAPER, "1.19")
            group.versions.forEach {
                println(it)
            }
        }
    }

    @Test
    fun versionGroupBuilds() {
        runBlocking {
            val builds = paper.versionGroupBuilds(ProjectType.PAPER, "1.20")
            builds.builds.forEach {
                println("${it.build} ${it.time}")
            }
        }
    }

}