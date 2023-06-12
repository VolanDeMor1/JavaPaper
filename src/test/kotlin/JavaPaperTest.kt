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
            println(project.version("1.19").buildIds.joinToString())
        }
    }

    @Test
    fun version() {
        runBlocking {
            println(paper.version(ProjectType.PAPER, "1.20").buildIds.joinToString())
        }
    }

    @Test
    fun builds() {
        runBlocking {
            paper.builds(ProjectType.PAPER, "1.20").builds.forEach { build ->
                println(build)
            }
        }
    }

    @Test
    fun buildFromId() {
        runBlocking {
            val build = paper.buildFrom(ProjectType.PAPER, "1.20", 10)
            println(build?.toString())
        }
    }

    @Test
    fun buildFromFile() {
        runBlocking {
            val start = getTimeMillis()
            val build = paper.buildFrom(ProjectType.PAPER, Path.of("downloads/paper-1.20-10.jar").absolutePathString())
            println(build?.toString())
            val end = getTimeMillis()
            println("Took ${end - start} ms")
        }
    }

    @Test
    fun download() {
        runBlocking {
            val build = paper.latestBuild(ProjectType.PAPER, "1.20")
            println("Downloading build ${build.id}")
            build.downloadAutoName(Path.of("downloads"))
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
                println("${it.id} ${it.time}")
            }
        }
    }

}