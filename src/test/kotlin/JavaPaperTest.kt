import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import pro.yggdra.JavaPaper
import pro.yggdra.objects.ProjectType
import java.nio.file.Path

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
            val project = paper.project("paper")
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
    fun download() {
        runBlocking {
            val builds = paper.builds(ProjectType.PAPER, "1.20")
            builds.builds.last().downloads.application.downloadAutoName(Path.of("downloads")).start()
        }
    }

}